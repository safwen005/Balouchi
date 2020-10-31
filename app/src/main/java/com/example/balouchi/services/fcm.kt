package com.example.balouchi.services

import com.example.balouchi.data.repository.login.user.start_notification
import com.example.balouchi.util.load_chat
import com.example.balouchi.util.log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage


class fcm : FirebaseMessagingService(){

    var Firestore: FirebaseFirestore
    var auth: FirebaseAuth

    init {
        auth=FirebaseAuth.getInstance()
        Firestore= FirebaseFirestore.getInstance()
    }


    override fun onMessageReceived(data: RemoteMessage) {
        super.onMessageReceived(data)
        baseContext.apply {
        data.data.apply {
            get("type")?.let {
               if (it.toBoolean()) {
                   if (auth.currentUser?.uid!=get("uid"))
                         trigger(get("path")!!)
                                return
               }
                   load_chat()?.let {
                           if (it!="no" && it!=get("uid")){
                               trigger(get("path")+"*"+get("uid"))
                           }

                   }
               }

            }
          }
        }


    fun MutableMap<String,String>.trigger(info:String) = start_notification(baseContext.applicationContext).start_notification(get("title")!!,get("body")!!,info)


    override fun onNewToken(token: String) {
        super.onNewToken(token)
        auth.currentUser?.uid?.let {
            Firestore.document("users/"+auth.currentUser?.uid).update(hashMapOf("token" to token) as Map<String, Any>)
        }
    }


}
