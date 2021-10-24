package com.summer.locationtracker.data.local_user_storage

import android.content.Context

object LocalUserDataStorage {
    @JvmStatic
    fun getLocalUserData(context: Context): PreferenceModel {
        val sharedPreferences = context.getSharedPreferences("local_user", Context.MODE_PRIVATE)
        return PreferenceModel(
            sharedPreferences.getString("country_code", null),
            sharedPreferences.getLong("phone_number", 0L),
            sharedPreferences.getString("first_name", null),
            sharedPreferences.getString("last_name", null),
            sharedPreferences.getString("last_known_latitude", "0")!!,
            sharedPreferences.getString("last_known_longitude", "0")!!
        )
    }

    @JvmStatic
    fun setLocalUserData(context: Context, userData: PreferenceModel) {
        val sharedPreferences = context.getSharedPreferences("local_user", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString("country_code", userData.countryCode)
        editor.putLong("phone_number", userData.phoneNumber)
        editor.putString("first_name", userData.firstName)
        editor.putString("last_name", userData.lastName)
        editor.putString("last_known_latitude", userData.lastKnowLatitude)
        editor.putString("last_known_longitude", userData.lastKnowLongitude)
        editor.apply()
    }


    @JvmStatic
    fun setUsersLocationData(context: Context, latitude: String, longitude: String) {
        val sharedPreferences = context.getSharedPreferences("local_user", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString("last_known_latitude", latitude)
        editor.putString("last_known_longitude", longitude)
        editor.apply()
    }
}