package com.example.balouchi.ui.search

import android.app.AlertDialog
import android.location.Location
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.core.os.bundleOf
import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.balouchi.R
import com.example.balouchi.data.repository.login.user.other_tools
import com.example.balouchi.data.repository.login.user.product.DMS
import com.example.balouchi.data.repository.login.user.product.product_data
import com.example.balouchi.data.repository.login.user.product.search_data
import com.example.balouchi.recycler.search_recycler
import com.example.balouchi.ui.home.home
import com.example.balouchi.util.*
import kotlinx.android.synthetic.main.search.*


class Search_viewmodel : ViewModel() {

    var name:String?=null
    var minn:String=""
    var maxx:String=""



    lateinit var lifecycleOwnerr: search
    lateinit var Location:other_tools.MyLocation
    lateinit var dialog: AlertDialog
    var Activity: home?=null
    var mylist=ArrayList<HashMap<String, *>>()
    var products=ArrayList<product_data?>()
    var filter=ArrayList<product_data?>()
    var stop=true


   fun onClick(v: View) {
        lifecycleOwnerr.apply {
        Activity!!.apply {
            when (v.id) {
                R.id.country -> mytools.expandable(v, byc, byl)
                R.id.locationn -> mytools.expandable(v, byl, byc)
                R.id.srsh -> start()
                R.id.mytest -> {
                    if (names.isVisible()) {
                        names.gone()
                        anim(names, R.anim.hide)
                    }
                }
                R.id.byc -> mycountry.performClick()
                R.id.names -> {
                    lifecycleOwnerr.apply {
                        if (mynamee.filter().isNotEmpty()) {
                            if (stop){
                                stop=false
                                mygif.visibile()
                                manageUser.Function<ArrayList<HashMap<String, *>>>(
                                    "byname", hashMapOf(
                                        "name" to mynamee.filter()
                                    )
                                ).observe(lifecycleOwnerr,
                                    {
                                        stop=true
                                        mygif.gone()
                                        anim(names, R.anim.show_frame)
                                        if (it.isNotEmpty()){
                                            names.visibile()
                                            if (it.isEmpty())
                                                names.gone()
                                            mylist = it
                                            lifecycleOwnerr.names.adapter = search_recycler(
                                                this@Search_viewmodel,
                                                mylist,mynamee.filter()
                                            )
                                            return@observe
                                        }
                                        names.gone()

                                    })
                            }
                        }
                    }
                }
                R.id.mycategories -> categoriee.performClick()
                R.id.mycondition -> conditionn.performClick()
                R.id.mygaranty -> garanti.performClick()
                R.id.mynamee -> {
                        if (mynamee.filter().isEmpty()) {
                            names.gone()
                            anim(names, R.anim.hide)
                            return
                        }
                    if (stop){
                        stop=false
                        mygif.visibile()
                        manageUser.Function<ArrayList<HashMap<String, *>>>(
                            "byname", hashMapOf(
                                "name" to mynamee.filter()
                            )
                        ).observe(lifecycleOwnerr,
                            {
                                stop=true
                                mygif.gone()
                                if (it.isNotEmpty()){
                                    anim(names, R.anim.show_frame)
                                    names.visibile()
                                    mylist = it
                                    lifecycleOwnerr.names.adapter = search_recycler(
                                        this@Search_viewmodel,
                                        mylist,mynamee.filter()
                                    )
                                    return@observe
                                }
                                    names.gone()
                            })
                    }
                }
                }
        }
        }
    }

