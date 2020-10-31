package com.example.balouchi.ui.login

import android.content.IntentFilter
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.example.balouchi.R
import com.example.balouchi.util.getApp
import com.example.balouchi.util.toast
import javax.inject.Inject

class NoInternet : AppCompatActivity()  ,
    InternetListenner {

    @Inject
    lateinit var NoInternetBroad: NoInternetBroad

    var i= IntentFilter().apply { addAction("android.net.conn.CONNECTIVITY_CHANGE") }

    var back=false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.nointernet)

    }

   override fun Listen() {
        back=true
        onBackPressed()
    }

    override fun onBackPressed() {
       if (!back){
           toast("لا يوجد انترنت")
           return
       }
        super.onBackPressed()
    }


    override fun onResume() {
        this.getApp().User.getInternetBroadcast(this)
        registerReceiver(NoInternetBroad,i)
        super.onResume()
    }


    override fun onDestroy() {
        unregisterReceiver(NoInternetBroad)
        super.onDestroy()
    }
}