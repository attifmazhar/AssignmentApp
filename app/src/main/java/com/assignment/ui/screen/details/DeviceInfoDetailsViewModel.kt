package com.assignment.ui.scan

import androidx.databinding.ObservableField
import androidx.lifecycle.ViewModel
import com.assignment.data.helper.*
import com.assignment.utils.BatteryInfo
import com.assignment.utils.DeviceInformation
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class DeviceInfoDetailsViewModel @Inject constructor() : ViewModel() {


    val title = ObservableField<String>()
    val details = ObservableField<String>()

    fun initData(infoType: Int, dInfo: DeviceInformation, batteryInfo: BatteryInfo) {

        when (infoType) {
            DEVICE_INFO_RAM -> {
                title.set("RAM")

                val totalRam = dInfo.totalRam.toInt()
                val availableRam = dInfo.availableRam.toInt()
                details.set("RAM usage : ${(totalRam - availableRam)}  / $totalRam  MB ")

            }
            DEVICE_INFO_SYSTEM -> {
                title.set("System")
                setupSystemInfo(dInfo)

            }
            DEVICE_INFO_BATTERY -> {
                title.set("Battery Info")
                batteryInfo(dInfo, batteryInfo)
            }
            DEVICE_INFO_CPU -> {
                title.set("CPU")
                setupProcessor(dInfo)
            }
            DEVICE_INFO_SCREEN -> {
                title.set("Screen")
                setupScreenInfo(dInfo)
            }
            DEVICE_INFO_STORAGE -> {
                title.set("Storage")
                storageCalculate(dInfo)
            }
        }

    }

    private fun setupSystemInfo(deviceClass: DeviceInformation) {
        val strSystemInfo = StringBuilder("")


        strSystemInfo.append("Android " + deviceClass.getOSVersion())
        strSystemInfo.append("\n")

        strSystemInfo.append("SDK Version: " + deviceClass.getSdkVersion())
        strSystemInfo.append("\n")

        strSystemInfo.append("Android OS: " + deviceClass.getOSVersion())
        strSystemInfo.append("\n")

        strSystemInfo.append("Security: "+deviceClass.securityPatchLevel)
        strSystemInfo.append("\n")

        strSystemInfo.append("Build NO: "+deviceClass.getBuildHost())
        strSystemInfo.append("\n")

        strSystemInfo.append("Baseband: "+deviceClass.getRadioVer())
        strSystemInfo.append("\n")

        strSystemInfo.append("JavaVM: "+deviceClass.systemProperty())
        strSystemInfo.append("\n")

        strSystemInfo.append("Kernel: "+ deviceClass.readKernelVersion())
        strSystemInfo.append("\n")


        strSystemInfo.append("RootAccess: "+if (deviceClass.isDeviceRooted()) {
            "Rooted"
        } else "Not Rooted")
        strSystemInfo.append("\n")

        strSystemInfo.append("SeLinux: "+deviceClass.isSeLinuxEnforcing)
        strSystemInfo.append("\n")

        details.set(strSystemInfo.toString())


    }

    private fun setupProcessor(dInfo: DeviceInformation) {
        val processorStr = StringBuilder("")
        for (i in 0 until dInfo.numOfCores) {
            val coreFreq: Int = dInfo.getFrequencyOfCore(i)
            val progressPercentage: Int = dInfo.calculatePercentage(
                coreFreq,
                dInfo.getMaxCpuFrequency(i)
            )
            processorStr.append( "${progressPercentage} Core $i ${dInfo.getFrequencyOfCore(i)} MHz \n" +
                    " ${dInfo.getMaxCpuFrequency(i)}  MHz")
        }
        details.set(processorStr.toString())
    }

    private fun setupScreenInfo(dInfo: DeviceInformation) {

        val screenStr = StringBuilder("")

        val width = java.lang.String.valueOf(dInfo.screenWidth)
        val height = java.lang.String.valueOf(dInfo.screenHeight)
        screenStr.append(width + "x" + height)

        details.set(screenStr.toString())

    }

    private fun storageCalculate(deviceInformation: DeviceInformation) {

        val totalInternalMemorySize = deviceInformation.totalInternalMemorySize
        val availableInternalMemorySize = deviceInformation.availableInternalMemorySize
        val internalPercentage = deviceInformation.calculateLongPercentage(
            totalInternalMemorySize - availableInternalMemorySize,
            totalInternalMemorySize
        )

        val avail =
            deviceInformation.humanReadableByteCountBin(totalInternalMemorySize - availableInternalMemorySize)
        val total = deviceInformation.humanReadableByteCountBin(totalInternalMemorySize)

        val internalTx = ("Internal: $avail / $total ($internalPercentage%)")


        val totalExternalMemorySize = deviceInformation.totalExternalMemorySize
        val availableExternalMemorySize = deviceInformation.availableExternalMemorySize
        val externalPercentage = deviceInformation.calculateLongPercentage(
            totalExternalMemorySize - availableExternalMemorySize,
            totalExternalMemorySize
        )

        val exAvail =
            deviceInformation.humanReadableByteCountBin(totalExternalMemorySize - availableExternalMemorySize)
        val exTotal = deviceInformation.humanReadableByteCountBin(totalExternalMemorySize)
        val externalTxt = ("External: $exAvail / $exTotal ($externalPercentage%)")

        details.set("${internalTx} \n ${externalTxt} ")

    }

    private fun batteryInfo(deviceInformation: DeviceInformation, batteryInfo: BatteryInfo) {

        val health = "Health ${deviceInformation.getBatteryHealth()}"
        val percentage = "Percentage ${deviceInformation.getBatteryPercent()} %"

        val connectedState = if (batteryInfo.isCharging) {
            "Connected"
        } else {
            "Disconnected"
        }

        val chargingState = if (batteryInfo.isCharging) {
            "Charging"
        } else {
            "Discharging"
        }

        val temperatur = "Temperatur ${deviceInformation.getBatteryTemperature()} °C"
        val technology = "Technology ${deviceInformation.getBatteryTechnology()}"
        val voltage = "Voltage ${deviceInformation.getBatteryVoltage()} mV"

        details.set("$health \n $percentage \n $connectedState \n $chargingState \n $temperatur \n $chargingState \n $technology \n $voltage")

    }

}