   fun prepare() {
       products.clear()
       mylist.clear()
       filter.clear()
        lifecycleOwnerr.apply {
            (requireActivity() as home).apply {
                Activity = this
                names.apply {
                bringToFront()
                layoutManager=LinearLayoutManager(Activity, RecyclerView.VERTICAL, false)
                adapter=search_recycler(this@Search_viewmodel, mylist,mynamee.filter())
                dialog=getSpots()
                }
                my_spinners(Pair(categoriee,R.array.categories),Pair(conditionn,R.array.casee),Pair(garanti,R.array.garanty),Pair(mycountry,R.array.countrys))
            Location=otherTools.MyLocation(this, lifecycleOwnerr)
            Location.apply {
            Gps=this
            info=Pair(binding.root, false)
           }
        }
            mytest.performClick()

            Activity!!.apply {
            manage_edit(edit=mynamee,a = {
                grab(it)
            })


            seekBar.manage_seekbar {
                    numbers.apply {
                        text = it.toString()
                        anim(this, R.anim.km_move)
                    }
                }


                mynamee.setOnFocusChangeListener { v, hasFocus ->
                    if (hasFocus) {
                            if (mynamee.filter().isNotEmpty()) {
                                if (stop){
                                    stop=false
                                mygif.visibile()
                                manageUser.Function<ArrayList<HashMap<String,*>>>(
                                    "byname", hashMapOf(
                                        "name" to mynamee.filter()
                                    )
                                ).observe(lifecycleOwnerr,
                                    {
                                        stop=true
                                        mygif.gone()
                                        if (it.isNotEmpty())
                                        {
                                            anim(names, R.anim.show_frame)
                                            names.visibile()
                                            mylist = it
                                            lifecycleOwnerr.names.adapter = search_recycler(
                                                this@Search_viewmodel,
                                                mylist,mynamee.filter()
                                            )
                                            return@observe
                                        }
                                        names.gone()
                                    })
                                return@setOnFocusChangeListener
                            }
                        }
                        return@setOnFocusChangeListener
                    }
                if (names.isVisible())
                {
                    names.gone()
                    anim(names, R.anim.hide)
                }
            }

        }
        }
        }

   fun my_spinners(vararg list:Pair<Spinner,Int>){
       list.forEach {
           it.apply {
               first.adapter=ArrayAdapter(Activity!!,R.layout.support_simple_spinner_dropdown_item,ArrayList<String>().apply {
                   add("الكل")
                   addAll(Activity!!.resources.getStringArray(second))
               })
           }
       }
   }

   fun start(){
        lifecycleOwnerr.apply {
            Activity!!.apply {
            Location.dialog.show()
                mynamee.setText("")
                    manageUser.Function<ArrayList<*>>(
                        "searchprod", hashMapOf(
                            "info" to ToMap(
                                collect_data()
                            )
                        )
                    ).observe(this,{
                        products.clear()
                        it.forEach {
                            products.add(fromJson(it, product_data::class))
                        }
                        if (byl.isVisible() && products.size>0) {
                            Location.apply {
                                myresult.observe(lifecycleOwner,{
                                    when(it){
                                        is Location -> it.apply {
                                            DMS(latitude,longitude).filter_distance()
                                        }
                                    }
                                })
                                start()
                            }
                            return@observe
                        }
                        Location.dialog.dismiss()
                        view.NavController.navigate(
                            R.id.action_searchh_to_specific_categorie, bundleOf(
                                "products" to products
                            )

                        )

               })

          }
        }
     }

   fun collect_data():search_data{
         lifecycleOwnerr.apply {
             return search_data(
                 name,
                 categoriee.selectedItemPosition ,
                 conditionn.selectedItemPosition ,
                 garanti.selectedItemPosition,
                 if (minn.isNotEmpty()) minn.toLong() else 0,
                 if (maxx.isNotEmpty()) maxx.toLong() else 100000
             ).apply {
                 if (byc.isVisible())
                 country= mycountry.selectedItemPosition
             }
         }
   }


   fun DMS.filter_distance(index:Int=0){
        lifecycleOwnerr.apply {
        if (index==products.size){
            Location.dialog.dismiss()
            Activity!!.view.NavController.navigate(
                R.id.action_searchh_to_specific_categorie, bundleOf(
                    "products" to filter
                )
            )
            return
        }
        products[index]!!.Location!!.coord?.let {
            if ((calcule_distance(it,this@filter_distance).toInt())<=seekBar.progress){
                filter.add(products[index])
            }
        }
        filter_distance(index+1)
    }
    }

   fun grab(it:String?){
        lifecycleOwnerr.apply {
            Activity!!.apply {
                if (stop){
                    it?.apply {
                        if (isNotEmpty()) {
                            stop=false
                            mygif.visibile()
                            Activity!!.manageUser.Function<ArrayList<HashMap<String, *>>>(
                                "byname", hashMapOf(
                                    "name" to trim()
                                )
                            ).observe(lifecycleOwnerr,
                                {
                                    stop=true
                                    mygif.gone()
                                    if (it.isNotEmpty()){
                                        names.visibile()
                                        Activity!!.anim(names, R.anim.show_frame)
                                        names.visibile()
                                        mylist = it
                                        lifecycleOwnerr.names.adapter = search_recycler(
                                            this@Search_viewmodel,
                                            mylist,this
                                        )
                                        return@observe
                                    }
                                    names.gone()

                                })
                            return
                        }
                        names.gone()
                        anim(names, R.anim.hide)
                    }
                }
            }
        }
    }

}