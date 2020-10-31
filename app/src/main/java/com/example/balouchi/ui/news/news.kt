package com.example.balouchi.ui.news


import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.balouchi.R
import com.example.balouchi.data.repository.login.user.chat.last
import com.example.balouchi.data.repository.login.user.user.user_data
import com.example.balouchi.databinding.FragmentNewsBinding
import com.example.balouchi.ui.home.home
import com.example.balouchi.util.anim
import com.example.balouchi.util.getSpots
import com.example.balouchi.util.log
import com.example.balouchi.util.visibile
import com.google.android.gms.ads.MobileAds
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.fragment_news.*
import kotlin.time.ExperimentalTime


class news : Fragment() {

lateinit var view:News_viewmodel
lateinit var binding:FragmentNewsBinding
var array= ArrayList<View>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {


        binding= DataBindingUtil.inflate(inflater, R.layout.fragment_news,container,false)
        view = ViewModelProvider(this).get(News_viewmodel::class.java)

        view.apply {
            News_View=binding.root
            lifecycleOwner=this@news
        }

        binding.apply {
            viewmodel=view
            lifecycleOwner=this@news
        }

        MobileAds.initialize(requireActivity())

        return binding.root
    }


    @ExperimentalTime
    override fun onResume() {
        super.onResume()
        (requireActivity() as home).news=this
        requireActivity().intent?.extras?.getString("info")?.let {
            view.prepare(it)
            return
        }
        view.prepare()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        binding.root.apply {
            array.apply {
                add(c1)
                add(c2)
                add(c3)
                add(c4)
            }
             Thread{
                 kotlin.run {
                     for (i in array){
                         i.apply {
                             binding.root.post {
                                 requireContext().anim(this,R.anim.show_categorie)
                                 visibile()
                             }
                         }
                         Thread.sleep(170)
                     }
                 }
             }.start()
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setRetainInstance(true)
    }
      }

