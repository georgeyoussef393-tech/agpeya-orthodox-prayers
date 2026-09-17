package com.example.audio

import android.content.Context
import android.media.AudioAttributes
import android.media.AudioFormat
import android.media.AudioManager
import android.media.AudioTrack
import android.media.MediaPlayer
import android.media.RingtoneManager
import android.net.Uri
import android.os.Build
import android.util.Log
import com.example.localization.AppLanguage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlin.math.PI
import kotlin.math.exp
import kotlin.math.sin

/**
 * Spiritual & Liturgical sound options for Coptic Agpeya prayer alarms.
 */
enum class SpiritualSound(
    val id: String,
    val arabicName: String,
    val copticName: String,
    val englishName: String,
    val arabicDescription: String,
    val englishDescription: String
) {
    DEFAULT_CHIME(
        id = "default_chime",
        arabicName = "نغمة التنبيه الافتراضية",
        copticName = "Ⲡⲓⲥⲟϩⲓ ⲛ̀ⲧⲉ ⲡⲓⲥⲏⲟⲩ",
        englishName = "System Default Tone",
        arabicDescription = "صوت نغمة التنبيه الافتراضية المحددة في هاتفك",
        englishDescription = "Standard device system notification sound"
    ),
    CHURCH_BELLS(
        id = "church_bells",
        arabicName = "أجراس الكنيسة القبطية الرنانة",
        copticName = "Ⲛⲓⲕⲱⲇⲱⲛ ⲛ̀ⲧⲉ ϯⲉⲕⲕⲗⲏⲥⲓⲁ",
        englishName = "Coptic Church Bells",
        arabicDescription = "رنين أجراس الكنيسة الروحية لدعوة المؤمنين للصلاة",
        englishDescription = "Reverberant church bell chimes calling the faithful to pray"
    ),
    CYMBALS_TRIANGLE(
        id = "cymbals_triangle",
        arabicName = "الدُف والمثلث الكنسي (الألحان)",
        copticName = "Ⲡⲓⲕⲉⲙⲕⲉⲙ ⲛⲉⲙ ⲡⲓⲧⲣⲓⲁⲛⲅⲟⲛ",
        englishName = "Coptic Cymbals & Triangle",
        arabicDescription = "دقات المثلث والدف الروحية لإيقاع الألحان القبطية الأصيلة",
        englishDescription = "Sacred rhythm of the liturgical triangle and cymbals"
    ),
    MONASTIC_WOODEN_NAQOUS(
        id = "wooden_naqous",
        arabicName = "الناقوس الخشبي الديري (سيمانتير)",
        copticName = "Ⲡⲓⲛⲁⲕⲟⲩⲥ ⲛ̀ϣⲉ ⲛ̀ⲧⲉ ⲛⲓⲙⲟⲛⲁⲭⲟⲥ",
        englishName = "Monastic Wooden Semantron",
        arabicDescription = "دقات الناقوس الخشبي التراثي في أديرة مصر لإيقاظ الرهبان لصلاة السحر",
        englishDescription = "Rhythmic wood resonance used in Egyptian desert monasteries for dawn vigil"
    ),
    GOLGOTHA_HYMN_TONE(
        id = "golgotha_hymn",
        arabicName = "لحن غولغوثا والصلوت الروحية",
        copticName = "Ⲡⲓϩⲱⲥ ⲛ̀ⲧⲉ Ⲅⲟⲗⲅⲟⲑⲁ",
        englishName = "Golgotha Contemplative Chimes",
        arabicDescription = "أنغام خشوع وتأمل من وحي صلوات السواعي ومراحم الصليب",
        englishDescription = "Solemn reverent notes inspiring deep contrition and stillness"
    ),
    EPECHOIS_HARMONY(
        id = "coptic_organ",
        arabicName = "نغمات المزامير والقيثارة",
        copticName = "Ϯⲕⲓⲑⲁⲣⲁ ⲛ̀ⲧⲉ Ⲇⲁⲩⲓⲇ",
        englishName = "Psaltery & Harp Chimes",
        arabicDescription = "رنات القيثارة الروحية الداعية لتسبيح الرب بمزامير داود النبي",
        englishDescription = "Gentle celestial harmonic tones evoking David's psaltery"
    );

    fun getName(lang: AppLanguage): String = when (lang) {
        AppLanguage.ARABIC -> arabicName
        AppLanguage.COPTIC -> copticName
        AppLanguage.ENGLISH -> englishName
        AppLanguage.FRENCH -> when (this) {
            DEFAULT_CHIME -> "Sonnerie par défaut"
            CHURCH_BELLS -> "Cloches d'Église Copte"
            CYMBALS_TRIANGLE -> "Cymbales et Triangle Coptes"
            MONASTIC_WOODEN_NAQOUS -> "Simandre Monastique en Bois"
            GOLGOTHA_HYMN_TONE -> "Harmonie Méditative de Golgotha"
            EPECHOIS_HARMONY -> "Harpe et Psaumes Célestes"
        }
        AppLanguage.SPANISH -> when (this) {
            DEFAULT_CHIME -> "Tono predeterminado"
            CHURCH_BELLS -> "Campanas de Iglesia Copta"
            CYMBALS_TRIANGLE -> "Címbalos y Triángulo Coptos"
            MONASTIC_WOODEN_NAQOUS -> "Semantrón Monástico de Madera"
            GOLGOTHA_HYMN_TONE -> "Melodía de Gólgota y Oración"
            EPECHOIS_HARMONY -> "Arpa y Salterio de David"
        }
        AppLanguage.GERMAN -> when (this) {
            DEFAULT_CHIME -> "Standard-Alarmton"
            CHURCH_BELLS -> "Koptische Kirchenglocken"
            CYMBALS_TRIANGLE -> "Koptische Zimbeln und Triangel"
            MONASTIC_WOODEN_NAQOUS -> "Mönchisches Holz-Semantron"
            GOLGOTHA_HYMN_TONE -> "Golgotha Besinnungsmelodie"
            EPECHOIS_HARMONY -> "Harfe und Psalter David"
        }
        AppLanguage.ITALIAN -> when (this) {
            DEFAULT_CHIME -> "Tono predefinito"
            CHURCH_BELLS -> "Campane della Chiesa Copta"
            CYMBALS_TRIANGLE -> "Cembali e Triangolo Copti"
            MONASTIC_WOODEN_NAQOUS -> "Semantron Monastico in Legno"
            GOLGOTHA_HYMN_TONE -> "Melodia di Golgota e Preghiera"
            EPECHOIS_HARMONY -> "Arpa e Salmi di Davide"
        }
    }

    fun getDescription(lang: AppLanguage): String = when (lang) {
        AppLanguage.ARABIC -> arabicDescription
        AppLanguage.COPTIC -> arabicDescription // Provide Arabic as practical explanation
        else -> englishDescription
    }

    companion object {
        fun fromId(id: String): SpiritualSound =
            entries.find { it.id.equals(id, ignoreCase = true) } ?: DEFAULT_CHIME
    }
}

