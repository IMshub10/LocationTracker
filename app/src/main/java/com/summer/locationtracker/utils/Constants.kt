package com.summer.locationtracker.utils

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.Configuration
import java.text.SimpleDateFormat
import java.util.*
import java.util.regex.Pattern

object Constants {

    val countryCodePattern: Pattern = Pattern.compile("^\\+?[0-9]{2,3}")
    val phoneNumberPattern: Pattern = Pattern.compile("^[6-9][0-9]{9}\$")

    @SuppressLint("ConstantLocale")
    val dateFormat = SimpleDateFormat("dd-MM-yyyy HH:mm:ss", Locale.getDefault())

    const val EXTERNAL_STORAGE_FOLDER_NAME = "Location Tracker"
    const val EXTERNAL_STORAGE_FILE_NAME = "Prev Locations.txt"


    const val ALARM_TRIGGER = 5 * 60 * 1000L
    const val ALARM_PENDING_INTENT_REQUEST_CODE = 100

    const val REQUEST_CHECK_SETTINGS = 10001

    fun getCurrentUiMode(context: Context): Boolean {
        val currentNightMode = (context.resources.configuration.uiMode
                and Configuration.UI_MODE_NIGHT_MASK)
        if (currentNightMode == Configuration.UI_MODE_NIGHT_YES) {
            return true
        }
        return false
    }
}