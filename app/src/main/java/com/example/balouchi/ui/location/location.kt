package com.example.balouchi.ui.location

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.SeekBar
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import com.example.balouchi.R
import com.example.balouchi.databinding.LocationBinding
import com.example.balouchi.ui.home.home
import kotlinx.android.synthetic.main.location.*

class location : DialogFragment() {
    lateinit var v:View
    lateinit var view:Location_Viewmodel
    lateinit var binding:LocationBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NO_TITLE, R.style.CustomDialog)
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding= DataBindingUtil.inflate(inflater,R.layout.location,container,false)

        view = ViewModelProvider(this).get(Location_Viewmodel::class.java)

        view.apply {
            mylifecycleOwner=this@location
        }

        binding.apply {
            viewmodel=view
            lifecycleOwner=this@location
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

         this.view.prepare()

    }

    override fun onStop() {
        (requireActivity() as home).Gps=null
        super.onStop()
    }
}