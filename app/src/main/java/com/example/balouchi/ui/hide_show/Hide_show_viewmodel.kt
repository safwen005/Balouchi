package com.example.balouchi.ui.hide_show

import android.app.Activity
import android.app.AlertDialog
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import com.example.balouchi.R
import com.example.balouchi.all_settings
import com.example.balouchi.data.repository.login.user.user.auth.manage_user
import com.example.balouchi.data.repository.login.user.user.Hide_Show
import com.example.balouchi.data.repository.login.user.user.user_data
import com.example.balouchi.user
import com.example.balouchi.util.*
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.functions.FirebaseFunctions
import kotlinx.android.synthetic.main.hide_show.*


class Hide_show_viewmodel : ViewModel() {

    lateinit var lifecycleOwner: hide_show

    lateinit var Activity: all_settings

    var dialog: AlertDialog? = null

    lateinit var Firestore: FirebaseFirestore


    fun Click(V: View) {
        prepare()
        Activity.apply {
            when (V.id) {
                R.id.cancell -> onBackPressed()
                R.id.applyy -> {
                    dialog!!.apply {
                        lifecycleOwner.apply {
                            show()
                            Firestore.document("users/" + auth.currentUser!!.uid).update(
                                hashMapOf(
                                    "hide_show" to ToMap(
                                        Hide_Show(
                                            switch_email.isChecked,
                                            switch_tel.isChecked,
                                            switch_location.isChecked
                                        )
                                    )
                                ) as Map<String, Any>
                            ).addOnCompleteListener {
                                dismiss()
                                if (it.isSuccessful) {
                                    toastg("تم التغيير بنجاح")
                                    Activity.onBackPressed()
                                    ad()
                                    return@addOnCompleteListener
                                }
                                toastg("هنالك مشكلة !")
                            }
                        }
                    }
                }
                R.id.change_email -> change_switch(switch_email)
                R.id.change_tel -> change_switch(switch_tel)
                R.id.change_location -> change_switch(switch_location)
            }
        }
    }

    fun prepare() {
        if (dialog == null) {
            (lifecycleOwner.requireActivity() as all_settings).apply {
                dialog = getSpots()
                Firestore= FirebaseFirestore.getInstance()
                Activity = this
                dialog!!.apply {
                    lifecycleOwner.apply {
                        show()
                        Firestore.document("users/" + auth.currentUser!!.uid).get()
                            .addOnSuccessListener {
                                dismiss()
                                it.toObject(user_data::class.java)?.apply {
                                    hide_show?.apply {
                                        switch_email.isChecked = iemail
                                        switch_tel.isChecked = itel
                                        switch_location.isChecked = ilocation
                                    }

                                }
                            }
                    }
                }
            }
        }

    }
}