package com.summer.locationtracker.ui.activities

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.summer.locationtracker.R
import com.summer.locationtracker.data.local_user_storage.LocalUserDataStorage

@SuppressLint("CustomSplashScreen")
class SplashScreenActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)
        if (!checkUserIsValid()) {
            val activityIntent = Intent(this, AuthenticationActivity::class.java)
            startActivity(activityIntent)
        } else {
            val activityIntent = Intent(this, MapsActivity::class.java)
            startActivity(activityIntent)
        }
    }


    private fun checkUserIsValid(): Boolean {
        val loadUserData = LocalUserDataStorage.getLocalUserData(this)
        if (loadUserData.firstName != null && loadUserData.lastName != null && loadUserData.countryCode != null && loadUserData.phoneNumber != 0L) {
            return true
        }
        return false
    }

}