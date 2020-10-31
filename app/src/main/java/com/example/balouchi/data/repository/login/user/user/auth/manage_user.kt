package com.example.balouchi.data.repository.login.user.user.auth

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.balouchi.data.repository.login.user.user.user_data
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirestoreRegistrar
import com.google.firebase.functions.FirebaseFunctions
import javax.inject.Inject
import javax.inject.Singleton

@Suppress("UNCHECKED_CAST")
@Singleton
class manage_user @Inject constructor() {

    var auth:FirebaseAuth
    var users:CollectionReference
    var Function:FirebaseFunctions

    init {
         auth=FirebaseAuth.getInstance()
         users = FirebaseFirestore.getInstance().collection("users")
         Function=FirebaseFunctions.getInstance()
    }


    fun <type> Function  (fn:String,data:Any?=null,reload:Boolean=false):LiveData<type> {
        val result=MutableLiveData<type>()
        result.apply {
        Function.getHttpsCallable(fn).call(data).addOnSuccessListener { res ->
            if (reload){
                auth.currentUser!!.reload().addOnSuccessListener {
                    value=(res.data  as type)
                }
                return@addOnSuccessListener
            }
            value=(res.data  as type)
        }
      }
        return result
    }


}