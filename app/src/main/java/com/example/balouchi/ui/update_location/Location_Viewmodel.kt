package com.example.balouchi.ui.update_location

import android.app.AlertDialog
import android.view.View
import androidx.lifecycle.ViewModel
import com.example.balouchi.R
import com.example.balouchi.all_settings
import com.example.balouchi.data.repository.login.user.other_tools
import com.example.balouchi.data.repository.login.user.tools
import com.example.balouchi.util.*
import kotlinx.android.synthetic.main.update_location.*

class Location_Viewmodel : ViewModel() {

    lateinit var lifecycleOwner: update_location

    lateinit var Activity:all_settings

    lateinit var Location: other_tools.MyLocation

    var dialog:AlertDialog?=null

    var adress=""

    fun prepare(){
        lifecycleOwner.apply {
            (requireActivity() as all_settings).apply {
            Activity=this
            dialog=getSpots()
            max(location)
            anim(show_location, R.anim.open_edit)
            Location=other_tools.MyLocation(this,lifecycleOwner)
            Gps=Location
            Location.info= Pair(root,true)
             Activity.apply {
                 dialog!!.show()
                 Firestore.document("users/"+auth.currentUser!!.uid).get().addOnCompleteListener {
                     dialog!!.dismiss()
                     if (it.isSuccessful){
                         it.result?.get("street_adress")?.let {
                             location.setText(it.toString())
                             anim(location,R.anim.show)
                         }
                         return@addOnCompleteListener
                     }
                     onBackPressed()
                     toastr("هنالك مشكلة ، حاول مجددا !")
                 }
             }
        }

    }
  }
    fun Click(view:View){
        Activity.apply {
        when(view.id){
            R.id.find_my_location -> start()
            R.id.back -> onBackPressed()
            R.id.apply -> apply()
        }
        }
    }

    fun apply(){
        Activity.apply {
        lifecycleOwner.apply {
            if (testinputs(location)){
                dialog!!.show()
                Firestore.document("users/"+auth.currentUser!!.uid).update(hashMapOf("street_adress" to adress.trim()) as Map<String, Any>).addOnCompleteListener {
                    if (it.isSuccessful){
                        auth.currentUser!!.reload().addOnSuccessListener {
                            dialog!!.dismiss()
                            toastg("تم التعديل بنجاح !")
                            onBackPressed()
                        }
                        return@addOnCompleteListener
                    }
                    toastr("هنالك مشكلة !")
                }

            }
         }
      }
    }

    fun start(){
        if (dialog==null)
            prepare()
        Activity.apply {
        Location.apply {
            lifecycleOwner.apply {
                myresult.observe(this,  {
                    it?.let {
                        when{
                            it is Exception ->  toastr("هنالك مشكلة ، حاول مجددا !")
                            (it is Pair<*, *>) -> {
                                anim(location, R.anim.show)
                                it.second?.let {
                                    location.setText(it.toString())
                                }
                            }
                        }
                    }
                })
                start()
            }
            }
        }
    }
}