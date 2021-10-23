package com.summer.locationtracker.data.local_user_storage

import android.content.Context

object LocalUserDataStorage {
    @JvmStatic
    fun getLocalUserData(context: Context): PreferenceModel {
        val sharedPreferences = context.getSharedPreferences("local_user", Context.MODE_PRIVATE)
        return PreferenceModel(
            sharedPreferences.getLong("phone_number", 0L),
            sharedPreferences.getString("first_name", null),
            sharedPreferences.getString("last_name", null)
        )
    }

    @JvmStatic
    fun setLocalUserData(context: Context, userData:PreferenceModel){
        val sharedPreferences = context.getSharedPreferences("local_user", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putLong("phone_number",userData.phoneNumber)
        editor.putString("first_name",userData.firstName)
        editor.putString("last_name",userData.lastName)
        editor.apply()
    }


}