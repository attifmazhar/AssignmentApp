package com.assignment.utils

import com.an.deviceinfo.device.DeviceInfo
import android.app.Activity
import android.app.ActivityManager
import android.content.Context
import android.hardware.SensorManager
import android.content.Intent
import android.content.pm.ResolveInfo
import android.graphics.Point
import android.hardware.Sensor
import android.hardware.usb.UsbManager
import android.hardware.usb.UsbDevice
import com.assignment.utils.DeviceInformation
import android.os.StatFs
import android.os.Environment
import android.util.DisplayMetrics
import android.util.Log
import android.view.Display
import android.view.WindowManager
import com.an.deviceinfo.device.model.Memory
import java.io.*
import java.lang.Exception
import java.text.CharacterIterator
import java.text.StringCharacterIterator
import java.util.*
import java.util.concurrent.TimeUnit
import java.util.regex.Pattern

class DeviceInformation : DeviceInfo {
    lateinit var context: Context
    lateinit var memory: Memory
    private var activity: Activity? = null

    constructor(context: Context?) : super(context) {
        this.context = context!!
        memory = Memory(context)
    }
    constructor(context: Context?, activity: Activity?) : super(context) {
        this.context = context!!
        this.activity = activity
        memory = Memory(context)
    }

    /**
     * Returns the size of internal storage in bytes;
     *
     * @return {long}
     */
    fun totalExternalMemory(): Long {
        return bytesToMB(memory.totalExternalMemorySize)
    }

    /**
     * Returns the size of internal storage in bytes;
     *
     * @return {long}
     */
    fun availableExternalMemory(): Long {
        return bytesToMB(memory.availableExternalMemorySize)
    }

    /**
     * Converts bytes to mega-bytes
     *
     * @return {long}
     */
    fun bytesToMB(B: Long): Long {
        val MB = 1024L * 1024L
        return B / MB
    }

    val totalRam: Long
        get() = bytesToMB(memory.totalRAM)
    val availableRam: Long
        get() {
            val mi = ActivityManager.MemoryInfo()
            val activityManager =
                activity!!.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
            activityManager.getMemoryInfo(mi)
            return mi.availMem / 1048576L
        }

    /**
     * Calculate the percentage to put in progressbar
     *
     * @return {Integer}
     */
    fun calculatePercentage(toCalculate: Int, maximum: Int): Int {
        return if (maximum != 0) {
            100 * toCalculate / maximum
        } else 30
    }

    fun calculateLongPercentage(toCalculate: Long, maximum: Long): Long {
        return if (maximum != 0L) {
            100 * toCalculate / maximum
        } else 30
    }

    /**
     * returns the number of cpu cores
     *
     * @return integer;
     */
    val numOfCores: Int
        get() = Objects.requireNonNull(File("/sys/devices/system/cpu/").listFiles { params ->
            Pattern.matches(
                "cpu[0-9]",
                params.name
            )
        }).size

    /**
     * Returns frequency of custom number of cores;
     *
     * @param coreNo
     * @return
     */
    fun getFrequencyOfCore(coreNo: Int): Int {
        var currentFReq = 0
        try {
            val currentFreq: Double
            val readerCurFreq: RandomAccessFile
            readerCurFreq =
                RandomAccessFile("/sys/devices/system/cpu/cpu$coreNo/cpufreq/scaling_cur_freq", "r")
            val curfreg = readerCurFreq.readLine()
            currentFreq = curfreg.toDouble() / 1000
            readerCurFreq.close()
            currentFReq = currentFreq.toInt()
            println("$currentFReq--------")
        } catch (ex: Exception) {
            ex.printStackTrace()
        }
        return currentFReq
    }

    /**
     * Maximum frequency of the cpu (used to calculate the percentage for frequency bars);
     *
     * @return
     */
    fun getMaxCpuFrequency(core: Int): Int {
        var currentFReq = 0
        try {
            val currentFreq: Double
            val readerCurFreq: RandomAccessFile
            readerCurFreq =
                RandomAccessFile("/sys/devices/system/cpu/cpu$core/cpufreq/cpuinfo_max_freq", "r")
            val curfreg = readerCurFreq.readLine()
            currentFreq = curfreg.toDouble() / 1000
            readerCurFreq.close()
            currentFReq = currentFreq.toInt()
        } catch (ex: Exception) {
            ex.printStackTrace()
        }
        return currentFReq
    }

