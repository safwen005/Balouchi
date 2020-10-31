package com.example.balouchi.data.repository.login.user.user.auth

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuth
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class Authh @Inject constructor() {

    lateinit var auth:FirebaseAuth

    fun login(email:String,password:String,login:Boolean):MutableLiveData<Any>{
        val result=MutableLiveData<Any>()
        auth=FirebaseAuth.getInstance()
        if (login){
            auth.signInWithEmailAndPassword(email,password).addOnCompleteListener {
                result.value=if (it.isSuccessful) it.result?.additionalUserInfo?.isNewUser else it.exception
            }
        }
        else {
            auth.createUserWithEmailAndPassword(email,password).addOnCompleteListener {
                               result.value=if (it.isSuccessful) it.result?.additionalUserInfo?.isNewUser  else it.exception
           }
        }

        return result
    }

    fun forget(email:String):LiveData<Any>{
         val result=MutableLiveData<Any>()
         auth=FirebaseAuth.getInstance()
        auth.sendPasswordResetEmail(email).addOnCompleteListener{
                if (it.isSuccessful){
                    result.value=1
                    return@addOnCompleteListener
                }
                result.value=it.exception
            }
        return result
    }

    fun verify():LiveData<Any>{
        val result=MutableLiveData<Any>()
        auth=FirebaseAuth.getInstance()
        auth.currentUser!!.sendEmailVerification().addOnCompleteListener {
            result.value=if (it.isSuccessful) 1 else it.exception
        }
        return result
    }



}