package com.example.balouchi.ui.register

import android.os.Bundle
import android.text.method.PasswordTransformationMethod
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.balouchi.R
import com.example.balouchi.databinding.RegisterBinding
import com.google.android.gms.safetynet.SafetyNet
import kotlinx.android.synthetic.main.register.view.*
import pl.droidsonroids.gif.GifDrawable


class register_fragment: Fragment() {

    lateinit var v:View
    lateinit var binding:RegisterBinding
    lateinit var view:Register_viewmodel


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding= DataBindingUtil.inflate(inflater,R.layout.register,container,false)

        view = ViewModelProvider(this).get(Register_viewmodel::class.java)

        v=binding.root

        view.lifecycleOwner=this@register_fragment

        binding.apply {
            viewmodel=view
            lifecycleOwner=this@register_fragment
        }


        return v
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        this.view.prepare()
    }

    override fun onResume() {
        super.onResume()
        view.resume()
    }

}