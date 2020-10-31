package com.example.balouchi.ui.update_phone

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.balouchi.R
import com.example.balouchi.databinding.UpdatePhoneBinding

class update_phone : Fragment() {

   lateinit var view: UpdatePhone_Viewmodel
   lateinit var binding:UpdatePhoneBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding= DataBindingUtil.inflate(inflater, R.layout.update_phone,container,false)

        view = ViewModelProvider(this).get(UpdatePhone_Viewmodel::class.java)

        view.lifecycleOwner=this@update_phone

        binding.apply {
            viewmodel=view
            lifecycleOwner=this@update_phone
        }


      return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        this.view.prepare()

    }
}