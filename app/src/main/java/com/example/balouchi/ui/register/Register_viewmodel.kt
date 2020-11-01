package com.example.balouchi.ui.register

import android.app.Activity
import android.app.AlertDialog
import android.text.method.PasswordTransformationMethod
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import com.example.balouchi.R
import com.example.balouchi.data.repository.login.user.tools
import com.example.balouchi.data.repository.login.user.user.Notificationn
import com.example.balouchi.data.repository.login.user.user.Personal_Info
import com.example.balouchi.data.repository.login.user.user.auth.Authh
import com.example.balouchi.data.repository.login.user.user.auth.manage_user
import com.example.balouchi.data.repository.login.user.user.user_data
import com.example.balouchi.ui.home.home
import com.example.balouchi.user
import com.example.balouchi.util.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthException
import com.google.firebase.iid.FirebaseInstanceId
import kotlinx.android.synthetic.main.register.*
import kotlin.check


class Register_viewmodel  : ViewModel(){

    var email:String?=null
    var password:String?=null
    var password2:String?=null
    var checked=false
    var Activity:user?=null

    lateinit var mydialog: AlertDialog
    lateinit var lifecycleOwner: register_fragment
    lateinit var myauth:FirebaseAuth
    lateinit var My_Auth: Authh
    lateinit var My_manage_user: manage_user
    lateinit var mycaptcha: tools


    fun Click(v:View){
            prepare()
            Activity!!.apply {
                when(v.id) {
                R.id.bounoud ->  openWebPage(R.string.policy)
                R.id.siasa ->    openWebPage(R.string.terms)
                R.id.register -> register()
                R.id.empty ->    start_captcha()
                R.id.backreg ->  onBackPressed()
            }
        }
    }

    fun start_captcha() {
        lifecycleOwner.apply {
            mycaptcha=Activity!!.tools
            mycaptcha.captcha(check,progress,empty,Activity!!.application)
        }
    }

    fun register() {
        Activity.apply {
        lifecycleOwner.apply {
            if (testinputs(email_edit,password_edit,password_repeat_edit)){
                if (!isEmailValid(email!!)) {
                    email_edit.apply {
                        error = "نمط الإيميل غير صحيح"
                        requestFocus()
                    }
                    toastr("نمط الإيميل غير صحيح")
                    return
                }
                 if (password!=password2){
                         password_edit.apply {
                             error= "كلمة السر غير متطابقة"
                             requestFocus()
                         }
                         password_repeat_edit.error= "كلمة السر غير متطابقة"
                         toastr("كلمة السر غير متطابقة")
                     return
                 }
                if (password!!.length<6){
                    toastr("كلمة السر ضعيفة")
                    password_edit.apply {
                        error= "كلمة السر ضعيفة"
                        requestFocus()
                    }
                    password_repeat_edit.error= "كلمة السر ضعيفة"
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


                creat()
                return
            }
            toastr("أملأ البيانات")
        }
        }
    }

    fun creat() {
        Activity!!.apply {
        mydialog.show()
        My_Auth.login(email!!,password!!,false).observe(lifecycleOwner, Observer {
            result ->
            if (result !is Boolean) {
                if (result is FirebaseAuthException) {
                    if (result.errorCode == "ERROR_ACCOUNT_EXISTS_WITH_DIFFERENT_CREDENTIA" || result.errorCode == "ERROR_EMAIL_ALREADY_IN_USE")
                        lifecycleOwner.email_edit.apply {
                            error = "الأيميل مستعمل"
                            requestFocus()
                        }
                    toastr("الأيميل مستعمل")
                    mydialog.dismiss()
                    return@Observer
                }
            }
            else{
                myauth.currentUser!!.apply {
                My_Auth.verify().observe(lifecycleOwner,Observer{
                if(it==1){
                        My_manage_user.Function<Boolean>("creatuser",
                            hashMapOf("info" to
                                    ToMap(user_data(uid = uid,personal_info = Personal_Info(username = email!!.substring(0,email!!.indexOf("@")),pemail = email.toString()),
                                        verified = false)) , "isphone" to false , "notifications" to ToMap(Notificationn())),true).observe(lifecycleOwner,
                            Observer {
                                if (it)
                                {
                                    mydialog.dismiss()
                                    toastg("لقد أرسلنا لك رسالة لتفعيل حسابك")
                                    if(result)
                                        save_new(true)
                                    go(home::class, hashMapOf("first_time" to "1"))
                                    finish()
                                    return@Observer
                                }
                                toastr("آسف حاول لاحقا")
                            })
                    return@Observer
                    }
                    FirebaseAuth.getInstance().signOut()
                    toastr("آسف حاول لاحقا")
                    return@Observer
                })
            }
                return@Observer
            }
            toastr("آسف حاول لاحقا")
        })
    }
    }

    fun prepare(){
            (lifecycleOwner.requireActivity() as user).apply{
                Activity=this
                myauth=FirebaseAuth.getInstance()
                My_Auth=Authh
                My_manage_user=manage_user
                mydialog=getSpots()
            }
            lifecycleOwner.apply {
                max(email_edit,password_edit,password_repeat_edit)
                password_input.setEndIconOnClickListener {
                    password_edit.transformationMethod=if (password_edit.transformationMethod is PasswordTransformationMethod) null else PasswordTransformationMethod.getInstance()
                    password_repeat_edit.transformationMethod=if (password_repeat_edit.transformationMethod is PasswordTransformationMethod) null else PasswordTransformationMethod.getInstance()
                }
            }
    }

    fun resume(){
        lifecycleOwner.apply {
            progress.invisibile()
            empty.visibile()
        }
    }


}


