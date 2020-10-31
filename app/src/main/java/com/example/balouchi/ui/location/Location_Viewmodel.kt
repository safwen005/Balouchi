package com.example.balouchi.ui.location

import android.app.AlertDialog
import android.location.Location
import android.view.View
import android.widget.SeekBar
import androidx.core.os.bundleOf
import androidx.lifecycle.ViewModel
import com.example.balouchi.R
import com.example.balouchi.data.repository.login.user.other_tools
import com.example.balouchi.data.repository.login.user.product.DMS
import com.example.balouchi.data.repository.login.user.product.product_data
import com.example.balouchi.ui.home.home
import com.example.balouchi.util.*
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.location.*
import kotlinx.android.synthetic.main.location.numbers
import kotlinx.android.synthetic.main.location.seekBar
import kotlinx.android.synthetic.main.search.*

class Location_Viewmodel : ViewModel() {

    lateinit var mylifecycleOwner: location

    lateinit var Activity: home

    lateinit var Location: other_tools.MyLocation

    var products=ArrayList<product_data?>()
    var filter=ArrayList<product_data?>()

    var mydialog: AlertDialog? = null



    fun Click(view: View) {
        mylifecycleOwner.apply {
            when (view.id) {
                R.id.back -> dismiss()
                R.id.search -> start()
            }
        }
    }


    fun prepare() {
            (mylifecycleOwner.requireActivity() as home).apply {
                if (mydialog == null) {
                    mydialog = getSpots()
                    Activity = this
                }
                mylifecycleOwner.apply {
                img.bringToFront()
                img.bringToFront()
                seekBar.manage_seekbar {
                    numbers.apply {
                        text = it.toString()
                        anim(this, R.anim.km_move)
                    }
                }
                }
                Location = otherTools.MyLocation(this, mylifecycleOwner)
                Location.apply {
                    Gps = this
                    info = Pair(binding.root, false)
                }

        }

    }

    fun start(){
        Activity.apply {
        Location.apply {
            dialog.show()
            manageUser.Function<ArrayList<HashMap<*, *>>>(
                "search",
                hashMapOf("location" to true)
            ).observe(lifecycleOwner, {
                if (it.size==0){
                    toastr("لا توجد نتائج !")
                    dialog.dismiss()
                    return@observe
                }
                it.forEach {
                    products.add(fromJson(it, product_data::class))
                }
                myresult.observe(lifecycleOwner,{
                    when(it){
                        is Location -> it.apply {
                            DMS(latitude,longitude).filter_distance()
                        }
                        else -> {
                            dialog.dismiss()
                            mylifecycleOwner.dismiss()
                        }
                    }
                })
                start()
            })
            }
        }
    }



    fun DMS.filter_distance(index:Int=0){
        mylifecycleOwner.apply {
            if (index==products.size){
                Location.dialog.dismiss()
                if (filter.size==0){
                    Activity.toastr("لا توجد نتائج !")
                    return
                }
                mylifecycleOwner.dismiss()
                Activity.view.NavController.navigate(
                    R.id.action_news_to_specific_categorie, bundleOf(
                        "products" to filter
                    )
                )
                return
            }
            products[index]!!.Location!!.coord?.let {
                if ((calcule_distance(it,this@filter_distance).toInt())<=seekBar.progress){
                    filter.add(products[index])
                }
                filter_distance(index+1)
            }
            filter_distance(index+1)
        }
    }
}