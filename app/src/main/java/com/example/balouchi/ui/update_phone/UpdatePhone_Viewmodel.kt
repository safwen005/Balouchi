package com.example.balouchi.ui.update_phone

import android.app.Activity
import android.app.AlertDialog
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import com.example.balouchi.R
import com.example.balouchi.all_settings
import com.example.balouchi.ui.phone.phone
import com.example.balouchi.util.*
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.PhoneAuthCredential
import kotlinx.android.synthetic.main.update_phone.*
import javax.inject.Inject

class UpdatePhone_Viewmodel : ViewModel() {

    lateinit var lifecycleOwner: update_phone

    lateinit var Activity:all_settings

    var dialog:AlertDialog?=null

    var number=""

    var Alert:AlertDialog?=null

    @Inject
    lateinit var phone: phone

    fun prepare(){
        lifecycleOwner.apply {
            (requireActivity() as all_settings).apply {
                Activity=this
                dialog=getSpots()
                anim(show_phone, R.anim.open_edit)
                manage_edit(edit=myphone,a = {
                    it?.let {
                        if (it.trim().length!=8){
                            if (theupload.isVisible()){
                                    theupload.gone()
                                    anim(theupload,R.anim.hide)

                           }
                            return@manage_edit
                        }
                        if (!theupload.isVisible()){
                            theupload.visibile()
                            anim(theupload,R.anim.show)
                        }
                    }
                })
            }

        }
    }

    fun Click(view:View){
        Activity.apply {
        when(view.id){
            R.id.mycancel -> onBackPressed()
            R.id.myupdatee -> start()
        }
    }
    }

    fun start(){
        dialog!!.apply {
            Activity.apply {
                getApp().User.getPhone().setActivity(Activity).setNumber("+216"+number).setPhone_viewmodel(this@UpdatePhone_Viewmodel).builder().getPhone(this@UpdatePhone_Viewmodel)
                show()
                phone.verifyPhoneNumber(true).observe(lifecycleOwner,  { res ->
                        if (res is PhoneAuthCredential){
                               auth.currentUser!!.reload().addOnSuccessListener {
                                       auth.currentUser!!.updatePhoneNumber(res).addOnSuccessListener {
                                               dismiss()
                                               Activity.onBackPressed()
                                               toastg("تم التعديل بنجاح !")
                                               ad()
                                       }
                               }
                        }
                 })
            }
        }
    }


}