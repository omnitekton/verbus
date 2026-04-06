package io.github.verbus.app

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorManager

class ShakeSupportChecker(
    private val context: Context,
) {
    fun isShakeSupported(): Boolean {
        val sensorManager = context.getSystemService(Context.SENSOR_SERVICE) as? SensorManager ?: return false
        return sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER) != null
    }
}
