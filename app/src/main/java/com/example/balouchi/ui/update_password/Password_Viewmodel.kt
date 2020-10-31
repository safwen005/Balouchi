package com.example.balouchi.ui.update_password

import android.app.AlertDialog
import android.text.method.PasswordTransformationMethod
import android.view.View
import androidx.lifecycle.ViewModel
import com.example.balouchi.R
import com.example.balouchi.all_settings
import com.example.balouchi.util.*
import kotlinx.android.synthetic.main.password.*

class Password_Viewmodel : ViewModel() {

    lateinit var lifecycleOwner: update_password

    lateinit var Activty:all_settings

    var mydialog:AlertDialog?=null

    var mypassword=""
    var myrepeat_password=""


    fun Click(view:View){
        Activty.apply {
        when(view.id){
            R.id.myback -> onBackPressed()
            R.id.myupdate -> start()
        }
        }
    }

    fun prepare(){
        lifecycleOwner.apply {
            (requireActivity() as all_settings).apply {
                 Activty=this
                 mydialog=getSpots()

                 manage_edit(password,repeat_password,a={
                    it?.apply {
                        if (mypassword.trim()==myrepeat_password.trim() && length>0){
                            if (!apply.isVisible()){
                                apply.visibile()
                                return@manage_edit
                            }
                        }
                        if (apply.isVisible())
                            apply.gone()
                    }
                })

            }
            max(password,repeat_password)
            password_input.setEndIconOnClickListener {
                password.transformationMethod=if (password.transformationMethod is PasswordTransformationMethod) null else PasswordTransformationMethod.getInstance()
                repeat_password.transformationMethod=if (repeat_password.transformationMethod is PasswordTransformationMethod) null else PasswordTransformationMethod.getInstance()
            }

        }
    }

    fun start(){
        lifecycleOwner.apply {
            Activty.apply {
            if (password.filter().length<6){
                toastr("كلمة السر ضعيفة")
                password.apply {
                    error= "كلمة السر ضعيفة"
                    requestFocus()
                }
                repeat_password.error= "كلمة السر ضعيفة"
                return
            }
             mydialog!!.apply {
                    show()
             auth.currentUser!!.updatePassword(mypassword).addOnCompleteListener {
                 if (it.isSuccessful){
                     Activty.onBackPressed()
                     toastg("تم التعديل بنجاح !")
                     toastg("الرجاء ، سجيل الدخول من جديد !")
                     auth.signOut()
                     finishAffinity()
                     return@addOnCompleteListener
                 }
                 dismiss()
                 toastr("آسف حاول لاحقا")
             }
          }
        }
        }
    }
}