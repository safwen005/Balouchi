package com.example.balouchi.ui.profile_items

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.balouchi.R
import com.example.balouchi.data.repository.login.user.product.product_data
import com.example.balouchi.databinding.ProfileBinding
import com.example.balouchi.databinding.ProfileItemsBinding
import com.example.balouchi.recycler.profile_recyclerview
import com.example.balouchi.ui.profile.Profile_viewmodel
import com.example.balouchi.util.*
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.profile.*
import kotlinx.android.synthetic.main.profile_items.view.*
import kotlin.concurrent.thread


class profile_items(

    var products: ArrayList<product_data?>?,
    var test: Pair<Boolean,Boolean>?,
    var load:View?,
    var uid:String?

) : Fragment() {
    constructor():this(null,null,null,null)

    lateinit var view:Profileitems_ViewModel
    lateinit var binding:ProfileItemsBinding


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding= DataBindingUtil.inflate(inflater,R.layout.profile_items,container,false)

        view = ViewModelProvider(this).get(Profileitems_ViewModel::class.java)

        view.apply {
            lifecycleOwner=this@profile_items

            myproducts=products!!
            mytest=test!!
            myload=load!!
            myuid=uid

        }

        binding.apply {
            viewmodel=view
            lifecycleOwner=this@profile_items
        }

        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        this.view.prepare()

    }


    }

