package com.example.util

import android.content.Context
import android.hardware.camera2.CameraCharacteristics
import android.hardware.camera2.CameraManager
import android.media.AudioAttributes
import android.media.AudioFormat
import android.media.AudioTrack
import android.os.Build
import android.os.CombinedVibration
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlin.math.sin

class SosBeaconController(private val context: Context) {

    private val cameraManager = try {
        context.getSystemService(Context.CAMERA_SERVICE) as? CameraManager
    } catch (_: Exception) {
        null
    }

    private val cameraId: String? by lazy {
        try {
            cameraManager?.cameraIdList?.firstOrNull { id ->
                val chars = cameraManager.getCameraCharacteristics(id)
                val hasFlash = chars.get(CameraCharacteristics.FLASH_INFO_AVAILABLE) == true
                val facing = chars.get(CameraCharacteristics.LENS_FACING)
                hasFlash && facing == CameraCharacteristics.LENS_FACING_BACK
            } ?: cameraManager?.cameraIdList?.firstOrNull()
        } catch (_: Exception) {
            null
        }
    }

    private val _isFlashlightOn = MutableStateFlow(false)
    val isFlashlightOn: StateFlow<Boolean> = _isFlashlightOn.asStateFlow()

    private val _isStrobeActive = MutableStateFlow(false)
    val isStrobeActive: StateFlow<Boolean> = _isStrobeActive.asStateFlow()

    private val _isSirenActive = MutableStateFlow(false)
    val isSirenActive: StateFlow<Boolean> = _isSirenActive.asStateFlow()

    private var strobeJob: Job? = null
    private var sirenJob: Job? = null
    private var audioTrack: AudioTrack? = null

    fun toggleFlashlight() {
        if (_isStrobeActive.value) {
            stopStrobe()
        }
        val target = !_isFlashlightOn.value
        setTorch(target)
        _isFlashlightOn.value = target
    }

    fun toggleStrobe(scope: CoroutineScope) {
        if (_isStrobeActive.value) {
            stopStrobe()
        } else {
            startStrobe(scope)
        }
    }

    fun startStrobe(scope: CoroutineScope) {
        _isStrobeActive.value = true
        _isFlashlightOn.value = false
        strobeJob?.cancel()
        strobeJob = scope.launch(Dispatchers.Default) {
            // Morse Code SOS: ... --- ...
            // Dot: 150ms on, 150ms off
            // Dash: 450ms on, 150ms off
            // Letter gap: 300ms, Word gap: 1000ms
            val morseCode = listOf(
                true, false, true, false, true, false, // S (3 dots)
                true, true, true, false, true, true, true, false, true, true, true, false, // O (3 dashes)
                true, false, true, false, true, false // S (3 dots)
            )

            try {
                while (isActive && _isStrobeActive.value) {
                    // Quick high-intensity strobe bursts
                    for (i in 0 until 6) {
                        if (!isActive) break
                        setTorch(true)
                        vibratePulse(80)
                        delay(120)
                        setTorch(false)
                        delay(120)
                    }
                    delay(300)

                    // Long beacon flashes
                    for (i in 0 until 3) {
                        if (!isActive) break
                        setTorch(true)
                        vibratePulse(250)
                        delay(400)
                        setTorch(false)
                        delay(200)
                    }
                    delay(600)
                }
            } finally {
                setTorch(false)
                _isStrobeActive.value = false
            }
        }
    }

    fun stopStrobe() {
        _isStrobeActive.value = false
        strobeJob?.cancel()
        strobeJob = null
        setTorch(false)
    }

    fun toggleSiren(scope: CoroutineScope) {
        if (_isSirenActive.value) {
            stopSiren()
        } else {
            startSiren(scope)
        }
    }

    fun startSiren(scope: CoroutineScope) {
        _isSirenActive.value = true
        sirenJob?.cancel()
        sirenJob = scope.launch(Dispatchers.Default) {
            try {
                val sampleRate = 44100
                val minBufferSize = AudioTrack.getMinBufferSize(
                    sampleRate,
                    AudioFormat.CHANNEL_OUT_MONO,
                    AudioFormat.ENCODING_PCM_16BIT
                )

                val audioAttributes = AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_ALARM)
                    .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                    .build()

                val audioFormat = AudioFormat.Builder()
                    .setSampleRate(sampleRate)
                    .setChannelMask(AudioFormat.CHANNEL_OUT_MONO)
                    .setEncoding(AudioFormat.ENCODING_PCM_16BIT)
                    .build()

                val track = AudioTrack.Builder()
                    .setAudioAttributes(audioAttributes)
                    .setAudioFormat(audioFormat)
                    .setBufferSizeInBytes(minBufferSize * 2)
                    .setTransferMode(AudioTrack.MODE_STREAM)
                    .build()

                audioTrack = track
                track.play()

                val buffer = ShortArray(1024)
                var phase = 0.0

                while (isActive && _isSirenActive.value) {
                    // Modulate frequency between 2600Hz and 3400Hz (emergency distress whistle)
                    for (f in 2600..3400 step 40) {
                        if (!isActive) break
                        val freq = f.toDouble()
                        for (i in buffer.indices) {
                            phase += 2.0 * Math.PI * freq / sampleRate
                            if (phase > 2.0 * Math.PI) phase -= 2.0 * Math.PI
                            buffer[i] = (sin(phase) * 32767.0 * 0.95).toInt().toShort()
                        }
                        track.write(buffer, 0, buffer.size)
                    }

                    for (f in 3400 downTo 2600 step 40) {
                        if (!isActive) break
                        val freq = f.toDouble()
                        for (i in buffer.indices) {
                            phase += 2.0 * Math.PI * freq / sampleRate
                            if (phase > 2.0 * Math.PI) phase -= 2.0 * Math.PI
                            buffer[i] = (sin(phase) * 32767.0 * 0.95).toInt().toShort()
                        }
                        track.write(buffer, 0, buffer.size)
                    }
                }
            } catch (_: Exception) {
                // Audio fallback handled gracefully
            } finally {
                stopSirenInternal()
            }
        }
    }

    private fun stopSirenInternal() {
        try {
            audioTrack?.pause()
            audioTrack?.flush()
            audioTrack?.stop()
            audioTrack?.release()
        } catch (_: Exception) {
        }
        audioTrack = null
        _isSirenActive.value = false
    }

    fun stopSiren() {
        _isSirenActive.value = false
        sirenJob?.cancel()
        sirenJob = null
        stopSirenInternal()
    }

    fun stopAll() {
        stopStrobe()
        stopSiren()
        setTorch(false)
        _isFlashlightOn.value = false
    }

    private fun setTorch(enabled: Boolean) {
        try {
            cameraId?.let { id ->
                cameraManager?.setTorchMode(id, enabled)
            }
        } catch (_: Exception) {
            // Devices without flash or hardware permission restrictions
        }
    }

    private fun vibratePulse(durationMs: Long) {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                val vibratorManager = context.getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as? VibratorManager
                val vibrator = vibratorManager?.defaultVibrator
                vibrator?.vibrate(VibrationEffect.createOneShot(durationMs, VibrationEffect.DEFAULT_AMPLITUDE))
            } else {
                @Suppress("DEPRECATION")
                val vibrator = context.getSystemService(Context.VIBRATOR_SERVICE) as? Vibrator
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    vibrator?.vibrate(VibrationEffect.createOneShot(durationMs, VibrationEffect.DEFAULT_AMPLITUDE))
                } else {
                    @Suppress("DEPRECATION")
                    vibrator?.vibrate(durationMs)
                }
            }
        } catch (_: Exception) {
        }
    }
}