    /**
     * Returns total number of available sensors in app;
     */
    val numberOfSensors: Int
        get() {
            val sensorManager = context.getSystemService(Context.SENSOR_SERVICE) as SensorManager
            val sensor = sensorManager.getSensorList(Sensor.TYPE_ALL)
            return sensor.size
        }

    /**
     * Returns the total number of apps in your mobile phone;
     */
    val numberOfApps: Int
        get() {
            val mainIntent = Intent(Intent.ACTION_MAIN, null)
            mainIntent.addCategory(Intent.CATEGORY_LAUNCHER)
            val pkgAppsList = context.packageManager.queryIntentActivities(mainIntent, 0)
            return pkgAppsList.size
        }

    /**
     * Returns security patch level of the device;
     *
     * @return
     */
    val securityPatchLevel: String
        get() {
            var str = ""
            try {
                val process = ProcessBuilder()
                    .command("/system/bin/getprop")
                    .redirectErrorStream(true)
                    .start()
                val `is` = process.inputStream
                val br = BufferedReader(InputStreamReader(`is`))
                var line: String
                while (br.readLine().also { line = it } != null) {
                    str += """
                    $line
                    
                    """.trimIndent()
                    if (str.contains("security_patch")) {
                        val splitted = line.split(":").toTypedArray()
                        if (splitted.size == 2) {
                            return splitted[1]
                        }
                        break
                    }
                }
                br.close()
                process.destroy()
            } catch (e: IOException) {
                e.printStackTrace()
            }
            return str
        }

    /**
     * Checks if the device supports USB Host;
     *
     * @param context
     * @return
     */
    fun checkInfo(context: Context): Boolean {
        val mUsbManager = context.getSystemService(Context.USB_SERVICE) as UsbManager
        val devlist = mUsbManager.deviceList
        return !devlist.isEmpty()
    }

    /**
     * Returns if the device is using dalvik or any other jvm and its version;
     *
     * @return
     */
    fun systemProperty(): String {
        return System.getProperty("java.vm.name") + " " + System.getProperty("java.vm.version")
    }

    /**
     * Returns the kernel version of the device;
     *
     * @return
     */
    fun readKernelVersion(): String {
        return System.getProperty("os.version")
    }

    /**
     * Gets the openGl version
     *
     * @param context
     * @return
     */
    fun openGlVersion(context: Context): String {
        val activityManager = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        val configurationInfo = activityManager.deviceConfigurationInfo
        return configurationInfo.glEsVersion
    }// If getenforce is modified on this device, assume the device is not enforcing// If getenforce is not available to the device, assume the device is not enforcing

    /**
     * Check for SELinux;
     *
     * @return
     */
    val isSeLinuxEnforcing: String
        get() {
            val output = StringBuffer()
            val p: Process
            val TAG = "SELINUX"
            try {
                p = Runtime.getRuntime().exec("getenforce")
                p.waitFor()
                val reader = BufferedReader(InputStreamReader(p.inputStream))
                var line: String? = ""
                while (reader.readLine().also { line = it } != null) {
                    output.append(line)
                }
            } catch (e: Exception) {
                Log.e(TAG, "OS does not support getenforce")
                // If getenforce is not available to the device, assume the device is not enforcing
                e.printStackTrace()
                return "Isn't Available"
            }
            val response = output.toString()
            return if ("Enforcing" == response) {
                "Available"
            } else if ("Permissive" == response) {
                "Unavailable"
            } else {
                Log.e(TAG, "getenforce returned unexpected value, unable to determine selinux!")
                // If getenforce is modified on this device, assume the device is not enforcing
                "Unable to determine"
            }
        }

    fun formatTime(millis: Long): String {
        var seconds = Math.round(millis.toDouble() / 1000)
        val hours = TimeUnit.SECONDS.toHours(seconds)
        if (hours > 0) seconds -= TimeUnit.HOURS.toSeconds(hours)
        val minutes = if (seconds > 0) TimeUnit.SECONDS.toMinutes(seconds) else 0
        if (minutes > 0) seconds -= TimeUnit.MINUTES.toSeconds(minutes)
        return if (hours > 0) String.format(
            "%02d:%02d:%02d",
            hours,
            minutes,
            seconds
        ) else String.format("%02d:%02d", minutes, seconds)
    }

