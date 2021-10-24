package com.summer.locationtracker.service

import android.Manifest
import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.os.IBinder
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import com.google.android.gms.location.LocationServices
import com.summer.locationtracker.R
import com.summer.locationtracker.ui.activities.MapsActivity
import com.summer.locationtracker.utils.WriteNUpdateUsersLocation
import java.lang.Exception

class FetchLocationService : Service() {
    private  val TAG = "FetchLocationService"
    private val locationServiceChannel = "LocationServiceChannel"

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        showNotification()
        updateUsersLastLocation()
        stopSelf()
        return START_STICKY
    }

    private fun showNotification() {
        val notificationIntent = Intent(this, MapsActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(
            this,
            0,
            notificationIntent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )
        val notification =
            NotificationCompat.Builder(applicationContext, locationServiceChannel)
                .setSmallIcon(R.drawable.ic_moon_stars)
                .setContentTitle("Fetching location updates")
                .setCategory(NotificationCompat.CATEGORY_PROGRESS)
                .setContentIntent(pendingIntent)
                .setSound(null)
                .build()
        startForeground(100, notification)
    }

    private fun updateUsersLastLocation() {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }
        LocationServices.getFusedLocationProviderClient(
            this
        ).lastLocation
            .addOnSuccessListener { location: Location? ->
                if (location != null){
                    Log.e(TAG,"Success")
                    val storeValue =
                        "From Service-> Latitude = ${location.latitude}    Longitude = ${location.longitude}"
                    try {
                        WriteNUpdateUsersLocation().createOrUpdate(applicationContext, storeValue)
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }else{
                    Log.e(TAG,"location is null")
                }
            }.addOnFailureListener {
                it.printStackTrace()
                Log.e(TAG,"Failed")
            }
    }
}