package com.example.balouchi.ui.verify

import android.app.AlertDialog
import android.content.Intent
import android.view.View
import android.view.animation.AnimationUtils
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import com.example.balouchi.R
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
                if (visibility==View.INVISIBLE){
                    visibile()
                    animation=AnimationUtils.loadAnimation(lifecycleOwner,R.anim.open_edit)
                    toastr("لم تثبت أنك أنسان")
                    return
                }
                }
                change(true)
                Authh.verify().observe(this, Observer {
                    change(false)
                    if (it==1){
                        toastg("لقد أرسلنا لك رسالة لتفعيل حسابك")
                        return@Observer
                    }
                    toastr((it as Exception).message.toString())
                })
            }
            R.id.v_empty -> {
                // CAPTCHA
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
    fun prepare() {
        lifecycleOwner.apply {
            if (auth == null) {
                auth = FirebaseAuth.getInstance()
                dialog = this.getSpots()
            }
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