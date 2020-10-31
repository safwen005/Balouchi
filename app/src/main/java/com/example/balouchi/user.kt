package com.example.balouchi


import android.app.AlertDialog
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.example.balouchi.data.repository.login.user.tools
import com.example.balouchi.data.repository.login.user.user.auth.Authh
import com.example.balouchi.data.repository.login.user.user.auth.manage_user
import com.example.balouchi.ui.home.home
import com.example.balouchi.ui.login.NoInternetBroad
import com.example.balouchi.util.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import kotlinx.android.synthetic.main.user.*
import javax.inject.Inject


class user : AppCompatActivity(){

    lateinit var dialog:AlertDialog
    lateinit var auth:FirebaseAuth

    var i:IntentFilter?=null

    @Inject
    lateinit var NoInternetBroad: NoInternetBroad


    @Inject
    lateinit var Authh: Authh

    @Inject
    lateinit var manage_user: manage_user

    @Inject
    lateinit var tools: tools

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.user)



        this.getApp().User.getInternetBroadcast(this)


        prepare()

        // if already logged in  !

        auth.currentUser?.let {
                toastg("مرحبا")
                go(home::class, hashMapOf("first_time" to true))
                finish()
                return
            }

        user_root.visibile()
        // if remember me 'on'  !
        if (existuser()) {
            dialog.apply {
                show()
                user_login().apply {
                Authh.login(first,second,true).observe(this@user, Observer {
                dismiss()
                    if (it !is Boolean){
                        saveuser("default","")
                        toastr(
                            if (it is FirebaseAuthInvalidCredentialsException) "كلمة المرور و الإيميل غير متطابقين"
                            else if (it is FirebaseAuthInvalidUserException) {
                                when (it.errorCode) {
                                    "ERROR_USER_NOT_FOUND" -> "إيميل خاطأ ، أو حساب غير موجود"
                                    "ERROR_WRONG_PASSWORD" -> "كلمة السر خاطئة"
                                    "ERROR_USER_DISABLED" -> "الحساب مغلق من طرف الأدارة"
                                    else -> "آسف حاول مجددا"
                                }
                            } else "آسف حاول مجددا"
                        )
                        return@Observer
                    }
                    toastg("مرحبا")
                    go(home::class, hashMapOf("first_time" to true))
                    finish()
                    return@Observer
                })
                }
                toastr("لا يوجد انترنت")
        }
        }
    }


    override fun onResume() {
        super.onResume()
        this.setvisibility(true)
        i=IntentFilter().apply { addAction("android.net.conn.CONNECTIVITY_CHANGE") }
        registerReceiver(NoInternetBroad,i)
    }


    override fun onDestroy() {
        super.onDestroy()
        i?.let {
            unregisterReceiver(NoInternetBroad)
        }
    }


   fun prepare(){
           dialog=getSpots()
           auth=FirebaseAuth.getInstance()
   }

}
