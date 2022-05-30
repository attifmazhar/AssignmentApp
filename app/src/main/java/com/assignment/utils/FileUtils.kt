package com.assignment.utils

import android.content.Context
import android.util.Log
import java.io.*


object FileUtils {

    fun writeStringAsFile(context: Context, fileContents: String?, fileName: String?) {
        try {
            val out = FileWriter(File(context.getFilesDir(), fileName))
            out.write(fileContents)
            out.close()
        } catch (e: IOException) {
            Log.e("writeStringAsFile", e.message!!)
        }
    }

    fun readFileAsString(context: Context, fileName: String?): String? {
        val stringBuilder = StringBuilder()
        var line: String?
        var `in`: BufferedReader? = null
        try {
            `in` = BufferedReader(FileReader(File(context.getFilesDir(), fileName)))
            while (`in`.readLine().also { line = it } != null) stringBuilder.append(line)
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return stringBuilder.toString()
    }
}