    /**
     * Checks if treble is supported;
     *
     * @param context
     * @return
     */
    fun getTreble(context: Context?): String {
        val output = getSystemProperty("ro.treble.enabled")
        return if (output == "true") {
            "Supported"
        } else {
            "Not Supported"
        }
    }

    fun getTotalStorageInfo(path: String?): Long {
        val statFs = StatFs(path)
        val t: Long
        t = statFs.totalBytes
        return t // remember to convert in GB,MB or KB.
    }

    fun getUsedStorageInfo(path: String?): Long {
        val statFs = StatFs(path)
        val u: Long
        u = statFs.totalBytes - statFs.availableBytes
        return u // remember to convert in GB,MB or KB.
    }

    val totalOsStorage: Long
        get() = getTotalStorageInfo(Environment.getRootDirectory().path)
    val usedOsStorage: Long
        get() = getUsedStorageInfo(Environment.getRootDirectory().path)

    fun humanReadableByteCountBin(bytes: Long): String {
        val absB = if (bytes == Long.MIN_VALUE) Long.MAX_VALUE else Math.abs(bytes)
        if (absB < 1024) {
            return "$bytes B"
        }
        var value = absB
        val ci: CharacterIterator = StringCharacterIterator("KMGTPE")
        var i = 40
        while (i >= 0 && absB > 0xfffccccccccccccL shr i) {
            value = value shr 10
            ci.next()
            i -= 10
        }
        value *= java.lang.Long.signum(bytes).toLong()
        return String.format("%.1f %ciB", value / 1024.0, ci.current())
    }

    /**
     * Gets Battery capacity in mAh
     * @param context
     * @return
     */
    fun getBatteryCapacity(context: Context?): Double {
        val mPowerProfile: Any
        var batteryCapacity = 0.0
        val POWER_PROFILE_CLASS = "com.android.internal.os.PowerProfile"
        try {
            mPowerProfile = Class.forName(POWER_PROFILE_CLASS)
                .getConstructor(Context::class.java)
                .newInstance(context)
            batteryCapacity = Class
                .forName(POWER_PROFILE_CLASS)
                .getMethod("getBatteryCapacity")
                .invoke(mPowerProfile) as Double
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return batteryCapacity
    }

    /**
     * Returns dpi of the current screen the app is shown.
     * @return
     */
    val dPI: String
        get() {
            val metrics = activity!!.resources.displayMetrics
            val densityDPI = (metrics.density * 160f).toInt()
            return "$densityDPI DPI"
        }

    /**
     * Calculates the screen size on inches;
     * @return
     */
    val screenSize: String
        get() {
            var widthPixels = 0
            var heightPixels = 0
            val windowManager = activity!!.windowManager
            val display = windowManager.defaultDisplay
            val displayMetrics = DisplayMetrics()
            display.getMetrics(displayMetrics)
            try {
                val realSize = Point()
                Display::class.java.getMethod("getRealSize", Point::class.java)
                    .invoke(display, realSize)
                widthPixels = realSize.x
                heightPixels = realSize.y
            } catch (e: Exception) {
                e.printStackTrace()
            }
            val x = Math.pow((widthPixels / displayMetrics.xdpi).toDouble(), 2.0)
            val y = Math.pow((heightPixels / displayMetrics.ydpi).toDouble(), 2.0)
            val screenSize = Math.sqrt(x + y)
            return String.format(Locale.ENGLISH, "%.2f", screenSize) + "\""
        }

    companion object {
        /**
         * @see FeatureInfo.getGlEsVersion
         */
        private fun getMajorVersion(glEsVersion: Int): Int {
            return glEsVersion and -0x10000 shr 16
        }

        /**
         * Get Values from android properties
         *
         * @param key
         * @return
         */
        fun getSystemProperty(key: String?): String? {
            var value: String? = null
            try {
                value = Class.forName("android.os.SystemProperties")
                    .getMethod("get", String::class.java).invoke(null, key) as String
            } catch (e: Exception) {
                e.printStackTrace()
            }
            return value
        }

        /**
         * Returns the refresh rate of the device;
         * @param context
         * @return
         */
        fun getRefreshValue(context: Activity): String {
            val display =
                (context.getSystemService(Context.WINDOW_SERVICE) as WindowManager).defaultDisplay
            val refreshValue = display.refreshRate
            return String.format(Locale.ENGLISH, "%.2f", refreshValue) + "Hz"
        }
    }
}