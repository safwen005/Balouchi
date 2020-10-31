package com.example.balouchi.ui.login

import android.app.Activity
import com.example.balouchi.user
import com.example.balouchi.util.log
import com.example.balouchi.util.toast
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.FacebookSdk
import com.facebook.FacebookSdk.getApplicationContext
import com.facebook.appevents.AppEventsLogger
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.facebook.login.widget.LoginButton
import java.util.*
import javax.inject.Inject


class facebook @Inject constructor(var Login_viewmodel:Login_viewmodel) {


    var Activity: Activity? = null
    lateinit var callbackManager: CallbackManager
    lateinit var login_button: LoginManager

    fun prepare() {
        Activity = Login_viewmodel.lifecycleOwner.requireActivity()
        FacebookSdk.sdkInitialize(getApplicationContext())
        AppEventsLogger.activateApp(Activity)
        callbackManager = CallbackManager.Factory.create()
        login_button = LoginManager.getInstance()

        Login_viewmodel.lifecycleOwner.callbackManager = callbackManager
        login_button.registerCallback(callbackManager, object : FacebookCallback<LoginResult?> {
            override fun onSuccess(loginResult: LoginResult?) {
                log("LoginResult")
                Login_viewmodel.firebaseAuth(loginResult!!.accessToken, false)
            }

            override fun onCancel() {
                log("onCancel")
            }

            override fun onError(exception: FacebookException) {
                Activity.toast("آسف حاول مجددا")
            }
        })


    }
    fun SignIn() {
        prepare()
        login_button.logInWithReadPermissions(
            Activity,
            Arrays.asList("public_profile", "email")
        )
    }
}