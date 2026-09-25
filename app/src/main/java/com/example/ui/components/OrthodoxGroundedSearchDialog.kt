package com.example.ui.components

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.OpenInBrowser
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.data.search.GroundedSearchResult
import com.example.data.search.OrthodoxSearchGroundingService
import com.example.localization.AppLanguage
import com.example.ui.theme.BurgundyDeep
import com.example.ui.theme.GoldLight
import com.example.ui.theme.GoldPrimary
import kotlinx.coroutines.launch

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun OrthodoxGroundedSearchDialog(
    language: AppLanguage,
    onDismissRequest: () -> Unit,
    initialQuery: String = ""
) {
    val context = LocalContext.current
    val focusManager = LocalFocusManager.current
    val coroutineScope = rememberCoroutineScope()

    var searchQuery by remember { mutableStateOf(initialQuery) }
    var isLoading by remember { mutableStateOf(false) }
    var searchResult by remember { mutableStateOf<GroundedSearchResult?>(null) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    val suggestedQuestions = remember(language) {
        if (language == AppLanguage.ARABIC) {
            listOf(
                "طقس وقراءات اليوم في الكنيسة القبطية",
                "موعد الصوم الكبير وعيد القيامة المجيد",
                "تفسير مزمور 23 (الرب يرعاني) للأجبية",
                "تاريخ صلوات السواعي وتأصيلها الكنسي",
                "أعياد السيدة العذراء مريم وتواريخها"
            )
        } else {
            listOf(
                "Today's Coptic liturgy and readings",
                "Coptic Great Lent and Pascha dates",
                "Patristic meditation on Psalm 23",
                "History of the Coptic Agpeya canonical hours",
                "Feasts of St. Mary in the Coptic Church"
            )
        }
    }

    fun executeSearch(query: String) {
        val clean = query.trim()
        if (clean.isBlank()) return
        focusManager.clearFocus()
        isLoading = true
        errorMessage = null

        coroutineScope.launch {
            val result = OrthodoxSearchGroundingService.performGroundedSearch(
                userQuery = clean,
                userLanguageCode = language.code
            )
            isLoading = false
            result.onSuccess {
                searchResult = it
            }.onFailure { err ->
                errorMessage = err.message ?: if (language == AppLanguage.ARABIC) "حدث خطأ أثناء البحث" else "Search error occurred"
            }
        }
    }

    Dialog(
        onDismissRequest = onDismissRequest,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth(0.96f)
                .fillMaxHeight(0.90f)
                .clip(RoundedCornerShape(24.dp)),
            color = MaterialTheme.colorScheme.surface,
            tonalElevation = 6.dp,
            border = BorderStroke(1.5.dp, GoldPrimary.copy(alpha = 0.4f))
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(20.dp)
            ) {
                // Header
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .size(42.dp)
                            .clip(CircleShape)
                            .background(
                                Brush.radialGradient(
                                    listOf(GoldLight, GoldPrimary, BurgundyDeep)
                                )
                            )
                    ) {
                        Icon(
                            imageVector = Icons.Default.AutoAwesome,
                            contentDescription = null,
                            tint = Color.Black,
                            modifier = Modifier.size(24.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = if (language == AppLanguage.ARABIC) "البحث الأرثوذكسي الموثق" else "Orthodox Grounded Search",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = GoldPrimary
                        )
                        Text(
                            text = if (language == AppLanguage.ARABIC) "مدعوم بـ Google Search Grounding & Gemini" else "Powered by Google Search Grounding",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }

                    IconButton(
                        onClick = onDismissRequest,
                        modifier = Modifier.testTag("btn_close_grounded_search")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Close",
                            tint = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Search Bar Input
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    placeholder = {
                        Text(
                            text = if (language == AppLanguage.ARABIC) "ابحث عن قديس، عيد، أو صلاة في الأجبية..." else "Search for saints, feasts, or prayers...",
                            style = MaterialTheme.typography.bodyMedium
                        )
                    },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = null,
                            tint = GoldPrimary
                        )
                    },
                    trailingIcon = {
                        if (searchQuery.isNotEmpty()) {
                            IconButton(onClick = { searchQuery = "" }) {
                                Icon(
                                    imageVector = Icons.Default.Clear,
                                    contentDescription = "Clear",
                                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                    },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                    keyboardActions = KeyboardActions(onSearch = { executeSearch(searchQuery) }),
                    shape = RoundedCornerShape(16.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = GoldPrimary,
                        unfocusedBorderColor = MaterialTheme.colorScheme.outlineVariant,
                        focusedLabelColor = GoldPrimary
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("grounded_search_input")
                )

                Spacer(modifier = Modifier.height(10.dp))

                // Search Action Button
                Button(
                    onClick = { executeSearch(searchQuery) },
                    enabled = !isLoading && searchQuery.isNotBlank(),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = GoldPrimary,
                        contentColor = Color.Black
                    ),
                    shape = RoundedCornerShape(14.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(46.dp)
                        .testTag("btn_execute_grounded_search")
                ) {
                    if (isLoading) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(18.dp),
                            strokeWidth = 2.dp,
                            color = Color.Black
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                    }
                    Text(
                        text = if (language == AppLanguage.ARABIC) "بحث موثق عبر Google Search" else "Search with Google Grounding",
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Scrollable Content
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .verticalScroll(rememberScrollState())
                ) {
                    // Quick Suggested Queries
                    if (searchResult == null && !isLoading) {
                        Text(
                            text = if (language == AppLanguage.ARABIC) "💡 مقترحات بحث سريعة:" else "💡 Suggested Inquiries:",
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = FontWeight.Bold,
                            color = GoldPrimary
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        FlowRow(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            suggestedQuestions.forEach { suggestion ->
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(12.dp))
                                        .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f))
                                        .border(
                                            1.dp,
                                            MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.4f),
                                            RoundedCornerShape(12.dp)
                                        )
                                        .clickable {
                                            searchQuery = suggestion
                                            executeSearch(suggestion)
                                        }
                                        .padding(horizontal = 12.dp, vertical = 7.dp)
                                ) {
                                    Text(
                                        text = suggestion,
                                        style = MaterialTheme.typography.labelSmall,
                                        color = MaterialTheme.colorScheme.onSurface
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(20.dp))
                    }

                    // Loading State
                    if (isLoading) {
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 40.dp)
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                CircularProgressIndicator(color = GoldPrimary)
                                Spacer(modifier = Modifier.height(14.dp))
                                Text(
                                    text = if (language == AppLanguage.ARABIC) "جاري البحث والتحقق من مصادر Google الحية..." else "Grounding with Google Search sources...",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                    }

                    // Error Message
                    AnimatedVisibility(visible = errorMessage != null) {
                        errorMessage?.let { msg ->
                            Card(
                                shape = RoundedCornerShape(14.dp),
                                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.errorContainer),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 12.dp)
                            ) {
                                Text(
                                    text = msg,
                                    color = MaterialTheme.colorScheme.onErrorContainer,
                                    style = MaterialTheme.typography.bodyMedium,
                                    modifier = Modifier.padding(14.dp)
                                )
                            }
                        }
                    }

                    // Result View
                    AnimatedVisibility(
                        visible = searchResult != null && !isLoading,
                        enter = fadeIn(),
                        exit = fadeOut()
                    ) {
                        searchResult?.let { result ->
                            Card(
                                shape = RoundedCornerShape(18.dp),
                                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                                elevation = CardDefaults.cardElevation(2.dp),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 8.dp)
                            ) {
                                Column(modifier = Modifier.padding(18.dp)) {
                                    // Question Header
                                    Text(
                                        text = "🔍 ${result.query}",
                                        style = MaterialTheme.typography.titleMedium,
                                        fontWeight = FontWeight.Bold,
                                        color = GoldPrimary
                                    )

                                    Spacer(modifier = Modifier.height(12.dp))

                                    // Grounded Answer
                                    Text(
                                        text = result.answer,
                                        style = MaterialTheme.typography.bodyMedium,
                                        color = MaterialTheme.colorScheme.onSurface,
                                        lineHeight = 22.sp
                                    )

                                    Spacer(modifier = Modifier.height(16.dp))

                                    // Grounded Sources Section
                                    if (result.webSources.isNotEmpty()) {
                                        HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f))
                                        Spacer(modifier = Modifier.height(12.dp))

                                        Row(verticalAlignment = Alignment.CenterVertically) {
                                            Icon(
                                                imageVector = Icons.Default.Language,
                                                contentDescription = null,
                                                tint = GoldPrimary,
                                                modifier = Modifier.size(16.dp)
                                            )
                                            Spacer(modifier = Modifier.width(6.dp))
                                            Text(
                                                text = if (language == AppLanguage.ARABIC) "مصادر موثقة من بحث Google:" else "Verified Google Search Sources:",
                                                style = MaterialTheme.typography.labelMedium,
                                                fontWeight = FontWeight.Bold,
                                                color = GoldPrimary
                                            )
                                        }

                                        Spacer(modifier = Modifier.height(8.dp))

                                        Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                                            result.webSources.forEach { source ->
                                                Row(
                                                    verticalAlignment = Alignment.CenterVertically,
                                                    modifier = Modifier
                                                        .fillMaxWidth()
                                                        .clip(RoundedCornerShape(8.dp))
                                                        .background(MaterialTheme.colorScheme.surface)
                                                        .clickable {
                                                            try {
                                                                val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(source.uri))
                                                                context.startActivity(browserIntent)
                                                            } catch (e: Exception) {
                                                                Toast.makeText(context, source.uri, Toast.LENGTH_SHORT).show()
                                                            }
                                                        }
                                                        .padding(horizontal = 10.dp, vertical = 6.dp)
                                                ) {
                                                    Icon(
                                                        imageVector = Icons.Default.OpenInBrowser,
                                                        contentDescription = null,
                                                        tint = GoldPrimary,
                                                        modifier = Modifier.size(14.dp)
                                                    )
                                                    Spacer(modifier = Modifier.width(8.dp))
                                                    Text(
                                                        text = source.title,
                                                        style = MaterialTheme.typography.labelSmall,
                                                        color = MaterialTheme.colorScheme.onSurface,
                                                        maxLines = 1,
                                                        modifier = Modifier.weight(1f)
                                                    )
                                                }
                                            }
                                        }
                                    }

                                    // Search Queries executed
                                    if (result.searchQueries.isNotEmpty()) {
                                        Spacer(modifier = Modifier.height(12.dp))
                                        FlowRow(
                                            horizontalArrangement = Arrangement.spacedBy(6.dp),
                                            verticalArrangement = Arrangement.spacedBy(6.dp)
                                        ) {
                                            result.searchQueries.forEach { sq ->
                                                Surface(
                                                    color = MaterialTheme.colorScheme.surface.copy(alpha = 0.7f),
                                                    shape = RoundedCornerShape(8.dp)
                                                ) {
                                                    Text(
                                                        text = "🔎 $sq",
                                                        style = MaterialTheme.typography.labelSmall,
                                                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                                                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                                    )
                                                }
                                            }
                                        }
                                    }

                                    Spacer(modifier = Modifier.height(14.dp))

                                    // Action buttons: Copy & Share
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                                    ) {
                                        OutlinedButton(
                                            onClick = {
                                                val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                                                clipboard.setPrimaryClip(
                                                    ClipData.newPlainText("Orthodox Search", "${result.query}\n\n${result.answer}")
                                                )
                                                Toast.makeText(
                                                    context,
                                                    if (language == AppLanguage.ARABIC) "تم نسخ الإجابة" else "Copied to clipboard",
                                                    Toast.LENGTH_SHORT
                                                ).show()
                                            },
                                            shape = RoundedCornerShape(10.dp),
                                            modifier = Modifier.weight(1f)
                                        ) {
                                            Icon(
                                                imageVector = Icons.Default.ContentCopy,
                                                contentDescription = null,
                                                modifier = Modifier.size(16.dp)
                                            )
                                            Spacer(modifier = Modifier.width(6.dp))
                                            Text(
                                                text = if (language == AppLanguage.ARABIC) "نسخ" else "Copy",
                                                fontSize = 12.sp
                                            )
                                        }

                                        Button(
                                            onClick = {
                                                val shareIntent = Intent(Intent.ACTION_SEND).apply {
                                                    type = "text/plain"
                                                    putExtra(Intent.EXTRA_SUBJECT, result.query)
                                                    putExtra(Intent.EXTRA_TEXT, "${result.query}\n\n${result.answer}\n\n— تطبيق صلوات الأجبية الأرثوذكسية")
                                                }
                                                context.startActivity(Intent.createChooser(shareIntent, "مشاركة الإجابة"))
                                            },
                                            shape = RoundedCornerShape(10.dp),
                                            colors = ButtonDefaults.buttonColors(containerColor = GoldPrimary),
                                            modifier = Modifier.weight(1f)
                                        ) {
                                            Icon(
                                                imageVector = Icons.Default.Share,
                                                contentDescription = null,
                                                tint = Color.Black,
                                                modifier = Modifier.size(16.dp)
                                            )
                                            Spacer(modifier = Modifier.width(6.dp))
                                            Text(
                                                text = if (language == AppLanguage.ARABIC) "مشاركة" else "Share",
                                                color = Color.Black,
                                                fontWeight = FontWeight.Bold,
                                                fontSize = 12.sp
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
