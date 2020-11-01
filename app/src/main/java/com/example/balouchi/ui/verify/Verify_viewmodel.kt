package com.example.balouchi.ui.verify

import android.app.AlertDialog
import android.content.Intent
import android.view.View
import android.view.animation.AnimationUtils
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import com.example.balouchi.R
import com.example.balouchi.data.repository.login.user.tools
import com.example.balouchi.data.repository.login.user.user.auth.Authh
import com.example.balouchi.ui.home.home
import com.example.balouchi.util.*
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.verify_email.*
import java.lang.Exception

class Verify_viewmodel : ViewModel() {

    lateinit var Authh: Authh

    lateinit var lifecycleOwner: verify_email

    var auth:FirebaseAuth?=null

    lateinit var dialog:AlertDialog

    lateinit var mycaptcha: tools

    lateinit var Activity:verify_email



    fun start_captcha() {
        lifecycleOwner.apply {
          tools.captcha(check,progress,v_empty,application)
        }
    }

    fun Click(V: View){
        prepare()
        lifecycleOwner.apply {
        when(V.id){
            R.id.v_logout -> {
                saveuser("default","")
                auth!!.signOut()
                finish()
            }
            R.id.resend -> {
                captcha.apply {
                if (!isVisible()){
                    visibile()
                    anim(this,R.anim.open_edit)
                    toastr("لم تثبت أنك أنسان")
                    return
                }
                }
                change(true)
                Authh.verify().observe(this, Observer {
                    change(false)
                    if (it==1){
                        toastg("لقد أرسلنا لك رسالة لتفعيل حسابك")
                        resume(true)
                        return@Observer
                    }
                    toastr((it as Exception).message.toString())
                })
            }
            R.id.v_empty -> {
                start_captcha()
            }
            R.id.continuee -> {
                go(home::class)
                finish()
            }

            R.id.reload -> {
                dialog.show()
                auth!!.currentUser?.apply {
                    reload().addOnSuccessListener {
                        dialog.dismiss()
                        if (isEmailVerified) {
                            toastg("مرحبا")
                            go(home::class)
                        }
                    }
                }
            }
        }
    }
}
    fun resume(hide:Boolean=false){
        lifecycleOwner.apply {
            if (hide){
                check.invisibile()
                v_empty.visibile()
                captcha.invisibile()
                anim(captcha,R.anim.close_edit)
                return
            }
            progress.invisibile()
            v_empty.visibile()
        }
    }

    fun prepare() {
        lifecycleOwner.apply {
                auth = FirebaseAuth.getInstance()
                dialog = getSpots()
                Activity=this
        }
    }

    fun change(ok:Boolean){
        lifecycleOwner.apply {
        if (ok){
            resend.invisibile()
            sending.visibile()
            return
        }
        resend.visibile()
        sending.invisibile()
    }
    }
}