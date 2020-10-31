package com.example.balouchi.ui.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.ArrayAdapter
import android.widget.SeekBar
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.balouchi.R
import com.example.balouchi.databinding.SearchBinding
import com.example.balouchi.ui.home.home
import com.example.balouchi.ui.post.Post
import com.example.balouchi.ui.post.Post_viewmodel
import com.example.balouchi.util.toolbar_text
import kotlinx.android.synthetic.main.search.view.*


class search : Fragment(){

    lateinit var view:Search_viewmodel
    lateinit var binding:SearchBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding= DataBindingUtil.inflate(inflater,R.layout.search,container,false)

        view = ViewModelProvider(this).get(Search_viewmodel::class.java)

        view.apply {
            lifecycleOwnerr=this@search
        }

        binding.apply {
            viewmodel=view
            lifecycleOwner=this@search
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

    override fun onResume() {
        super.onResume()
        binding.root.testt.requestFocus()
    }


}