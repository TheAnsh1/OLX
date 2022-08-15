package com.example.olxapp.ui.splash

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import android.os.Bundle
import android.util.Log
import androidx.core.app.ActivityCompat
import com.example.olxapp.MainActivity
import com.example.olxapp.R
import com.example.olxapp.ui.BaseActivity
import com.example.olxapp.ui.login.LogIn
import com.example.olxapp.utilities.Constants
import com.example.olxapp.utilities.SharedPref
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.*
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import java.lang.Exception
import java.util.*

class SplashScreen:BaseActivity() {
    private  val MY_PERMISSIONS_REQUEST_LOCATION =100
    private val REQUEST_GPS=101
    private lateinit var fusedLocationClient:FusedLocationProviderClient
    private lateinit var locationCallback: LocationCallback
    private var locationRequest: LocationRequest?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.splashscreen)


        askforPermissions()
        fusedLocationClient= LocationServices.getFusedLocationProviderClient(this)
        getloactionCallback()
    }



    override fun onResume()
    {
        super.onResume()

    }
    private fun askforPermissions(){
        val permissions= arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION)
        ActivityCompat.requestPermissions(this,permissions,MY_PERMISSIONS_REQUEST_LOCATION)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if(requestCode==MY_PERMISSIONS_REQUEST_LOCATION) {
            var grarnted = false
            for (grantResults in grantResults) {
                if (grantResults == PackageManager.PERMISSION_GRANTED) {
                    grarnted = true
                }
            }
            if (grarnted) {
                enableGps()
            }
        }
    }
    private fun enableGps(){
        locationRequest=LocationRequest.create()
        locationRequest?.setInterval(300)
        locationRequest?.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
        val builder= LocationSettingsRequest.Builder()
        builder.addLocationRequest(locationRequest!!)
        val tesk=LocationServices.getSettingsClient(this).checkLocationSettings(builder.build())
        tesk.addOnCompleteListener(object : OnCompleteListener<LocationSettingsResponse> {
            override fun onComplete(p0: Task<LocationSettingsResponse>) {
                try{
                    val response=tesk.getResult(ApiException::class.java)
                    startLocationUpdate()

                }catch(execption: ApiException){
                    when(execption.statusCode){
                        LocationSettingsStatusCodes.RESOLUTION_REQUIRED->{
                            val resovable=execption as ResolvableApiException
                            resovable.startResolutionForResult(this@SplashScreen,REQUEST_GPS)
                        }
                    }
                }
            }

        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode==REQUEST_GPS){
            startLocationUpdate()
        }
    }


    private  fun startLocationUpdate(){

        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return
        }
        fusedLocationClient.requestLocationUpdates(locationRequest!!,locationCallback,null)
    }
    private fun getloactionCallback(){
        locationCallback=object:LocationCallback(){
            override fun onLocationResult(p0: LocationResult) {

                super.onLocationResult(p0)
                val location=p0.lastLocation
                 SharedPref(this@SplashScreen).setString(Constants.CITY_NAME,getCityName(location!!))
                if(SharedPref(this@SplashScreen).getString(Constants.USER_ID)?.isEmpty()!!){
                startActivity(Intent(this@SplashScreen, LogIn::class.java))
                finish()
                }
                else{
                    startActivity(Intent(this@SplashScreen,MainActivity::class.java))
                    finish()
                }
            }
        }
    }
    override fun onDestroy() {
        super.onDestroy()
        fusedLocationClient.removeLocationUpdates(locationCallback)

    }
    private fun  getCityName(location:Location):String{
        var cityName=""
        val geocoder= Geocoder(this,Locale.getDefault())
        try {
            val address=geocoder.getFromLocation(location?.latitude!!,location?.longitude!!,1)
            cityName=address[0].locality
        }catch (e:Exception){
            Log.d("locationExecption","Failed")
        }
        return cityName
    }
}