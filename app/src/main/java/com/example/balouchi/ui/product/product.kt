package com.example.balouchi.ui.product

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.transition.TransitionInflater
import com.example.balouchi.R
import com.example.balouchi.databinding.ProductBinding
import com.example.balouchi.ui.home.home
import com.example.balouchi.ui.login.NoInternetBroad
import com.example.balouchi.util.getApp
import com.example.balouchi.util.gone
import com.example.balouchi.util.visibile
import kotlinx.android.synthetic.main.product.view.*
import kotlin.time.ExperimentalTime

class product : Fragment() {

    lateinit var binding: ProductBinding
    lateinit var view: Product_Viewmodel
    var tool:View?=null


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {


        requireActivity().getApp().User.InjectProduct(this)


        binding=DataBindingUtil.inflate(inflater,R.layout.product,container,false)

        view = ViewModelProvider(this).get(Product_Viewmodel::class.java)


        view.lifecycleOwner=this@product

        binding.apply {
                viewmodel=view
                lifecycleOwner=this@product
         }


        return binding.root

    }

    @ExperimentalTime
    override fun onResume() {
        super.onResume()
        (requireActivity() as home).apply {
            call=this@product.view
            if (tool==null)
                tool=binding.tool
        }
        tool!!.gone()

        if(Build.VERSION.SDK_INT>=21)
            binding.root.mytool.elevation=15f
        view.apply {
            requireArguments().getString("path")?.let {
                path=it
            }
            prepare()
        }
    }

    override fun onStop() {
        super.onStop()
        tool!!.visibile()
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setRetainInstance(true)
    }



}