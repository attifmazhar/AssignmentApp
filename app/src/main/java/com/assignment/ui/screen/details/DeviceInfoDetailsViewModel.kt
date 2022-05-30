package com.assignment.ui.scan

import androidx.databinding.ObservableField
import androidx.lifecycle.ViewModel
import com.assignment.data.helper.*
import com.assignment.utils.DeviceInfoUtils
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class DeviceInfoDetailsViewModel @Inject constructor() : ViewModel() {


    val title = ObservableField<String>()
    val details = ObservableField<String>()

    fun initData(infoType: Int, dInfo: DeviceInfoUtils) {

        when (infoType) {
            DEVICE_INFO_RAM -> {
                title.set("RAM")
                val totalRam = dInfo.dInfo.totalRam.toInt()
                val availableRam = dInfo.dInfo.availableRam.toInt()
                details.set("RAM usage : ${(totalRam - availableRam)}  / $totalRam  MB ")

            }
            DEVICE_INFO_SYSTEM -> {
                title.set("System")
                details.set(dInfo.getAllSystemInfo())

            }
            DEVICE_INFO_BATTERY -> {
                title.set("Battery Info")
                details.set(dInfo.getBatteryInfo())
            }
            DEVICE_INFO_CPU -> {
                title.set("CPU")
                details.set(dInfo.getProcessorInfo())
            }
            DEVICE_INFO_SCREEN -> {
                title.set("Screen")
                details.set(dInfo.getScreenInfo())
            }
            DEVICE_INFO_STORAGE -> {
                title.set("Storage")
                details.set(dInfo.getStorageInfo())
            }
        }
    }

}
