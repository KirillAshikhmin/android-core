package com.digexco.app.tech

import android.content.Context
import android.media.AudioAttributes
import android.media.SoundPool
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import com.digexco.app.tech.sounds.R


object EffectsPlayer {

    private var vibrator: Vibrator? = null
    var soundPool: SoundPool

    var patternSuccess = longArrayOf(500, 100, 50, 20)
    var patternError = longArrayOf(300)
    var patternFinished = longArrayOf(500, 60, 250, 60, 250, 80)


    init {
        val attr = AudioAttributes.Builder()
            .setUsage(AudioAttributes.USAGE_NOTIFICATION)
            .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
            .build()

        val pl = SoundPool.Builder().setAudioAttributes(attr).setMaxStreams(2).build()
        pl.setOnLoadCompleteListener { soundPool, sampleId, _ ->
            soundPool.play(sampleId, 1f, 1f, 0, 0, 1f)
        }
        soundPool = pl
    }

    fun setVibrator(vibrator: Vibrator) {
        this.vibrator = vibrator
    }

    var soundEnabled: Boolean = true
    var vibrationEnabled: Boolean = true

    fun playError(context: Context) {
        if (soundEnabled) soundPool.load(context, R.raw.error, 0)
        if (vibrationEnabled) vibrate(patternError, heavy = true)
    }

    fun playSuccess(context: Context) {
        if (soundEnabled) soundPool.load(context, R.raw.correct, 0)
        if (vibrationEnabled) vibrate(patternSuccess)
    }

    fun playFinished(context: Context) {
        if (soundEnabled) soundPool.load(context, R.raw.finished, 0)
        if (vibrationEnabled) vibrate(patternFinished)
    }

    private fun vibrate(pattern: LongArray, heavy: Boolean = false) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val effect =
                if (heavy && Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q)
                    VibrationEffect.EFFECT_HEAVY_CLICK
                else
                    VibrationEffect.DEFAULT_AMPLITUDE

            vibrator?.vibrate(
                if (pattern.size == 1) VibrationEffect.createOneShot(pattern.first(), effect)
                else
                    VibrationEffect.createWaveform(pattern, effect)
            )
        } else {
            if (pattern.size == 1)
                vibrator?.vibrate(pattern.first())
            else
                vibrator?.vibrate(pattern, -1)
        }
    }
}
