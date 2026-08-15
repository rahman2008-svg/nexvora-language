package com.example.audio

import android.content.Context
import android.speech.tts.TextToSpeech
import android.util.Log
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import java.util.Locale

class TTSManager(context: Context) : TextToSpeech.OnInitListener {
    private var tts: TextToSpeech? = TextToSpeech(context.applicationContext, this)
    private var isInitialized = false

    private val _isSpeaking = MutableStateFlow(false)
    val isSpeaking: StateFlow<Boolean> = _isSpeaking

    private val _speechRate = MutableStateFlow(0.9f) // Slightly slower for language learners
    val speechRate: StateFlow<Float> = _speechRate

    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS) {
            isInitialized = true
            tts?.setSpeechRate(_speechRate.value)
            Log.d("TTSManager", "TextToSpeech initialized successfully")
        } else {
            Log.e("TTSManager", "TextToSpeech initialization failed with status $status")
        }
    }

    fun speak(text: String, languageCode: String = "en") {
        if (!isInitialized || tts == null) {
            Log.w("TTSManager", "TTS not initialized yet")
            return
        }

        val locale = when (languageCode.lowercase()) {
            "en" -> Locale.US
            "bn" -> Locale("bn", "BD")
            "hi" -> Locale("hi", "IN")
            "ar" -> Locale("ar", "SA")
            "es" -> Locale("es", "ES")
            "fr" -> Locale.FRENCH
            "de" -> Locale.GERMAN
            "ja" -> Locale.JAPANESE
            "ko" -> Locale.KOREAN
            "zh" -> Locale.CHINESE
            "it" -> Locale.ITALIAN
            "ru" -> Locale("ru", "RU")
            "tr" -> Locale("tr", "TR")
            "pt" -> Locale("pt", "PT")
            else -> Locale.US
        }

        try {
            val result = tts?.setLanguage(locale)
            if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                tts?.setLanguage(Locale.US)
            }
            tts?.speak(text, TextToSpeech.QUEUE_FLUSH, null, "TTS_UTTERANCE_ID")
        } catch (e: Exception) {
            Log.e("TTSManager", "Error in speak: ${e.message}")
        }
    }

    fun setSpeechRate(rate: Float) {
        _speechRate.value = rate
        tts?.setSpeechRate(rate)
    }

    fun stop() {
        tts?.stop()
        _isSpeaking.value = false
    }

    fun shutdown() {
        tts?.stop()
        tts?.shutdown()
        tts = null
    }
}
