package com.example.balouchi.ui.login

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.core.app.ActivityCompat.startActivityForResult
import com.example.balouchi.R
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import javax.inject.Inject
import javax.inject.Singleton


class gmail  @Inject constructor(var loginFragment:login_fragment) {

    var Activity:Activity?=null
    lateinit var gso:GoogleSignInOptions
    lateinit var signInIntent:Intent

    fun prepare() {
            Activity=loginFragment.requireActivity()

            gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(Activity!!.getString(R.string.default_web_client_id))
                .requestEmail()
                .build()

            signInIntent = GoogleSignIn.getClient(Activity!!, gso).signInIntent
    }

    fun signIn(){
        prepare()
        loginFragment.startActivityForResult(signInIntent, 22)
    }

}