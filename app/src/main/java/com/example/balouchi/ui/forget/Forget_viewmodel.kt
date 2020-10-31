package com.example.balouchi.ui.forget

import android.app.Activity
import android.app.AlertDialog
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import com.example.balouchi.R
import com.example.balouchi.data.repository.login.user.tools
import com.example.balouchi.data.repository.login.user.user.auth.Authh
import com.example.balouchi.user
import com.example.balouchi.util.*
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import kotlinx.android.synthetic.main.forget.*

class Forget_viewmodel : ViewModel() {

    lateinit var lifecycleOwner: forget_fragment

    var email:String?=null
    var Activity:user?=null

    lateinit var Auth: Authh
    lateinit var mydialog: AlertDialog
    lateinit var mycaptch:tools



    fun send() {
        lifecycleOwner.apply {
        Activity!!.apply {
            if (testinputs(msg)) {
                if (!isEmailValid(email!!)) {
                    msg.apply {
                        error = "نمط الإيميل غير صحيح"
                        requestFocus()
                    }
                    toastr("نمط الإيميل غير صحيح")
                    return
                }
                if (!valid.isVisible()) {
                    toastr("لم تثبت أنك أنسان")
                    return
                }
                mydialog.show()
                Auth.forget(email!!).observe(lifecycleOwner, Observer {
                    mydialog.dismiss()
                    if (it == 1) {
                        toastg("لقد أرسلنا لك رسالة عبر الأيميل")
                        onBackPressed()
                        return@Observer
                    }
                    if (it is FirebaseAuthInvalidUserException) {
                        msg.apply {
                            error = "إيميل خاطأ ، أو حساب غير موجود"
                            requestFocus()
                        }
                        toastr("إيميل خاطأ ، أو حساب غير موجود")
                        return@Observer
                    }
                    toastr("آسف حاول لاحقا")
                })
                return
            }
            toastr("ضع البريد الألكتروني")
        }
    }
    }

    fun Click(view:View){
        prepare()
        when(view.id) {
            R.id.send -> send()
            R.id.empty -> start_captcha()
        }
    }



    fun resume(){
        lifecycleOwner.apply {
            progress.invisibile()
            empty.visibile()
        }
    }

    fun start_captcha() {
            lifecycleOwner.apply {
                mycaptch.captcha(valid,progress,empty,Activity!!.application)
            }
    }

    fun prepare(){
            (lifecycleOwner.requireActivity() as user).apply {
                mycaptch= Activity!!.tools
                Auth=Authh
                mydialog=getSpots()
                Activity=this
            }

    }

}