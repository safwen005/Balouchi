package com.example.balouchi.ui.settings

import android.app.AlertDialog
import android.view.View
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.example.balouchi.R
import com.example.balouchi.all_settings
import com.example.balouchi.data.repository.login.user.user.user_data
import com.example.balouchi.util.*
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.settings.*

class Settings_viewmodel : ViewModel() {


    lateinit var lifecycleOwner: settings
    lateinit var settings_view:View
    lateinit var dialog:AlertDialog
    lateinit var functions:FirebaseFirestore
    lateinit var Activity:all_settings
    var Alert:AlertDialog?=null
    var navController: NavController?=null

    fun prepare(){
           lifecycleOwner.apply {
               if (isEmail())
                   mypassword.visibile()
              navController=Navigation.findNavController(settings_view)
               (requireActivity() as all_settings).apply {
                  Activity=this
                  functions= FirebaseFirestore.getInstance()
                  dialog=getSpots()

                  load_new()?.let {
                      if (it){
                          navController!!.navigate(R.id.action_settings2_to_hide_show)
                          return
                      }
                  }
                   loadnever("change_password")?.let {
                       if (it){
                           navController!!.navigate(R.id.action_settings2_to_update_password)
                           return
                       }
                   }

                   loadnever("change_phone")?.let {
                       if (it){
                           navController!!.navigate(R.id.action_settings2_to_update_phone)
                           return
                       }
                   }

                 dialog.apply {
                     lifecycleOwner.apply {
                     show()
                     functions.document("users/"+auth.currentUser!!.uid).get(
                     ).addOnSuccessListener {
                         dismiss()
                         it.toObject(user_data::class.java)?.apply {
                             onoff.isChecked=active_notification
                         }
                     }
                   }
                 }
              }
            }
    }

    fun onClick(v: View?) {
        navController!!.apply {
            when(v!!.id){
                R.id.hideshow ->   navigate(R.id.action_settings2_to_hide_show)
                R.id.profile ->    navigate(R.id.action_settings2_to_update_profile)
                R.id.mylocation -> navigate(R.id.action_settings2_to_update_location)
                R.id.mypassword -> start_password()
                R.id.thephone ->   start_phone()
                R.id.notification -> start_notification()
                R.id.onoff -> start_notification()
        }
        }
    }

    fun start_password(){
            Activity.apply {
                if (Alert==null)
                    Alert=other_tools.Alert(this,message = "لتغيير كلمة السر يجب عليك إعادة تسجيل الدخول من جديد ، موافق ؟", yes = {
                        savenever("change_password",true)
                        auth.signOut()
                        finishAffinity()
                    })
                Alert!!.show()
            }
    }

    fun start_phone(){
        Activity.apply {
            if (Alert==null)
                Alert=other_tools.Alert(this,message = "لتغيير الهاتف يجب عليك إعادة تسجيل الدخول من جديد ، موافق ؟", yes = {
                    savenever("change_phone",true)
                    auth.signOut()
                    finishAffinity()
                })
            Alert!!.show()
        }
    }

    fun start_notification(){
            Activity.apply {
                    dialog.apply {
                        lifecycleOwner.apply {
                        onoff.isChecked=!onoff.isChecked
                        show()
                        functions.document("users/"+auth.currentUser!!.uid).update(
                            hashMapOf("active_notification" to onoff.isChecked) as Map<String, Any>
                        ).addOnCompleteListener {
                            dismiss()
                            if (it.isSuccessful){
                                toastg("تم التغيير بنجاح")
                                return@addOnCompleteListener
                            }
                            toastg("هنالك مشكلة !")
                        }
                    }
                }
           }
    }
}