package com.example.balouchi.ui.post

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.balouchi.R
import com.example.balouchi.databinding.PostBinding
import com.example.balouchi.ui.home.home
import com.example.balouchi.util.log


class Post : Fragment(){

    lateinit var view:Post_viewmodel
    lateinit var binding:PostBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {


        binding= DataBindingUtil.inflate(inflater,R.layout.post,container,false)

        view = ViewModelProvider(this).get(Post_viewmodel::class.java)

        view.apply {
            lifecycleOwner=this@Post
        }

        binding.apply {
            viewmodel=view
            lifecycleOwner=this@Post
        }

        return binding.root

    }


    override fun onViewCreated(vieww: View, savedInstanceState: Bundle?) {
        super.onViewCreated(vieww, savedInstanceState)
        view.prepare()
    }


    override fun onStop() {
        (requireActivity() as home).Gps=null
        super.onStop()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        log(resultCode)
    }


}


