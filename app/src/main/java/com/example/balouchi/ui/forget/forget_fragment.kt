package com.example.balouchi.ui.forget

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.balouchi.R
import com.example.balouchi.databinding.ForgetBinding
import com.example.balouchi.util.edit_empty
import kotlinx.android.synthetic.main.forget.view.*

class forget_fragment : Fragment() {

    lateinit var binding:ForgetBinding
    lateinit var view:Forget_viewmodel
    lateinit var v:View

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding= DataBindingUtil.inflate(inflater,R.layout.forget,container,false)

        view = ViewModelProvider(this).get(Forget_viewmodel::class.java)

        v=binding.root
        view.apply {
            lifecycleOwner=this@forget_fragment
        }

        binding.apply {
            viewmodel=view
            lifecycleOwner=this@forget_fragment
        }

        v.apply {
            edit_empty(msg,text)
        }


        return v
    }

    override fun onResume() {
        super.onResume()
        view.resume()
    }
}