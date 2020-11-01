package com.example.balouchi.ui.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.balouchi.R
import com.example.balouchi.databinding.SettingsBinding
import com.example.balouchi.util.myad
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.InterstitialAd
import com.google.android.gms.ads.MobileAds
import kotlinx.android.synthetic.main.settings.*

class settings : Fragment() {



    lateinit var vv:View

    lateinit var vieww:Settings_viewmodel

    lateinit var binding:SettingsBinding


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding=DataBindingUtil.inflate(inflater,R.layout.settings,container,false)
        vieww=ViewModelProvider(this).get(Settings_viewmodel::class.java)
        vv=binding.root
        vieww.apply {
            lifecycleOwner=this@settings
            settings_view=vv
        }
        binding.apply {
            viewmodel=vieww
            lifecycleOwner=this@settings
        }

        return vv
    }



    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        requireActivity().apply {
        MobileAds.initialize(this)
        val adRequest = AdRequest.Builder().build()
        vv.apply {
            settingsad.loadAd(adRequest)
        }

        }
    }


    override fun onResume() {
        super.onResume()
        vieww.prepare()
    }
}