/**
 * Audio Synthesizer & Player that synthesizes liturgical acoustic instruments
 * (Coptic Bells, Desert Monastic Semantron wood strike, Liturgical Triangle & Cymbals,
 * and David's Psaltery) with high fidelity without requiring external audio downloads.
 */
object SpiritualAudioPlayer {
    private const val TAG = "SpiritualAudioPlayer"
    private var activeJob: Job? = null
    private var mediaPlayer: MediaPlayer? = null

    /**
     * Stop any currently playing preview or tone.
     */
    fun stopPreview() {
        try {
            activeJob?.cancel()
            activeJob = null
            mediaPlayer?.stop()
            mediaPlayer?.release()
            mediaPlayer = null
        } catch (e: Exception) {
            Log.e(TAG, "Error stopping audio: ${e.message}")
        }
    }

    /**
     * Preview a spiritual sound using local acoustic synthesis or system notification.
     */
    fun playPreview(
        context: Context,
        sound: SpiritualSound,
        onFinished: (() -> Unit)? = null
    ) {
        stopPreview()

        if (sound == SpiritualSound.DEFAULT_CHIME) {
            try {
                val notificationUri: Uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
                mediaPlayer = MediaPlayer.create(context, notificationUri)?.apply {
                    setOnCompletionListener {
                        stopPreview()
                        onFinished?.invoke()
                    }
                    start()
                }
            } catch (e: Exception) {
                Log.e(TAG, "Default chime preview failed: ${e.message}")
                onFinished?.invoke()
            }
            return
        }

        // Liturgical acoustic synthesis on background dispatcher
        activeJob = CoroutineScope(Dispatchers.Default).launch {
            try {
                when (sound) {
                    SpiritualSound.CHURCH_BELLS -> playChurchBells()
                    SpiritualSound.CYMBALS_TRIANGLE -> playCopticCymbalsAndTriangle()
                    SpiritualSound.MONASTIC_WOODEN_NAQOUS -> playMonasticWoodenSemantron()
                    SpiritualSound.GOLGOTHA_HYMN_TONE -> playGolgothaContemplativeTone()
                    SpiritualSound.EPECHOIS_HARMONY -> playDavidPsalteryChimes()
                    else -> {}
                }
            } catch (e: Exception) {
                Log.e(TAG, "Synthesis playback error: ${e.message}")
            } finally {
                onFinished?.invoke()
            }
        }
    }

