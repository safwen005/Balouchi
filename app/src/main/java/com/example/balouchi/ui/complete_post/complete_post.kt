package com.example.balouchi.ui.complete_post

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.balouchi.R
import com.example.balouchi.databinding.CompletePostBinding
import com.example.balouchi.ui.home.home
import com.example.balouchi.util.log
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.MobileAds
import kotlinx.android.synthetic.main.complete_post.view.*


class complete_post(): Fragment() {

lateinit var binding:CompletePostBinding
lateinit var vieww:Complete_post_viewmodel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding= DataBindingUtil.inflate(
            inflater,
            R.layout.complete_post, container, false
        )
        vieww = ViewModelProvider(this).get(Complete_post_viewmodel::class.java)
        vieww.apply {
            lifecycleOwner=this@complete_post
        }
        binding.apply {
            viewmodel=vieww
            lifecycleOwner=this@complete_post
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        MobileAds.initialize(requireActivity())
        view.completead.loadAd(AdRequest.Builder().build())

        vieww.prepare()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
                if (resultCode== Activity.RESULT_OK){
                    vieww.Display(requestCode==1,data)
                }
        }




}



