package com.summer.locationtracker

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager


class App : Application() {
    override fun onCreate() {
        super.onCreate()
        createNotificationChannel()
    }

    private fun createNotificationChannel() {
        val locationChannel =
            NotificationChannel(
                locationServiceChannel,
                locationServiceChannel,
                NotificationManager.IMPORTANCE_LOW
            )
        locationChannel.description = "Location Update"
        val manager = getSystemService(NotificationManager::class.java)
        manager.createNotificationChannel(locationChannel)
    }

    companion object {
        const val locationServiceChannel = "LocationServiceChannel"
    }
}