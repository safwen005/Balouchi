package com.example.balouchi.ui.phone

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.balouchi.R
import com.example.balouchi.databinding.PhoneBinding
import com.example.balouchi.util.edit_empty
import com.example.balouchi.util.invisibile
import com.example.balouchi.util.manage_edit
import com.example.balouchi.util.visibile
import kotlinx.android.synthetic.main.phone.view.*

class phone_fragment : Fragment() {

    lateinit var v:View
    lateinit var view:Phone_viewmodel
    lateinit var binding:PhoneBinding


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {


        binding=DataBindingUtil.inflate(inflater, R.layout.phone,container,false)


        view = ViewModelProvider(this).get(Phone_viewmodel::class.java)
        v=binding.root

        view.apply {
            phone_view=v
            lifecycleOwner=this@phone_fragment
        }

        binding.apply {
            viewmodel=view
            lifecycleOwner=this@phone_fragment
        }

        return v

    }


    override fun onResume() {
        super.onResume()
        view.resume()
    }
}