package com.example.balouchi

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.example.balouchi.data.repository.login.user.other_tools
import com.example.balouchi.data.repository.login.user.tools
import com.example.balouchi.data.repository.login.user.user.auth.Authh
import com.example.balouchi.data.repository.login.user.user.auth.manage_user
import com.example.balouchi.data.repository.login.user.user.storage
import com.example.balouchi.ui.home.home
import com.example.balouchi.util.*
import com.google.android.gms.ads.InterstitialAd
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.auth.api.Auth
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import javax.inject.Inject

class all_settings : AppCompatActivity() {

    @Inject
    lateinit var manage: manage_user

    @Inject
    lateinit var other_tools:other_tools

    @Inject
    lateinit var authh: Authh
    lateinit var auth: FirebaseAuth
    lateinit var  mystorage: FirebaseStorage
    lateinit var  Firestore: FirebaseFirestore
    lateinit var NavController:NavController

    var camera_storage:storage?=null

    var active=true

    var Gps:other_tools.MyLocation?=null

    lateinit var ad:InterstitialAd

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.all_settings)

        prepare()

        auth.currentUser!!.reload().addOnFailureListener {
            finish()
        }

         getApp().User.Injectsettings(this)

    }

    fun prepare(){
        MobileAds.initialize(this)
        ad=myad()
        Firestore=FirebaseFirestore.getInstance()
        auth=FirebaseAuth.getInstance()
        mystorage= FirebaseStorage.getInstance()
        NavController = Navigation.findNavController(this, R.id.nav_host_fragment)
    }



    override fun onBackPressed() {
        if (NavController.currentDestination?.getId()==R.id.settings2)
            active=false
        load_new()?.let {
            if (it){
                save_new(false)
                finish()
                return
            }
        }
        loadnever("change_password")?.let {
            if (it){
                savenever("change_password",false)
                finish()
                return
            }
        }
        loadnever("change_phone")?.let {
            if (it){
                savenever("change_phone",false)
                finish()
                return
            }
        }
        super.onBackPressed()
    }

    override fun onStop() {
        super.onStop()
        if (active)
            exist()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        for (permission in permissions) {
            if (ActivityCompat.checkSelfPermission(
                    this@all_settings,
                    permission
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                Gps?.apply {
                    start()
                    return
                }
                camera_storage?.apply {
                    start(isStorage)
                }
            } else if (!ActivityCompat.shouldShowRequestPermissionRationale(this@all_settings, permission)){
                Gps?.let {
                    savenever("location",true)
                    return
                }
                camera_storage?.isStorage?.let {
                    savenever(if (it) "storage" else "camera",true)
                }
            }


        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        Gps?.apply {
            if (resultCode==android.app.Activity.RESULT_OK)
                start()
            return
        }
    }
    fun ad(){
        if (ad.isLoaded)
            ad.show()
    }



}
