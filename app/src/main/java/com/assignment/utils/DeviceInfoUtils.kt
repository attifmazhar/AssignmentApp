package com.assignment.utils

class DeviceInfoUtils constructor(val dInfo: DeviceInformation, val batteryInfo: BatteryInfo) {


    fun getAllDeviceInfo() : String {
        val strSystemInfo = StringBuilder("")

        strSystemInfo.append(getAllSystemInfo())
        strSystemInfo.append(getProcessorInfo())
        strSystemInfo.append(getScreenInfo())
        strSystemInfo.append(getStorageInfo())
        strSystemInfo.append(getBatteryInfo())
        return strSystemInfo.toString()
    }


    fun getAllSystemInfo(): String {
        val strSystemInfo = StringBuilder("")

        strSystemInfo.append("Android " + dInfo.getOSVersion())
        strSystemInfo.append("\n")

        strSystemInfo.append("SDK Version: " + dInfo.getSdkVersion())
        strSystemInfo.append("\n")

        strSystemInfo.append("Android OS: " + dInfo.getOSVersion())
        strSystemInfo.append("\n")

        strSystemInfo.append("Security: "+dInfo.securityPatchLevel)
        strSystemInfo.append("\n")

        strSystemInfo.append("Build NO: "+dInfo.getBuildHost())
        strSystemInfo.append("\n")

        strSystemInfo.append("Baseband: "+dInfo.getRadioVer())
        strSystemInfo.append("\n")

        strSystemInfo.append("JavaVM: "+dInfo.systemProperty())
        strSystemInfo.append("\n")

        strSystemInfo.append("Kernel: "+ dInfo.readKernelVersion())
        strSystemInfo.append("\n")


        strSystemInfo.append("RootAccess: "+if (dInfo.isDeviceRooted()) {
            "Rooted"
        } else "Not Rooted")
        strSystemInfo.append("\n")

        strSystemInfo.append("SeLinux: "+dInfo.isSeLinuxEnforcing)
        strSystemInfo.append("\n")

        return strSystemInfo.toString()


    }

    fun getProcessorInfo() : String{
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
        return (processorStr.toString())
    }

    fun getScreenInfo(): String {

        val screenStr = StringBuilder("")

        val width = java.lang.String.valueOf(dInfo.screenWidth)
        val height = java.lang.String.valueOf(dInfo.screenHeight)
        screenStr.append(width + "x" + height)

        return screenStr.toString()

    }

    fun getStorageInfo(): String {

        val totalInternalMemorySize = dInfo.totalInternalMemorySize
        val availableInternalMemorySize = dInfo.availableInternalMemorySize
        val internalPercentage = dInfo.calculateLongPercentage(
            totalInternalMemorySize - availableInternalMemorySize,
            totalInternalMemorySize
        )

        val avail =
            dInfo.humanReadableByteCountBin(totalInternalMemorySize - availableInternalMemorySize)
        val total = dInfo.humanReadableByteCountBin(totalInternalMemorySize)

        val internalTx = ("Internal: $avail / $total ($internalPercentage%)")


        val totalExternalMemorySize = dInfo.totalExternalMemorySize
        val availableExternalMemorySize = dInfo.availableExternalMemorySize
        val externalPercentage = dInfo.calculateLongPercentage(
            totalExternalMemorySize - availableExternalMemorySize,
            totalExternalMemorySize
        )

        val exAvail =
            dInfo.humanReadableByteCountBin(totalExternalMemorySize - availableExternalMemorySize)
        val exTotal = dInfo.humanReadableByteCountBin(totalExternalMemorySize)
        val externalTxt = ("External: $exAvail / $exTotal ($externalPercentage%)")

        return ("${internalTx} \n ${externalTxt} ")

    }

    fun getBatteryInfo() : String {

        val health = "Health ${dInfo.getBatteryHealth()}"
        val percentage = "Percentage ${dInfo.getBatteryPercent()} %"

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

        val temperatur = "Temperatur ${dInfo.getBatteryTemperature()} °C"
        val technology = "Technology ${dInfo.getBatteryTechnology()}"
        val voltage = "Voltage ${dInfo.getBatteryVoltage()} mV"

        return ("$health \n $percentage \n $connectedState \n $chargingState \n $temperatur \n $chargingState \n $technology \n $voltage")

    }
}