package com.example.balouchi.data.repository.login.user

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.location.Geocoder
import android.location.Location
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.provider.Settings
import android.view.Gravity
import android.view.View
import android.widget.TextView
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.example.balouchi.R
import com.example.balouchi.data.repository.login.user.product.DMS
import com.example.balouchi.ui.complete_post.complete_post
import com.example.balouchi.ui.home.home
import com.example.balouchi.util.*
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.*
import com.google.android.material.snackbar.Snackbar
import java.util.*
import javax.inject.Inject


class other_tools  @Inject constructor(var tools: tools) {


    fun Alert(c: Context, yes: (() -> Unit)? = null, no: (() -> Unit)? = null,message:String="هل تريد بالتأكيد حذف هذا المنتج ؟"): AlertDialog {

        AlertDialog.Builder(c, R.style.MyDialog).apply {
            setPositiveButton(
                "نعم"
            ) { _, _ ->
                yes?.let { it() }

            }
            setNegativeButton(
                "لا"
            ) { _, _ ->
                no?.let { it() }
            }
            setMessage(message)

            create().apply {
                setOnShowListener {
                    findViewById<TextView>(android.R.id.message).apply {
                        typeface = ResourcesCompat.getFont(c, R.font.et)
                        underline()
                        setTextColor(Color.RED)
                    }
                    getButton(AlertDialog.BUTTON_POSITIVE).apply {
                        setTextColor(Color.BLACK)
                        typeface = ResourcesCompat.getFont(c, R.font.items)
                    }
                    getButton(AlertDialog.BUTTON_NEGATIVE).apply {
                        setTextColor(Color.BLACK)
                        typeface = ResourcesCompat.getFont(c, R.font.items)
                    }
                }
                return this
            }
        }
    }



    inner class MyLocation(var Activity: Activity, var lifecycleOwner: Fragment) {
        var fusedLocationProviderClient: FusedLocationProviderClient? = null
        lateinit var locationRequest: LocationRequest
        lateinit var locationCallback: LocationCallback
        var gcd: Geocoder? = null
        var snake: Snackbar? = null
        var myresult: MutableLiveData<Any>
        lateinit var info: Pair<View, Boolean>
        var dialog: AlertDialog

        init {
            dialog = Activity.getSpots()
            myresult = MutableLiveData()
        }

        fun prepareLocation() {
            fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(Activity)
            locationRequest = LocationRequest.create()
            locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
            locationRequest.interval = 5000
            locationCallback = object : LocationCallback() {
                override fun onLocationResult(p0: LocationResult?) {
                    p0?.lastLocation?.let {
                        fusedLocationProviderClient!!.removeLocationUpdates(locationCallback)
                        try {
                            if (!info.second) {
                                myresult.value = it
                                return
                            }
                            get_adress(it)
                        } catch (e: Exception) {
                            myresult.value = e
                        }

                    }
                }
            }
            gcd = Geocoder(Activity, Locale.getDefault())
        }

        fun get_adress(location: Location) {
            Activity.apply {
                myresult.apply {
                    location.apply {
                        var result = ""
                        gcd!!.getFromLocation(latitude, longitude, 10).get(0).apply {
                            (0..maxAddressLineIndex).forEach {
                                result += getAddressLine(it) + "\n"
                            }
                        }
                        if (result.isEmpty()) {
                            value = null
                            dialog.dismiss()
                            return
                        }
                        tools.Retrofit(result)
                            .observe(lifecycleOwner, Observer {
                                it?.also {
                                    if (!it.isEmpty()) {
                                        value = Pair(DMS(latitude, longitude), it)
                                        dialog.dismiss()
                                        return@Observer
                                    }
                                }
                            })
                    }
                }
            }
        }

        fun start() {
            prepareLocation()
            dialog.show()
            Activity.apply {
                if (this.permissions(
                        android.Manifest.permission.ACCESS_FINE_LOCATION,
                        android.Manifest.permission.ACCESS_COARSE_LOCATION
                    )
                ) {

                    val locationsettingsbuilder =
                        LocationSettingsRequest.Builder()
                            .addLocationRequest(locationRequest).build()
                    val settings_client = LocationServices.getSettingsClient(Activity)

                    val locationsettingsresulttask =
                        settings_client.checkLocationSettings(locationsettingsbuilder)

                    locationsettingsresulttask.addOnSuccessListener {

                        fusedLocationProviderClient!!.requestLocationUpdates(
                            locationRequest,
                            locationCallback,
                            Activity.mainLooper
                        )
                        fusedLocationProviderClient!!.lastLocation

                    }.addOnFailureListener {
                        dialog.dismiss()
                        if (it is ResolvableApiException) {
                            try {
                                it.startResolutionForResult(this, 123)
                            } catch (e: Exception) {
                                myresult.value = e
                            }
                        }
                    }
                    return
                }
                dialog.dismiss()
                if (loadnever("location")!!) {
                    if (snake==null)
                        snake= prepare_snake(info.first, "الموقع")
                    snake!!.show()
                    myresult.value = null
                    return
                }
                request_permissions(
                    android.Manifest.permission.ACCESS_FINE_LOCATION,
                    android.Manifest.permission.ACCESS_COARSE_LOCATION
                )
            }
        }


    }


}