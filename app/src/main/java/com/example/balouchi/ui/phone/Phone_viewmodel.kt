package com.example.balouchi.ui.phone

import android.app.Activity
import android.app.AlertDialog
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import com.example.balouchi.R
import com.example.balouchi.data.repository.login.user.tools
import com.example.balouchi.data.repository.login.user.user.Notificationn
import com.example.balouchi.data.repository.login.user.user.Personal_Info
import com.example.balouchi.data.repository.login.user.user.auth.manage_user
import com.example.balouchi.data.repository.login.user.user.user_data
import com.example.balouchi.ui.home.home
import com.example.balouchi.user
import com.example.balouchi.util.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.iid.FirebaseInstanceId
import kotlinx.android.synthetic.main.phone.*
import javax.inject.Inject

class Phone_viewmodel : ViewModel() {

    var numberr:String?=null
    var checked=false

    lateinit var mydialog:AlertDialog
    lateinit var phone_view:View
    lateinit var Activity:user
    lateinit var lifecycleOwner: phone_fragment
    lateinit var myauth:FirebaseAuth
    lateinit var mycaptch:tools



    @Inject
    lateinit var phone: phone



    lateinit var My_manage_user: manage_user


    fun Click(v:View){
        prepare()
        Activity.apply {
        when(v.id){
            R.id.empty ->    start_captcha()
            R.id.send ->     send()
            R.id.back ->     onBackPressed()
            R.id.bounoud ->  openWebPage(R.string.policy)
            R.id.siasa ->    openWebPage(R.string.terms)
        }
        }
    }

    fun start_captcha() {
        lifecycleOwner.apply {
            mycaptch= Activity.tools
            mycaptch.captcha(check,progress,empty,Activity.application)
        }

    }

    fun send() {

        phone_view.apply {
        Activity.apply {
        if (testinputs(number)) {
            if (!Regex("[0-9]{8}").matches(numberr!!)) {
                toastr("مطلوب 8 أرقام")
                number.error = "مطلوب 8 أرقام"
                return
            }
            if (!checked){
                toastr("يجب أن توافق على الشروط")
                return
            }
            if (!check.isVisible()){
                toastr("لم تثبت أنك أنسان")
                return
            }

            mydialog.apply {
            getApp().User.getPhone().setActivity(Activity).setNumber("+216"+numberr).setPhone_viewmodel(this@Phone_viewmodel).builder().getPhone(this@Phone_viewmodel)
            show()
            phone.verifyPhoneNumber().observe(lifecycleOwner, Observer { res ->
                when{
                    (res is Boolean)-> {
                        myauth.currentUser!!.apply {
                                My_manage_user.Function<Boolean>(
                                    "creatuser", hashMapOf(
                                        "info" to
                                                ToMap(
                                                    user_data(
                                                        uid = uid,
                                                        personal_info = Personal_Info(ptel = numberr)
                                                   )
                                                ),
                                        "isphone" to true,
                                        "notifications" to ToMap(Notificationn())
                                    ), true
                                ).observe(lifecycleOwner,
                                    Observer {
                                        dismiss()
                                        if (it) {
                                            if (res)
                                                Activity.save_new(true)
                                            toastg("مرحبا")
                                            go(home::class, hashMapOf("first_time" to "1"))
                                            finish()
                                            return@Observer
                                        }
                                        FirebaseAuth.getInstance().signOut()
                                        toastr("آسف حاول لاحقا")
                                        return@Observer
                                    })

                        }
                    }
                (res is FirebaseAuthInvalidCredentialsException) -> {
                        if (res.errorCode!="ERROR_INVALID_VERIFICATION_CODE"){
                            toastr("آسف حاول لاحقا")
                            dismiss()
                        }
                      return@Observer

                    }
                  }
                })
              }
            }
          }
         }
        }



    fun prepare(){
        myauth=FirebaseAuth.getInstance()
        (lifecycleOwner.requireActivity() as user).apply {
            Activity=this
            My_manage_user=manage_user
            mydialog=this.getSpots()
        }
    }

    fun resume(){
        lifecycleOwner.apply {
            progress.invisibile()
            empty.visibile()
        }
    }



}