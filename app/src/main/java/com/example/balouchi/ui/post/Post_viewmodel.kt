package com.example.balouchi.ui.post

import android.app.AlertDialog
import android.view.View
import androidx.core.os.bundleOf
import androidx.lifecycle.ViewModel
import com.example.balouchi.R
import com.example.balouchi.data.repository.login.user.other_tools
import com.example.balouchi.data.repository.login.user.product.DMS
import com.example.balouchi.data.repository.login.user.product.Products
import com.example.balouchi.data.repository.login.user.product.Location
import com.example.balouchi.data.repository.login.user.tools
import com.example.balouchi.ui.home.home
import com.example.balouchi.util.*
import kotlinx.android.synthetic.main.post.*
import java.lang.Exception
import kotlin.collections.HashMap
class Post_viewmodel() : ViewModel() {

    lateinit var lifecycleOwner: Post
    var dialog: AlertDialog? = null
    lateinit var location: Location
    var product = Products()
    lateinit var Activity: home
    lateinit var Location: other_tools.MyLocation
    var firt_time = true
    var thename=""
    var mylocation=""
    var theprice=""


    fun prepare() {
        lifecycleOwner.apply {
            prepare_all()
            max(myname, myprice, edit_location)
            if (firt_time)
                arguments?.getString("path")?.let {
                    dialog!!.show()
                    Activity.manageUser.Function<HashMap<String, Any>>(
                        "theprod",
                        hashMapOf("path" to it,"exist" to true)
                    ).observe(viewLifecycleOwner,
                         {
                            dialog!!.dismiss()
                            product = fromJson(it, Products::class)
                            product.apply {
                                myname.setText(name)
                                edit_location.setText(Location.adress?.trim() ?: "بدون عنوان")
                                myprice.setText(price.toString())
                                Description!!.apply {
                                    categoriee.setSelection(categorie!!-1)
                                    conditionn.setSelection(condition!!-1)
                                    garanti.setSelection(garanty!!-1)
                                    countrys.setSelection(Location.country!!-1)
                                }
                                sellbuy.setSelection(if (buy_sell!!) 0 else 1)
                            }
                        })
                    firt_time = false
                }
        }
    }

    fun Click(v: View) {
        when (v.id) {
            R.id.find_location -> start()
            R.id.postt -> {
                lifecycleOwner.apply {
                    Activity.apply {
                    if (check_inputs(myname, edit_location, myprice)) {
                        collect_data()
                        if (product.Location.coord == null) {
                            Location.apply {
                                dialog.show()
                                 mytools.getLatLong(product.Location.adress!!)
                                    .observe(lifecycleOwner, {
                                        dialog.dismiss()
                                        product.Location.coord = it
                                         view.NavController.navigate(
                                            R.id.action_post2_to_complete_post,
                                            bundleOf("product" to product)
                                        )

                                    })
                            }
                            return
                        }

                        view.NavController.navigate(
                            R.id.action_post2_to_complete_post,
                            bundleOf("product" to product)
                        )
                    }
                }
                }

            }
            }
        }

    fun prepare_all() {
        (lifecycleOwner.requireActivity() as home).apply {
                Activity = this
                dialog = getSpots()
            Location=otherTools.MyLocation(this,lifecycleOwner)
            Gps=Location
            Location.info= Pair(lifecycleOwner.post_root,true)
        }
    }

    fun collect_data(){
        lifecycleOwner.apply {
        product.apply {
            buy_sell = sellbuy.selectedItemPosition == 0
            name = this@Post_viewmodel.thename.trim()
            Location.apply {
                adress = mylocation
                country = countrys.selectedItemPosition+1
            }
            price = theprice.toDouble()
            Description?.apply {
                categorie = categoriee.selectedItemPosition+1
                condition = conditionn.selectedItemPosition+1
                garanty = garanti.selectedItemPosition+1
            }
        }
        }
    }

    fun start(){
        prepare_all()
            Location.apply {
                lifecycleOwner.apply {
                myresult.observe(this,  {
                    it?.let {
                        when{
                            it is Exception ->  Activity.toastr("هنالك مشكلة ، حاول مجددا !")
                            (it is Pair<*, *>) -> {
                                    Activity.anim(edit_location, R.anim.show)
                                    it.second?.let {
                                        edit_location.setText(it.toString())
                                    }
                                    it.first?.let {
                                        product.Location.coord=it as DMS
                                    }
                            }
                        }
                    }
                })
                    start()
            }
        }
    }

}


