package com.assignment.data.helper

import androidx.annotation.IntDef

@Retention(AnnotationRetention.RUNTIME)
@IntDef(DEVICE_INFO_RAM, DEVICE_INFO_SYSTEM, DEVICE_INFO_STORAGE, DEVICE_INFO_CPU, DEVICE_INFO_BATTERY, DEVICE_INFO_SCREEN, DEVICE_INFO_CAMERA)
annotation class InfoState()
