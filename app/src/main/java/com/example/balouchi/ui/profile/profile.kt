package com.example.balouchi.ui.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.balouchi.R
import com.example.balouchi.data.repository.login.user.user.auth.manage_user
import com.example.balouchi.databinding.ProfileBinding
import com.example.balouchi.ui.home.home
import com.example.balouchi.util.getApp
import kotlinx.android.synthetic.main.home.*
import kotlinx.android.synthetic.main.profile.view.*
import javax.inject.Inject

class profile : Fragment() {

    lateinit var binding:ProfileBinding
    lateinit var viewmodel:Profile_viewmodel


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding=DataBindingUtil.inflate(inflater,R.layout.profile,container,false)
        viewmodel = ViewModelProvider(this).get(Profile_viewmodel::class.java)

        viewmodel.lifecycleOwner=this@profile

        binding.apply {
            viewmodel=viewmodel
            lifecycleOwner=this@profile
        }

        return binding.root

    }

    override fun onResume() {
        super.onResume()
        viewmodel.apply {
            my_uid=arguments?.getString("uid")
            LoadInfo()
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setRetainInstance(true)
    }
}