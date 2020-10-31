package com.example.balouchi.services

import android.app.IntentService
import android.app.Service
import android.content.Intent
import android.os.*
import androidx.lifecycle.LiveData
import com.example.balouchi.data.repository.login.user.user.auth.manage_user
import com.example.balouchi.util.log
import com.google.android.gms.tasks.Task
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.functions.FirebaseFunctions
import com.google.firebase.functions.HttpsCallableReference
import com.google.firebase.functions.HttpsCallableResult
import javax.inject.Inject
import kotlin.time.ExperimentalTime
import kotlin.time.milliseconds

class online() : Service() {

    var move = true

    lateinit var uid: String

    lateinit var handler:Handler

    lateinit var run:Runnable

    lateinit var functions:FirebaseFunctions

    lateinit var firestore:FirebaseFirestore

    @ExperimentalTime
    override fun onCreate() {
        super.onCreate()

        functions=FirebaseFunctions.getInstance()
        handler= Handler()
        firestore= FirebaseFirestore.getInstance()
        run=object:Runnable{
            override fun run() {
                if (move){
                    update_online()
                    return
                }
                handler.removeCallbacks(run)
            }
        }
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        intent?.getStringExtra("uid")?.let {
            uid = it
            run.run()
        }
        return START_NOT_STICKY
    }

    @ExperimentalTime
    fun update_online(){
                firestore.document("users/"+uid).update(hashMapOf("lastlogin" to Timestamp.now().toDate().time.milliseconds.toLongMilliseconds()) as Map<String, Any>).addOnSuccessListener {
                    handler.postDelayed(run,60000)
                    return@addOnSuccessListener
                }
    }



}