    // --- High-Fidelity Liturgical Acoustic Synthesizers (PCM 16-bit) ---

    private fun playChurchBells() {
        val sampleRate = 22050
        val strikes = listOf(
            BellStrike(freq = 523.25, durationMs = 1200, strikeDelayMs = 600), // C5
            BellStrike(freq = 659.25, durationMs = 1300, strikeDelayMs = 700), // E5
            BellStrike(freq = 783.99, durationMs = 1500, strikeDelayMs = 800), // G5
            BellStrike(freq = 1046.50, durationMs = 2000, strikeDelayMs = 1500) // C6 Grand chime
        )

        for (strike in strikes) {
            val samples = generateBellStrikeSamples(sampleRate, strike.freq, strike.durationMs)
            playPcmBuffer(samples, sampleRate)
            try {
                Thread.sleep(strike.strikeDelayMs.toLong())
            } catch (_: InterruptedException) {
                return
            }
        }
    }

    private data class BellStrike(val freq: Double, val durationMs: Int, val strikeDelayMs: Int)

    /**
     * Synthesizes resonant bronze church bell physics with inharmonic strike-tone overtones.
     */
    private fun generateBellStrikeSamples(sampleRate: Int, fundamental: Double, durationMs: Int): ShortArray {
        val numSamples = (sampleRate * (durationMs / 1000.0)).toInt()
        val buffer = ShortArray(numSamples)

        // Bell overtones: strike tone (1.0), prime (0.5), tierce (1.2), quint (1.5), nominal (2.0), superquint (3.0)
        val overtones = doubleArrayOf(1.0, 0.5, 1.19, 1.51, 2.0, 3.01)
        val amplitudes = doubleArrayOf(0.5, 0.3, 0.25, 0.18, 0.2, 0.1)
        val decays = doubleArrayOf(3.2, 1.8, 3.8, 4.5, 5.0, 6.5) // Higher overtones decay faster

        for (i in 0 until numSamples) {
            val t = i.toDouble() / sampleRate
            var sampleVal = 0.0

            for (h in overtones.indices) {
                val f = fundamental * overtones[h]
                val envelope = exp(-decays[h] * t)
                sampleVal += amplitudes[h] * sin(2.0 * PI * f * t) * envelope
            }

            // Warm envelope attack
            val attack = if (t < 0.005) (t / 0.005) else 1.0
            val normalized = (sampleVal * attack * 18000.0).coerceIn(-32767.0, 32767.0)
            buffer[i] = normalized.toInt().toShort()
        }
        return buffer
    }

