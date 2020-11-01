package com.example.balouchi.ui.all_categories

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.balouchi.R
import com.example.balouchi.databinding.AllcategoriesBinding
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.MobileAds
import kotlinx.android.synthetic.main.allcategories.view.*

class all_categories : Fragment() {

    lateinit var view:All_Viewmodel
    lateinit var binding:AllcategoriesBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding= DataBindingUtil.inflate(inflater,R.layout.allcategories,container,false)

        view = ViewModelProvider(this).get(All_Viewmodel::class.java)

        view.lifecycleOwner=this@all_categories

        binding.apply {
            viewmodel=view
            lifecycleOwner=this@all_categories
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        MobileAds.initialize(requireActivity())
            view.allad.loadAd(AdRequest.Builder().build())
    }

    override fun onResume() {
        super.onResume()
        this.view.prepare()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setRetainInstance(true)
    }
}
