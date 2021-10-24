package com.summer.locationtracker.ui.activities

import android.Manifest
import android.annotation.SuppressLint
import android.content.*
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.summer.locationtracker.R
import com.summer.locationtracker.databinding.ActivityMapsBinding
import android.content.pm.PackageManager
import android.location.Location
import android.os.Build
import android.widget.Toast
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.model.MapStyleOptions
import com.summer.locationtracker.data.local_user_storage.LocalUserDataStorage
import com.summer.locationtracker.utils.Constants
import com.summer.locationtracker.utils.ServiceAlarmHandler
import com.summer.locationtracker.utils.WriteNUpdateUsersLocation
import java.lang.Exception


class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityMapsBinding
    private lateinit var mFusedLocationProviderClient: FusedLocationProviderClient
    private var mReadPermissionGranted = false
    private var mWritePermissionGranted = false
    private var mLocationPermissionGranted = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(
            applicationContext
        )
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
        initFetchServiceAlarmManger()
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        if (Constants.getCurrentUiMode(this)) {
            mMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(this, R.raw.map_style))
        }
    }

    //true => Dark Mode : false => Light Mode


    override fun onStart() {
        super.onStart()
        updateOrRequestPermissions()
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            requestMultiplePermissionLauncher.launch(
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )
            )
        }
        updateUsersLastLocation()
    }

    private fun updateOrRequestPermissions() {
        //Read Permission
        val hasReadPermission =
            ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.READ_EXTERNAL_STORAGE
            ) == PackageManager.PERMISSION_GRANTED

        //Write Permission
        val hasWritePermission =
            ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            ) == PackageManager.PERMISSION_GRANTED
        val minSdk29 = Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q

        //Location Permission
        val hasLocationPermission = ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
        mReadPermissionGranted = hasReadPermission
        mWritePermissionGranted = hasWritePermission || minSdk29
        mLocationPermissionGranted = hasLocationPermission
        val permissionsToRequest = mutableListOf<String>()
        if (!mWritePermissionGranted) {
            permissionsToRequest.add(Manifest.permission.WRITE_EXTERNAL_STORAGE)
        }
        if (!mReadPermissionGranted) {
            permissionsToRequest.add(Manifest.permission.READ_EXTERNAL_STORAGE)
        }
        if (!mLocationPermissionGranted) {
            permissionsToRequest.add(Manifest.permission.ACCESS_FINE_LOCATION)
        }
        if (permissionsToRequest.isNotEmpty()) {
            requestMultiplePermissionLauncher.launch(permissionsToRequest.toTypedArray())
        }
    }

    private val requestMultiplePermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) {
            for (entry in it.entries) {
                val permissionName = entry.key
                val permissionResult = entry.value
                when (permissionName) {
                    Manifest.permission.ACCESS_FINE_LOCATION ->
                        if (permissionResult) {
                            updateUsersLastLocation()
                        } else {
                            Toast.makeText(
                                this,
                                "This permission is needed to perform smoothly",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                }
            }
        }

    override fun onBackPressed() {
        val a = Intent(Intent.ACTION_MAIN)
        a.addCategory(Intent.CATEGORY_HOME)
        startActivity(a)
    }

    @SuppressLint("MissingPermission")
    private fun updateUsersLastLocation() {
        updateOrRequestPermissions()
        mFusedLocationProviderClient.lastLocation
            .addOnSuccessListener { location: Location? ->
                if (location != null) {
                    val currentLatLongs = LatLng(location.latitude, location.longitude)
                    LocalUserDataStorage.setUsersLocationData(
                        applicationContext,
                        location.latitude.toString(),
                        location.longitude.toString()
                    )
                    mMap.addMarker(MarkerOptions().position(currentLatLongs).title("Your Location"))
                    mMap.moveCamera(CameraUpdateFactory.newLatLng(currentLatLongs))
                }
            }.addOnFailureListener {
                it.printStackTrace()
            }
    }

    private fun initFetchServiceAlarmManger() {
        val alarmManager = ServiceAlarmHandler(this)
        alarmManager.cancelAlarmManager() //cancel existing pending intent
        alarmManager.setAlarmManager()
    }
}