    /**
     * Coptic Triangle & Cymbals (Duf): high frequency metallic ring + soft percussive bronze chime.
     */
    private fun playCopticCymbalsAndTriangle() {
        val sampleRate = 22050
        // Traditional Coptic rhythm: Triangle ring (Ding) + Cymbals response (Tak - Doff)
        for (rep in 0 until 4) {
            // Triangle high crystalline chime (approx 2400 Hz)
            val triangleSamples = generateMetallicChimeSamples(sampleRate, 2480.0, 350)
            playPcmBuffer(triangleSamples, sampleRate)
            Thread.sleep(220)

            // Cymbals pulse (bronze clash + damping)
            val cymbalSamples = generateCopticCymbalsSamples(sampleRate, 400)
            playPcmBuffer(cymbalSamples, sampleRate)
            Thread.sleep(380)
        }
    }

    private fun generateMetallicChimeSamples(sampleRate: Int, freq: Double, durationMs: Int): ShortArray {
        val numSamples = (sampleRate * (durationMs / 1000.0)).toInt()
        val buffer = ShortArray(numSamples)
        for (i in 0 until numSamples) {
            val t = i.toDouble() / sampleRate
            // Rich inharmonic partials
            val sound = sin(2.0 * PI * freq * t) * 0.6 +
                    sin(2.0 * PI * (freq * 1.58) * t) * 0.3 +
                    sin(2.0 * PI * (freq * 2.32) * t) * 0.2
            val decay = exp(-8.0 * t)
            buffer[i] = (sound * decay * 22000.0).coerceIn(-32767.0, 32767.0).toInt().toShort()
        }
        return buffer
    }

    private fun generateCopticCymbalsSamples(sampleRate: Int, durationMs: Int): ShortArray {
        val numSamples = (sampleRate * (durationMs / 1000.0)).toInt()
        val buffer = ShortArray(numSamples)
        for (i in 0 until numSamples) {
            val t = i.toDouble() / sampleRate
            // Triangle & bronze ring with fast damping
            val partials = sin(2.0 * PI * 1850.0 * t) * 0.4 +
                    sin(2.0 * PI * 3200.0 * t) * 0.3 +
                    sin(2.0 * PI * 850.0 * t) * 0.4
            val decay = exp(-10.0 * t)
            buffer[i] = (partials * decay * 20000.0).coerceIn(-32767.0, 32767.0).toInt().toShort()
        }
        return buffer
    }

    /**
     * Monastic Wooden Semantron (Naqous): hollow wood block acoustic resonance in Egyptian monasteries.
     */
    private fun playMonasticWoodenSemantron() {
        val sampleRate = 22050
        // Rhythmic pattern: Accelerando taps echoing monastic awakening
        val strikes = intArrayOf(500, 420, 350, 300, 240, 240, 240, 450)
        for (interval in strikes) {
            val woodStrike = generateWoodBlockSamples(sampleRate, 720.0, 180)
            playPcmBuffer(woodStrike, sampleRate)
            Thread.sleep(interval.toLong())
        }
    }

    private fun generateWoodBlockSamples(sampleRate: Int, freq: Double, durationMs: Int): ShortArray {
        val numSamples = (sampleRate * (durationMs / 1000.0)).toInt()
        val buffer = ShortArray(numSamples)
        for (i in 0 until numSamples) {
            val t = i.toDouble() / sampleRate
            val woodTone = sin(2.0 * PI * freq * t) * exp(-28.0 * t) * 0.7 +
                    sin(2.0 * PI * (freq * 1.62) * t) * exp(-35.0 * t) * 0.4
            buffer[i] = (woodTone * 26000.0).coerceIn(-32767.0, 32767.0).toInt().toShort()
        }
        return buffer
    }

