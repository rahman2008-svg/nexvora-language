package com.example.audio

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import android.util.Log
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import java.util.Locale

sealed class SpeechState {
    data object Idle : SpeechState()
    data object Listening : SpeechState()
    data class Success(val spokenText: String, val accuracyPercentage: Int, val isMatch: Boolean) : SpeechState()
    data class Error(val message: String) : SpeechState()
}

class SpeechRecognitionManager(private val context: Context) {
    private var speechRecognizer: SpeechRecognizer? = null

    private val _speechState = MutableStateFlow<SpeechState>(SpeechState.Idle)
    val speechState: StateFlow<SpeechState> = _speechState

    private val _partialText = MutableStateFlow("")
    val partialText: StateFlow<String> = _partialText

    init {
        initRecognizer()
    }

    private fun initRecognizer() {
        if (SpeechRecognizer.isRecognitionAvailable(context)) {
            speechRecognizer = SpeechRecognizer.createSpeechRecognizer(context).apply {
                setRecognitionListener(object : RecognitionListener {
                    override fun onReadyForSpeech(params: Bundle?) {
                        _speechState.value = SpeechState.Listening
                    }

                    override fun onBeginningOfSpeech() {}
                    override fun onRmsChanged(rmsdB: Float) {}
                    override fun onBufferReceived(buffer: ByteArray?) {}
                    override fun onEndOfSpeech() {}

                    override fun onError(error: Int) {
                        val message = when (error) {
                            SpeechRecognizer.ERROR_NO_MATCH -> "No speech recognized. Try speaking clearly into the mic."
                            SpeechRecognizer.ERROR_NETWORK -> "Network issue in speech recognizer. You can also type or use manual verification."
                            SpeechRecognizer.ERROR_AUDIO -> "Audio recording error."
                            SpeechRecognizer.ERROR_INSUFFICIENT_PERMISSIONS -> "Microphone permission required."
                            else -> "Could not hear clearly. Tap mic to retry."
                        }
                        _speechState.value = SpeechState.Error(message)
                    }

                    override fun onResults(results: Bundle?) {
                        val matches = results?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
                        val spoken = matches?.firstOrNull() ?: ""
                        _partialText.value = spoken
                        _speechState.value = SpeechState.Success(
                            spokenText = spoken,
                            accuracyPercentage = 0,
                            isMatch = false
                        )
                    }

                    override fun onPartialResults(partialResults: Bundle?) {
                        val matches = partialResults?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
                        _partialText.value = matches?.firstOrNull() ?: ""
                    }

                    override fun onEvent(eventType: Int, params: Bundle?) {}
                })
            }
        }
    }

    fun startListening(targetPhrase: String, languageCode: String = "en") {
        if (speechRecognizer == null) {
            initRecognizer()
        }

        _speechState.value = SpeechState.Listening
        _partialText.value = ""

        val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
            putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
            val localeStr = when (languageCode.lowercase()) {
                "en" -> "en-US"
                "bn" -> "bn-BD"
                "hi" -> "hi-IN"
                "ar" -> "ar-SA"
                "es" -> "es-ES"
                "fr" -> "fr-FR"
                "de" -> "de-DE"
                "ja" -> "ja-JP"
                "ko" -> "ko-KR"
                "zh" -> "zh-CN"
                "it" -> "it-IT"
                "ru" -> "ru-RU"
                "tr" -> "tr-TR"
                "pt" -> "pt-PT"
                else -> Locale.getDefault().toLanguageTag()
            }
            putExtra(RecognizerIntent.EXTRA_LANGUAGE, localeStr)
            putExtra(RecognizerIntent.EXTRA_PARTIAL_RESULTS, true)
            putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 3)
        }

        try {
            speechRecognizer?.startListening(intent)
        } catch (e: Exception) {
            _speechState.value = SpeechState.Error("Microphone initialization error: ${e.message}")
        }
    }

    fun evaluateSpeech(spoken: String, expected: String): Int {
        val cleanSpoken = spoken.trim().lowercase().replace(Regex("[^a-zA-Z0-9\\s\u0980-\u09FF]"), "")
        val cleanExpected = expected.trim().lowercase().replace(Regex("[^a-zA-Z0-9\\s\u0980-\u09FF]"), "")

        if (cleanSpoken == cleanExpected) return 100
        if (cleanExpected.isEmpty()) return 0

        // Levenshtein distance similarity
        val distance = calculateLevenshteinDistance(cleanSpoken, cleanExpected)
        val maxLen = maxOf(cleanSpoken.length, cleanExpected.length)
        if (maxLen == 0) return 100
        val ratio = ((maxLen - distance).toDouble() / maxLen.toDouble()) * 100.0
        return ratio.toInt().coerceIn(0, 100)
    }

    private fun calculateLevenshteinDistance(s1: String, s2: String): Int {
        val dp = Array(s1.length + 1) { IntArray(s2.length + 1) }
        for (i in 0..s1.length) dp[i][0] = i
        for (j in 0..s2.length) dp[0][j] = j

        for (i in 1..s1.length) {
            for (j in 1..s2.length) {
                val cost = if (s1[i - 1] == s2[j - 1]) 0 else 1
                dp[i][j] = minOf(
                    dp[i - 1][j] + 1,      // deletion
                    dp[i][j - 1] + 1,      // insertion
                    dp[i - 1][j - 1] + cost // substitution
                )
            }
        }
        return dp[s1.length][s2.length]
    }

    fun stopListening() {
        try {
            speechRecognizer?.stopListening()
        } catch (e: Exception) {
            Log.e("SpeechManager", "stopListening error: ${e.message}")
        }
    }

    fun reset() {
        _speechState.value = SpeechState.Idle
        _partialText.value = ""
    }

    fun destroy() {
        try {
            speechRecognizer?.destroy()
            speechRecognizer = null
        } catch (e: Exception) {
            Log.e("SpeechManager", "destroy error: ${e.message}")
        }
    }
}
