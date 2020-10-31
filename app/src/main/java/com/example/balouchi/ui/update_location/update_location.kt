package com.example.balouchi.ui.update_location

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
import com.example.balouchi.databinding.UpdateLocationBinding
import kotlinx.android.synthetic.main.update_location.view.*

class update_location : Fragment() {
    lateinit var view:Location_Viewmodel
    lateinit var binding:UpdateLocationBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding= DataBindingUtil.inflate(inflater,R.layout.update_location,container,false)

        view = ViewModelProvider(this).get(Location_Viewmodel::class.java)

        view.apply {
            lifecycleOwner=this@update_location
        }

        binding.apply {
            viewmodel=view
            lifecycleOwner=this@update_location
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        this.view.prepare()
    }

    override fun onStop() {
        (requireActivity() as all_settings).Gps=null
        super.onStop()
    }
}