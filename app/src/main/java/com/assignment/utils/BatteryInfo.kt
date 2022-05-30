package com.assignment.utils

import android.content.Context
import com.an.deviceinfo.device.model.Battery

class BatteryInfo(private var context: Context?) {
    private val battery: Battery

    /**
     * Returns the battery percentage;
     */
    fun batteryPercentage(): Int {
        return battery.batteryPercent
    }

    /**
     * Returns state of the battery;
     */
    val isCharging: Boolean
        get() = battery.isPhoneCharging

    init {
        battery = Battery(context)
    }
}