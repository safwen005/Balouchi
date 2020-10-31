package com.example.balouchi.ui.chat

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.balouchi.R
import com.example.balouchi.databinding.ChatBinding
import com.example.balouchi.util.*

class chat() : Fragment() {



    var view:Chat_Viewmodel?=null
    lateinit var binding:ChatBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding= DataBindingUtil.inflate(inflater,R.layout.chat,container,false)

        view = ViewModelProvider(this).get(Chat_Viewmodel::class.java)

        view?.lifecycleOwner=this@chat

        binding.apply {
            viewmodel=view
            lifecycleOwner=this@chat
        }

        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        view?.apply {
            list.forEach { it.remove() }
            moves.forEach { it.online=false }
            online.forEach { it.remove() }
        }
        requireActivity().save_chat()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setRetainInstance(true)
    }

}