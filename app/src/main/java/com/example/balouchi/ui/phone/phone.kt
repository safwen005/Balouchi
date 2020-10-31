package com.example.balouchi.ui.phone

import android.app.Activity
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider
import java.util.concurrent.TimeUnit
import javax.inject.Inject


class phone @Inject constructor(var Activity: Activity, var number: String) {


    var user:FirebaseAuth?=null
    lateinit var PhoneAuthCallback: PhoneAuthProvider.OnVerificationStateChangedCallbacks



fun prepare(update:Boolean): LiveData<Any> {

    val results = MutableLiveData<Any>()

         user=FirebaseAuth.getInstance()

    results.apply {
        PhoneAuthCallback = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            override fun onVerificationCompleted(PhoneAuthCredential: PhoneAuthCredential) {

                user!!.signInWithCredential(PhoneAuthCredential).addOnCompleteListener {
                    if (it.isSuccessful) {
                        if (update){
                            value = PhoneAuthCredential
                            return@addOnCompleteListener
                        }
                        value = it.result!!.additionalUserInfo!!.isNewUser
                        return@addOnCompleteListener
                    }
                    value = it.exception
                }

            }

            override fun onVerificationFailed(FirebaseException: FirebaseException) {
                value = FirebaseException
            }

            override fun onCodeSent(
                VerificationId: String,
                p1: PhoneAuthProvider.ForceResendingToken
            ) {
                super.onCodeSent(VerificationId, p1)
                val PhoneAuthCredential = PhoneAuthProvider.getCredential(VerificationId, number)
                user!!.signInWithCredential(PhoneAuthCredential).addOnCompleteListener {
                    if (it.isSuccessful) {
                        if (update){
                            value = PhoneAuthCredential
                            return@addOnCompleteListener
                        }
                        value = it.result!!.additionalUserInfo!!.isNewUser
                        return@addOnCompleteListener
                    }
                    value = it.exception
                }
            }
        }
    }
    return results
}

    fun verifyPhoneNumber(update:Boolean=false):LiveData<Any>{
        val a=prepare(update)
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
            number, 60, TimeUnit.SECONDS, Activity
            , PhoneAuthCallback
        )
        return a
    }
}








