package io.github.verbus.app

import android.media.AudioManager
import android.media.ToneGenerator
import io.github.verbus.domain.model.SoundEffect
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

class ProceduralSoundPlayer {
    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Default)
    private val mutex = Mutex()

    fun play(effect: SoundEffect, enabled: Boolean, volumeLevel: Int) {
        if (!enabled) return
        val toneVolume = (volumeLevel.coerceIn(1, 10) * 10).coerceIn(10, 100)

        scope.launch {
            mutex.withLock {
                val generator = try {
                    ToneGenerator(AudioManager.STREAM_MUSIC, toneVolume)
                } catch (_: RuntimeException) {
                    return@withLock
                }

                try {
                    when (effect) {
                        SoundEffect.SINGLE_TAP -> {
                            generator.startTone(ToneGenerator.TONE_PROP_BEEP, 35)
                            delay(45)
                        }

                        SoundEffect.DOUBLE_TAP -> {
                            generator.startTone(ToneGenerator.TONE_PROP_BEEP2, 50)
                            delay(60)
                            generator.startTone(ToneGenerator.TONE_PROP_BEEP2, 50)
                            delay(70)
                        }

                        SoundEffect.TOPIC_SUCCESS -> {
                            generator.startTone(ToneGenerator.TONE_PROP_ACK, 110)
                            delay(130)
                            generator.startTone(ToneGenerator.TONE_PROP_BEEP2, 80)
                            delay(100)
                        }

                        SoundEffect.TOPIC_TIMEOUT -> {
                            generator.startTone(ToneGenerator.TONE_PROP_NACK, 180)
                            delay(200)
                        }

                        SoundEffect.ROUND_SUCCESS -> {
                            generator.startTone(ToneGenerator.TONE_PROP_ACK, 130)
                            delay(150)
                            generator.startTone(ToneGenerator.TONE_PROP_BEEP2, 110)
                            delay(130)
                            generator.startTone(ToneGenerator.TONE_PROP_PROMPT, 160)
                            delay(180)
                        }

                        SoundEffect.ROUND_FAILURE -> {
                            generator.startTone(ToneGenerator.TONE_SUP_ERROR, 180)
                            delay(190)
                            generator.startTone(ToneGenerator.TONE_PROP_NACK, 180)
                            delay(200)
                        }
                    }
                } finally {
                    generator.release()
                }
            }
        }
    }
}
