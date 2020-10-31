package com.example.balouchi.ui.profile_items

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.balouchi.data.repository.login.user.product.product_data
import com.example.balouchi.data.repository.login.user.user.auth.manage_user
import com.example.balouchi.recycler.profile_recyclerview
import com.example.balouchi.ui.home.home
import com.example.balouchi.util.*
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.profile_items.*
import java.text.FieldPosition
import kotlin.concurrent.thread

class Profileitems_ViewModel : ViewModel() {

    lateinit var lifecycleOwner: profile_items
    lateinit var Activity:home
    lateinit var dialog:AlertDialog
    lateinit var myproducts: ArrayList<product_data?>
    lateinit var mytest:Pair<Boolean,Boolean>
    lateinit var myload: View
    var stop=true

    var myuid:String?=null
    var toast=true
    var array=ArrayList<product_data?>()

    fun prepare(){
        (lifecycleOwner.requireActivity() as home).apply {
            Activity=this
            dialog=getSpots()
        }

        lifecycleOwner.apply {
            myproducts.apply {
                    array=products!!
                    profile_rec.layoutManager= LinearLayoutManager(requireContext(),
                        RecyclerView.VERTICAL,false)
                    profile_rec.setFocusable(false)
                    profile_rec.adapter=profile_recyclerview(this@Profileitems_ViewModel,array,test!!)

                    profile_rec.addOnScrollListener(object: RecyclerView.OnScrollListener() {
                        @SuppressLint("FragmentLiveDataObserve", "ShowToast")
                        override fun onScrollStateChanged(
                            recyclerView: RecyclerView,
                            newState: Int
                        ) {
                            super.onScrollStateChanged(recyclerView, newState)
                            if (!recyclerView.canScrollVertically(1) && newState==RecyclerView.SCROLL_STATE_IDLE) {
                                if (stop){
                                    stop=false
                                    myload.visibile()
                                    profile_rec.adapter!!.apply {
                                        array.apply {
                                            (requireActivity() as home).manageUser.Function<Any>("myprod" , hashMapOf("uid" to (myuid ?:FirebaseAuth.getInstance().currentUser!!.uid) , "pos" to  array.filter { it!=null }.size , "mine" to (myuid==null))).observe(lifecycleOwner,
                                                Observer {
                                                    stop=true
                                                    if (it is ArrayList<*>){
                                                        myload.invisibile()
                                                        it.forEach{
                                                                a->
                                                            if ((array.size)%4==0)
                                                                add(null)
                                                            add(fromJson(a,product_data::class))
                                                        }
                                                        notify(size+it.size,size)
                                                        return@Observer
                                                    }
                                                    myload.invisibile()
                                                    if (toast){
                                                        toast=false
                                                        thread {
                                                            requireActivity().apply {
                                                                runOnUiThread {
                                                                    toastr("لا توجد ملفات أخرى")
                                                                }
                                                                Thread.sleep(3000)
                                                                toast=true
                                                            }
                                                        }
                                                    }
                                                })
                                        }
                                    }
                                }
                            }
                        }
                    })
            }
        }
    }
    fun refresh(position: Int){
        array.removeAt( position)
        array= remove_item(array)
        lifecycleOwner.profile_rec.adapter= profile_recyclerview(this@Profileitems_ViewModel,array,mytest)
    }


}