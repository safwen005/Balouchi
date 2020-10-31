package com.example.balouchi.ui.home

import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.Window
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.example.balouchi.R
import com.example.balouchi.all_settings
import com.example.balouchi.data.repository.login.user.other_tools
import com.example.balouchi.data.repository.login.user.tools
import com.example.balouchi.data.repository.login.user.user.auth.manage_user
import com.example.balouchi.data.repository.login.user.user.storage
import com.example.balouchi.databinding.HomeBinding
import com.example.balouchi.services.online
import com.example.balouchi.ui.login.NoInternetBroad
import com.example.balouchi.ui.news.news
import com.example.balouchi.ui.product.Product_Viewmodel
import com.example.balouchi.ui.profile.Profile_viewmodel
import com.example.balouchi.ui.specific_Categorie.Specific_Viewmodel
import com.example.balouchi.ui.verify.verify_email
import com.example.balouchi.util.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.storage.FirebaseStorage
import javax.inject.Inject
import kotlin.time.ExperimentalTime


class home() : AppCompatActivity(){


    lateinit var storage:FirebaseStorage
    lateinit var binding: HomeBinding
    lateinit var view: Home_viewmodel
    lateinit var auth:FirebaseAuth
    var running=true

    var Profile_viewmodel:Profile_viewmodel?=null
    var i:IntentFilter?=null
    var service:Intent?=null
    var myprofile:Profile_viewmodel?=null
    var specific:Specific_Viewmodel?=null
    var Gps: other_tools.MyLocation?=null
    var camera_storage: storage?=null
    var call:Product_Viewmodel?=null


    @Inject
    lateinit var NoInternetBroad: NoInternetBroad

    @Inject
    lateinit var otherTools: other_tools

    @Inject
    lateinit var manageUser: manage_user

    @Inject
    lateinit var mytools: tools

    var news:news?=null


    @ExperimentalTime
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        prepare()

        save_chat()

        auth.currentUser!!.reload().addOnFailureListener {
            finish()
            auth.signOut()
        }.addOnSuccessListener {
            token()
        }

        getApp().User.getInternetBroadcast(this)


        if (!(auth.currentUser!!.isEmailVerified) && intent.getStringExtra("first_time")=="1"){
            go(verify_email::class)
            finish()
            return
        }


        load_new()?.let {
            if (it){
                go(all_settings::class)
                running=false
            }

        }

        loadnever("change_password")?.let {
            if (it)
            {
                go(all_settings::class)
                running=false
            }
        }


        loadnever("change_phone")?.let {
            if (it)
            {
                go(all_settings::class)
                running=false
            }
        }



        if (Build.VERSION.SDK_INT >= 21) {
            window.requestFeature(Window.FEATURE_CONTENT_TRANSITIONS)
        }

        binding = DataBindingUtil.setContentView(this, R.layout.home)
        view = ViewModelProvider(this).get(Home_viewmodel::class.java)

        view.apply {
            binding.viewmodel=this
            home=this@home
            prepare()
        }

    }

    override fun onResume() {
        super.onResume()
        i=IntentFilter().apply { addAction("android.net.conn.CONNECTIVITY_CHANGE") }
        auth.currentUser?.uid?.let {
            service!!.putExtra("uid", it)
            startService(service!!)
        }
        registerReceiver(NoInternetBroad, i)
    }

    override fun onNavigateUp(): Boolean {
        return view.navigateUp()
    }

    override fun onDestroy() {
        i?.let {
            unregisterReceiver(NoInternetBroad)
        }
        if (running)
            exist()
        super.onDestroy()
    }

    override fun onBackPressed() {
        news?.view?.move?.let {
            go(home::class)
            finish()
            return
        }
        Profile_viewmodel?.apply {
         if (goBack())
             return
         }
        super.onBackPressed()
    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        for (permission in permissions) {
                if (ActivityCompat.checkSelfPermission(
                        this@home,
                        permission
                    ) == PackageManager.PERMISSION_GRANTED
                ) {
                    call?.apply {
                        call()
                        return
                    }
                    Gps?.apply {
                        start()
                        return
                    }
                    camera_storage?.apply {
                        start(isStorage)
                    }
                } else if (!ActivityCompat.shouldShowRequestPermissionRationale(
                        this@home,
                        permission
                    )) {
                    call?.let {
                        savenever("call",true)
                    }
                    Gps?.let {
                        savenever("location", true)
                        return
                    }
                    camera_storage?.isStorage?.let {
                        savenever(if (it) "storage" else "camera", true)
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

    @ExperimentalTime
    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        intent?.extras?.getString("info")?.let {
            news?.view?.prepare(it)
        }
    }


    fun prepare(){
        service=Intent(this, online::class.java)
        auth= FirebaseAuth.getInstance()
        storage= FirebaseStorage.getInstance()
    }


}

