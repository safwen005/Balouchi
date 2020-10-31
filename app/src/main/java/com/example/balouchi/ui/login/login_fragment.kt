package com.example.balouchi.ui.login


import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.balouchi.R
import com.example.balouchi.databinding.LoginBinding
import com.example.balouchi.util.log
import com.example.balouchi.util.toast
import com.example.balouchi.util.toastg
import com.example.balouchi.util.toastr
import com.facebook.CallbackManager
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.common.api.ApiException
import kotlinx.android.synthetic.main.login.view.*


class login_fragment : Fragment(){

    lateinit var view:Login_viewmodel
    lateinit var binding:LoginBinding
    var callbackManager:CallbackManager?=null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding=DataBindingUtil.inflate(inflater,R.layout.login,container,false)

        view = ViewModelProvider(this).get(Login_viewmodel::class.java)

        view.lifecycleOwner=this@login_fragment

        binding.apply {
           viewmodel=view
           lifecycleOwner=this@login_fragment
        }

        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        this.view.prepare()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        callbackManager?.onActivityResult(requestCode, resultCode, data).also { log("fb") }
        if (requestCode==22 && resultCode==Activity.RESULT_OK){
                try {
                    val account = GoogleSignIn.getSignedInAccountFromIntent(data).getResult(ApiException::class.java)
                    view.firebaseAuth(account)
                } catch (e: ApiException) {
                    requireActivity().toastr("آسف حاول لاحقا")
                }
        }
    }




}