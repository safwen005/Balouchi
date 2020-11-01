package com.example.balouchi.ui.verify

import android.content.IntentFilter
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.example.balouchi.R
import com.example.balouchi.data.repository.login.user.tools
import com.example.balouchi.data.repository.login.user.user.auth.Authh
import com.example.balouchi.databinding.VerifyEmailBinding
import com.example.balouchi.ui.login.NoInternetBroad
import com.example.balouchi.util.*
import javax.inject.Inject

class verify_email : AppCompatActivity(){


    @Inject
    lateinit var authh: Authh

    @Inject
    lateinit var NoInternetBroad: NoInternetBroad

    @Inject
    lateinit var tools: tools

    lateinit var binding:VerifyEmailBinding
    lateinit var view:Verify_viewmodel

    lateinit var i:IntentFilter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.verify_email)



        binding = DataBindingUtil.setContentView(this, R.layout.verify_email)
        view = ViewModelProvider(this).get(Verify_viewmodel::class.java)

        getApp().User.getAuth(this)



        view.apply {
            binding.viewmodel=this
            lifecycleOwner=this@verify_email
            Authh =authh
            prepare()
        }

    }



    override fun onResume() {
        super.onResume()
        view.resume()
        i=IntentFilter().apply { addAction("android.net.conn.CONNECTIVITY_CHANGE") }
        registerReceiver(NoInternetBroad,i)
    }


    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(NoInternetBroad)
    }


}