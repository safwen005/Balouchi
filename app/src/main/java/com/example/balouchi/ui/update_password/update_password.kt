package com.example.balouchi.ui.update_password

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.balouchi.R
import com.example.balouchi.databinding.PasswordBinding
import kotlinx.android.synthetic.main.password.view.*

class update_password : Fragment() {

    lateinit var view: Password_Viewmodel
    lateinit var binding:PasswordBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding= DataBindingUtil.inflate(inflater,R.layout.password,container,false)

        view = ViewModelProvider(this).get(Password_Viewmodel::class.java)

        view.lifecycleOwner=this@update_password

        binding.apply {
            viewmodel=view
            lifecycleOwner=this@update_password
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        this.view.prepare()
    }
}