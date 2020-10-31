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

        /*
        MobileAds.initialize(requireActivity()) {}
        val adRequest = AdRequest.Builder().build()
        vv.apply {
            adView.loadAd(adRequest)
            var mInterstitialAd = InterstitialAd(requireActivity())
            mInterstitialAd.adUnitId = "ca-app-pub-3940256099942544/1033173712"
            mInterstitialAd.loadAd(AdRequest.Builder().build())
            onoff.setOnClickListener {
                if (mInterstitialAd.isLoaded) {
                    mInterstitialAd.show()
                } else {
                    Log.i("myapp", "The interstitial wasn't loaded yet.")
                }
            }

        }

         */

    }


    override fun onResume() {
        super.onResume()
        vieww.prepare()
    }
}