    /**
     * Golgotha contemplative chord progression: profound solemnity and reverence.
     */
    private fun playGolgothaContemplativeTone() {
        val sampleRate = 22050
        // Minor key sacred progression (A4 -> C5 -> E5 -> A5 chord tones)
        val frequencies = doubleArrayOf(440.0, 523.25, 659.25, 880.0)
        for (freq in frequencies) {
            val noteSamples = generateOrganPipeSamples(sampleRate, freq, 750)
            playPcmBuffer(noteSamples, sampleRate)
            Thread.sleep(450)
        }
    }

    /**
     * David's Psaltery & Harp Chimes: bright gentle ascending spiritual arpeggios.
     */
    private fun playDavidPsalteryChimes() {
        val sampleRate = 22050
        // Pentatonic peaceful prayer melody
        val notes = doubleArrayOf(587.33, 659.25, 783.99, 880.00, 987.77, 1174.66) // D5, E5, G5, A5, B5, D6
        for (n in notes) {
            val harpNote = generateHarpPluckSamples(sampleRate, n, 600)
            playPcmBuffer(harpNote, sampleRate)
            Thread.sleep(220)
        }
    }

    private fun generateOrganPipeSamples(sampleRate: Int, freq: Double, durationMs: Int): ShortArray {
        val numSamples = (sampleRate * (durationMs / 1000.0)).toInt()
        val buffer = ShortArray(numSamples)
        for (i in 0 until numSamples) {
            val t = i.toDouble() / sampleRate
            val pipe = sin(2.0 * PI * freq * t) * 0.6 +
                    sin(2.0 * PI * (freq * 2.0) * t) * 0.25 +
                    sin(2.0 * PI * (freq * 3.0) * t) * 0.1
            val envelope = (1.0 - exp(-30.0 * t)) * exp(-2.5 * t)
            buffer[i] = (pipe * envelope * 22000.0).coerceIn(-32767.0, 32767.0).toInt().toShort()
        }
        return buffer
    }

    private fun generateHarpPluckSamples(sampleRate: Int, freq: Double, durationMs: Int): ShortArray {
        val numSamples = (sampleRate * (durationMs / 1000.0)).toInt()
        val buffer = ShortArray(numSamples)
        for (i in 0 until numSamples) {
            val t = i.toDouble() / sampleRate
            val string = sin(2.0 * PI * freq * t) * 0.7 +
                    sin(2.0 * PI * (freq * 2.0) * t) * 0.2 +
                    sin(2.0 * PI * (freq * 4.0) * t) * 0.1
            val decay = exp(-5.0 * t)
            buffer[i] = (string * decay * 22000.0).coerceIn(-32767.0, 32767.0).toInt().toShort()
        }
        return buffer
    }

    private fun playPcmBuffer(samples: ShortArray, sampleRate: Int) {
        var track: AudioTrack? = null
        try {
            val minBufSize = AudioTrack.getMinBufferSize(
                sampleRate,
                AudioFormat.CHANNEL_OUT_MONO,
                AudioFormat.ENCODING_PCM_16BIT
            )
            val bufSize = maxOf(minBufSize, samples.size * 2)

            track = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                AudioTrack(
                    AudioAttributes.Builder()
                        .setUsage(AudioAttributes.USAGE_NOTIFICATION_RINGTONE)
                        .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                        .build(),
                    AudioFormat.Builder()
                        .setSampleRate(sampleRate)
                        .setChannelMask(AudioFormat.CHANNEL_OUT_MONO)
                        .setEncoding(AudioFormat.ENCODING_PCM_16BIT)
                        .build(),
                    bufSize,
                    AudioTrack.MODE_STATIC,
                    AudioManager.AUDIO_SESSION_ID_GENERATE
                )
            } else {
                @Suppress("DEPRECATION")
                AudioTrack(
                    AudioManager.STREAM_NOTIFICATION,
                    sampleRate,
                    AudioFormat.CHANNEL_OUT_MONO,
                    AudioFormat.ENCODING_PCM_16BIT,
                    bufSize,
                    AudioTrack.MODE_STATIC
                )
            }

            track.write(samples, 0, samples.size)
            track.play()
        } catch (e: Exception) {
            Log.e(TAG, "Track write error: ${e.message}")
        }
    }
}
