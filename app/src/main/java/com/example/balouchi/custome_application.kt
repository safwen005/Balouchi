package com.example.balouchi

import android.app.Application
import android.content.Context
import androidx.multidex.MultiDex
import com.example.balouchi.injection.DaggerUser
import com.example.balouchi.injection.User
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.iid.FirebaseInstanceId
import com.google.firebase.messaging.FirebaseMessaging


class custome_application : Application(){

    lateinit var User:User
    lateinit var Firestore:FirebaseFirestore
    lateinit var auth:FirebaseAuth
    lateinit var InstanceId:FirebaseInstanceId

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
        MultiDex.install(this)
    }

    override fun onCreate() {
        super.onCreate()
        auth=FirebaseAuth.getInstance()
        Firestore= FirebaseFirestore.getInstance()
        InstanceId=FirebaseInstanceId.getInstance()
        FirebaseApp.initializeApp(this)
        FirebaseMessaging.getInstance().isAutoInitEnabled=true
        User=DaggerUser.create()
    }


}