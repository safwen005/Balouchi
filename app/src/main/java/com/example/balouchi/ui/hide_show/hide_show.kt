package com.example.balouchi.ui.hide_show

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.balouchi.R
import com.example.balouchi.databinding.HideShowBinding

class hide_show : Fragment() {

    lateinit var view:Hide_show_viewmodel
    lateinit var binding:HideShowBinding
    lateinit var vv:View

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding=DataBindingUtil.inflate(inflater,R.layout.hide_show,container,false)
        view=ViewModelProvider(this).get(Hide_show_viewmodel::class.java)

        binding.apply {
            viewmodel=view
            lifecycleOwner=this@hide_show
            vv=root
        }
        view.lifecycleOwner=this@hide_show

        return vv
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        this.view.prepare()
    }


}