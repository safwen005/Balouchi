package com.example.balouchi.util

import android.app.AlertDialog
import android.content.Context
import android.graphics.Color
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.Uri
import android.os.Build
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.util.Patterns
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.navigation.NavController
import androidx.navigation.Navigation
import bolts.Bolts
import com.example.balouchi.custome_application
import com.example.balouchi.ui.login.Login_viewmodel
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import dmax.dialog.SpotsDialog

fun isEmailValid(email: String): Boolean {
    return Patterns.EMAIL_ADDRESS.matcher(email).matches()
}


fun testinputs(vararg list:TextInputEditText):Boolean{
    var result=true
    list.forEach {
        it.apply {
        if (text.toString().isEmpty()) {
            result = false
            error = "لاينبغي ان يكون فارغا"
            requestFocus()
        }
        }
    }
    return result
}

fun update(name:String?=null,photo:Uri?=null):LiveData<Boolean>{
    val result=MutableLiveData<Boolean>()
    val update = UserProfileChangeRequest.Builder()
             name?.let {
                update.setDisplayName(it)
             }
             photo?.let {
                update.setPhotoUri(photo)
             }

    val user=FirebaseAuth.getInstance().currentUser!!
    user.updateProfile(update.build()).addOnCompleteListener {
        result.value=it.isSuccessful
    }
    return result
}

fun edit_empty(edit:TextInputEditText,v:View){
    edit.addTextChangedListener(object:TextWatcher{
            override fun afterTextChanged(s: Editable?) {

            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s.toString().length==0){
                    v.visibile()
                    return
                }
                v.invisibile()
            }

        })
}