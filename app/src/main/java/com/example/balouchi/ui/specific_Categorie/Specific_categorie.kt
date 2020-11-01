package com.example.balouchi.ui.specific_Categorie

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.balouchi.R
import com.example.balouchi.data.repository.login.user.product.product_data
import com.example.balouchi.databinding.SpecificBinding
import com.example.balouchi.ui.home.home
import com.example.balouchi.util.log


class Specific_categorie : Fragment() {



    lateinit var view:Specific_Viewmodel
    lateinit var binding:SpecificBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding= DataBindingUtil.inflate(inflater,R.layout.specific,container,false)

        view = ViewModelProvider(this).get(Specific_Viewmodel::class.java)

        view.lifecycleOwner=this@Specific_categorie

        binding.apply {
            viewmodel=view
            lifecycleOwner=this@Specific_categorie
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (requireActivity() as home).ad()
    }

    override fun onResume() {
        super.onResume()
        this.view.prepare()
        arguments?.getParcelableArrayList<product_data>("products")?.let {
            this.view.search(it)
            return
        }
        arguments?.getInt("categorie")?.let {
            this.view.categorie(it)
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setRetainInstance(true)
    }

}