package com.example.balouchi.ui.notification

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.balouchi.R
import com.example.balouchi.databinding.NotificationBinding

class notification() : Fragment() {

    lateinit var view:Notification_Viewodel
    lateinit var binding:NotificationBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding= DataBindingUtil.inflate(inflater,R.layout.notification,container,false)

        view = ViewModelProvider(this).get(Notification_Viewodel::class.java)

        view.lifecycleOwner=this@notification

        binding.apply {
            viewmodel=view
            lifecycleOwner=this@notification
        }

        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        this.view.prepare()
    }


    override fun onStop() {
        super.onStop()
        view.goo=false
        view.Listener?.remove()
    }

}