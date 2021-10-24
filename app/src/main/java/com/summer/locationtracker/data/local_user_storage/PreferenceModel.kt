package com.summer.locationtracker.data.local_user_storage

data class PreferenceModel(
    val countryCode:String?,
    val phoneNumber: Long,
    val firstName: String?,
    val lastName: String?
)