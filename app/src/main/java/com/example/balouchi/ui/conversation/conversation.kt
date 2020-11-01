package com.example.balouchi.ui.conversation

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.balouchi.R
import com.example.balouchi.databinding.ConversationBinding
import com.example.balouchi.ui.home.home
import com.example.balouchi.util.log
import com.example.balouchi.util.save_chat
import com.google.firebase.auth.FirebaseAuth
import kotlin.time.ExperimentalTime

class conversation : Fragment() {



    lateinit var view:Conversation_Viewmodel
    lateinit var binding:ConversationBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding= DataBindingUtil.inflate(inflater,R.layout.conversation,container,false)

        view = ViewModelProvider(this).get(Conversation_Viewmodel::class.java)

        view.lifecycleOwner=this@conversation

        binding.apply {
            viewmodel=view
            lifecycleOwner=this@conversation
        }

        return binding.root
    }

    @ExperimentalTime
    override fun onResume() {
        super.onResume()
        view.prepare()
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        view.Display(requestCode==1,data)
    }


    override fun onDestroy() {
        super.onDestroy()
        requireActivity().save_chat()
        view.apply {
            online?.remove()
            move=false
            Listener?.apply {
                remove()
            }
        }
        (requireActivity() as home).camera_storage=null
    }



}

