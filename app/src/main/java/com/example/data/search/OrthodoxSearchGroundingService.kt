package com.example.data.search

import android.util.Log
import com.example.BuildConfig
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONArray
import org.json.JSONObject
import java.util.concurrent.TimeUnit

data class WebSourceChunk(
    val title: String,
    val uri: String
)

data class GroundedSearchResult(
    val query: String,
    val answer: String,
    val webSources: List<WebSourceChunk>,
    val searchQueries: List<String>,
    val timestamp: Long = System.currentTimeMillis()
)

/**
 * Service providing Gemini API with Google Search Grounding (googleSearch tool)
 * for verified Coptic Orthodox Church calendar, liturgical readings, feast dates,
 * biblical psalm commentaries, and spiritual guidelines.
 */
object OrthodoxSearchGroundingService {

    private const val TAG = "OrthodoxSearchService"
    private const val BASE_URL = "https://generativelanguage.googleapis.com/v1beta/models/"

    // Recommended models per guidelines and user specification
    private val MODEL_CANDIDATES = listOf(
        "gemini-3.5-flash",
        "gemini-2.5-flash"
    )

    private val httpClient: OkHttpClient = OkHttpClient.Builder()
        .connectTimeout(45, TimeUnit.SECONDS)
        .readTimeout(60, TimeUnit.SECONDS)
        .writeTimeout(45, TimeUnit.SECONDS)
        .build()

    suspend fun performGroundedSearch(
        userQuery: String,
        userLanguageCode: String = "ar"
    ): Result<GroundedSearchResult> = withContext(Dispatchers.IO) {
        val apiKey = BuildConfig.GEMINI_API_KEY
        if (apiKey.isBlank() || apiKey == "MY_GEMINI_API_KEY") {
            Log.w(TAG, "GEMINI_API_KEY not configured in Secrets panel")
            return@withContext Result.failure(
                IllegalStateException("يرجى إدخال مفتاح Gemini API في لوحة الأسرار (Secrets) لتفعيل البحث الموثق بجوجل.")
            )
        }

        val systemPrompt = if (userLanguageCode == "ar") {
            "أنت باحث كنسي أرثوذكسي قبطي خبير في صلوات الأجبية، تاريخ الكنيسة، الأعياد والأصوام، وتفاسير الآباء. " +
            "استعن بأداة بحث جوجل (googleSearch) لتقديم معلومات حية وموثقة ودقيقة عن التواريخ، المناسبات، والتراث الروحي الأرثوذكسي، مع أسلوب محبة ووقار."
        } else {
            "You are a Coptic Orthodox scholar expert in the Agpeya prayers, Church history, liturgical feasts and fasts, and patristic commentaries. " +
            "Use the googleSearch tool to retrieve up-to-date, grounded, and verified information regarding dates, spiritual events, and Orthodox spiritual heritage."
        }

        // Construct request payload with googleSearch tool for search grounding
        val requestJson = JSONObject().apply {
            val contentsArray = JSONArray().apply {
                put(JSONObject().apply {
                    val partsArray = JSONArray().apply {
                        put(JSONObject().apply {
                            put("text", userQuery)
                        })
                    }
                    put("parts", partsArray)
                })
            }
            put("contents", contentsArray)

            // System instruction
            put("systemInstruction", JSONObject().apply {
                put("parts", JSONArray().apply {
                    put(JSONObject().apply {
                        put("text", systemPrompt)
                    })
                })
            })

            // Search Grounding Tool
            val toolsArray = JSONArray().apply {
                put(JSONObject().apply {
                    put("googleSearch", JSONObject())
                })
            }
            put("tools", toolsArray)
        }

        var lastException: Exception? = null

        // Try candidate models
        for (model in MODEL_CANDIDATES) {
            try {
                val endpointUrl = "${BASE_URL}${model}:generateContent?key=${apiKey}"
                val mediaType = "application/json; charset=utf-8".toMediaType()
                val requestBody = requestJson.toString().toRequestBody(mediaType)

                val request = Request.Builder()
                    .url(endpointUrl)
                    .post(requestBody)
                    .build()

                httpClient.newCall(request).execute().use { response ->
                    val responseCode = response.code
                    val responseBodyString = response.body?.string() ?: ""

                    if (response.isSuccessful && responseBodyString.isNotBlank()) {
                        val parsed = parseGroundingResponse(userQuery, responseBodyString)
                        if (parsed != null) {
                            return@withContext Result.success(parsed)
                        }
                    } else {
                        Log.w(TAG, "Model $model returned HTTP $responseCode: $responseBodyString")
                        lastException = Exception("HTTP $responseCode: $responseBodyString")
                    }
                }
            } catch (e: Exception) {
                Log.e(TAG, "Error querying model $model: ${e.message}", e)
                lastException = e
            }
        }

        Result.failure(lastException ?: Exception("تعذر الاتصال بخدمة البحث الموثق."))
    }

    private fun parseGroundingResponse(query: String, jsonStr: String): GroundedSearchResult? {
        try {
            val root = JSONObject(jsonStr)
            val candidates = root.optJSONArray("candidates") ?: return null
            if (candidates.length() == 0) return null

            val firstCandidate = candidates.optJSONObject(0) ?: return null
            val content = firstCandidate.optJSONObject("content")
            val parts = content?.optJSONArray("parts")

            val answerBuilder = StringBuilder()
            if (parts != null) {
                for (i in 0 until parts.length()) {
                    val partObj = parts.optJSONObject(i)
                    val text = partObj?.optString("text")
                    if (!text.isNullOrBlank()) {
                        if (answerBuilder.isNotEmpty()) answerBuilder.append("\n\n")
                        answerBuilder.append(text)
                    }
                }
            }

            val finalAnswer = answerBuilder.toString()
            if (finalAnswer.isBlank()) return null

            // Extract Grounding Metadata
            val groundingMetadata = firstCandidate.optJSONObject("groundingMetadata")
            val searchQueries = mutableListOf<String>()
            val webSources = mutableListOf<WebSourceChunk>()

            if (groundingMetadata != null) {
                // Queries that Google Search ran
                val queriesArray = groundingMetadata.optJSONArray("webSearchQueries")
                if (queriesArray != null) {
                    for (i in 0 until queriesArray.length()) {
                        val q = queriesArray.optString(i)
                        if (q.isNotBlank()) searchQueries.add(q)
                    }
                }

                // Sources chunks with web title & URI
                val chunksArray = groundingMetadata.optJSONArray("groundingChunks")
                if (chunksArray != null) {
                    for (i in 0 until chunksArray.length()) {
                        val chunkObj = chunksArray.optJSONObject(i)
                        val webObj = chunkObj?.optJSONObject("web")
                        if (webObj != null) {
                            val uri = webObj.optString("uri")
                            val title = webObj.optString("title")
                            if (uri.isNotBlank()) {
                                webSources.add(
                                    WebSourceChunk(
                                        title = if (title.isNotBlank()) title else uri,
                                        uri = uri
                                    )
                                )
                            }
                        }
                    }
                }
            }

            return GroundedSearchResult(
                query = query,
                answer = finalAnswer,
                webSources = webSources,
                searchQueries = searchQueries
            )
        } catch (e: Exception) {
            Log.e(TAG, "Failed to parse grounding response: ${e.message}", e)
            return null
        }
    }
}
