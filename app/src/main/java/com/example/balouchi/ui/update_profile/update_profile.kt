package com.example.balouchi.ui.update_profile

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.balouchi.R
import com.example.balouchi.all_settings
import com.example.balouchi.databinding.UpdateProfileBinding
import com.example.balouchi.ui.home.home
import com.example.balouchi.util.*
import com.facebook.Profile
import com.google.firebase.auth.FirebaseAuth

class update_profile : Fragment() {

    lateinit var view:Update_viewmodel
    lateinit var binding:UpdateProfileBinding

    var myuser = FirebaseAuth.getInstance().currentUser
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding= DataBindingUtil.inflate(inflater,R.layout.update_profile,container,false)

        view = ViewModelProvider(this).get(Update_viewmodel::class.java)

        view.lifecycleOwner=this@update_profile

        binding.apply {
            viewmodel=view
            lifecycleOwner=this@update_profile
        }

        return binding.root
    }

    override fun onResume() {
        super.onResume()
        this.view.prepare()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        view.Display(requestCode==1,data)
    }

    override fun onStop() {
        (requireActivity() as all_settings).camera_storage=null
        super.onStop()
    }
}