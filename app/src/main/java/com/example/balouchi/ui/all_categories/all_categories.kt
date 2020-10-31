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
import com.example.balouchi.ui.home.home
import com.example.balouchi.util.log

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

    override fun onResume() {
        super.onResume()
        this.view.prepare()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setRetainInstance(true)
    }
}
