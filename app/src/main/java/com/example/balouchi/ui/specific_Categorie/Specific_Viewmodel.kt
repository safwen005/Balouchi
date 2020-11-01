package com.example.balouchi.ui.specific_Categorie

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.view.View
import android.widget.AdapterView
import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.balouchi.R
import com.example.balouchi.data.repository.login.user.product.product_data
import com.example.balouchi.recycler.specific_categorie_recyclerview
import com.example.balouchi.ui.home.home
import com.example.balouchi.util.*
import kotlinx.android.synthetic.main.specific.*

class Specific_Viewmodel : ViewModel() {

    lateinit var lifecycleOwner:Specific_categorie
    lateinit var Activity:home
    var list= ArrayList<product_data?>()
    var mydialog: AlertDialog?=null
    var stop=true

    fun add(view:View){
        lifecycleOwner.apply {
        when(view.id){
            R.id.add ->  Activity.view.NavController.navigate(R.id.action_specific_categorie_to_post2)
            R.id.options -> spinner.performClick()
        }
        }
    }




    fun prepare(){
        (lifecycleOwner.requireActivity() as home).apply {
            Activity=this
            mydialog=getSpots()
            specific=this@Specific_Viewmodel
        }

    }

    fun search(mylist:ArrayList<product_data?>){
        lifecycleOwner.apply {
            list.clear()
                list= remove_item(mylist,3)
                results.text=mylist.size.toString()
                prepare_recycler()
            }
    }

    fun prepare_recycler(){
        lifecycleOwner.apply {
            recycler.apply {
                layoutManager= LinearLayoutManager(requireActivity(), RecyclerView.VERTICAL,false)
                adapter=specific_categorie_recyclerview(context,list)
            }
            spinner.onItemSelectedListener=object: AdapterView.OnItemSelectedListener{
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    when(position){
                        0 -> list.sortWith(compareByDescending {it?.date})
                        1->  list.sortWith(compareBy {it?.price?.toDouble()})
                        2->  list.sortWith(compareByDescending {it?.price?.toDouble()})
                    }
                    list=remove_item(list,3)
                    recycler.adapter= specific_categorie_recyclerview(Activity,list)

                }

                override fun onNothingSelected(parent: AdapterView<*>?) {

                }
            }
        }
    }

    fun categorie(categorie:Int){
        Activity.apply {
            mydialog!!.apply {
                    lifecycleOwner.apply {
                    show()
                manageUser.Function<ArrayList<HashMap<*,*>>>("getnews", hashMapOf("categorie" to categorie.toString() , "start" to (list.filter { it!=null }.size),"step" to 2)).observe(viewLifecycleOwner,
                    {
                        dismiss()
                        it.forEach {
                            if (list.size%3==0)
                                list.add(null)
                            list.add(fromJson(it,product_data::class))
                        }
                        recycler.apply {
                            results.text=it.size.toString()
                            anim(results, R.anim.show_frame)
                            prepare_recycler()
                            addOnScrollListener(object: RecyclerView.OnScrollListener() {
                                override fun onScrollStateChanged(
                                    recyclerView: RecyclerView,
                                    newState: Int
                                ) {
                                    super.onScrollStateChanged(recyclerView, newState)

                                    if (!recyclerView.canScrollVertically(1) && newState==RecyclerView.SCROLL_STATE_IDLE) {
                                        if (stop){
                                            stop=false
                                            show()
                                            manageUser.Function<ArrayList<HashMap<*,*>>>("getnews", hashMapOf("categorie" to categorie.toString() , "start" to (list.filter { it!=null }.size+1),"step" to 2)).observe(viewLifecycleOwner,
                                                {
                                                    stop=true
                                                    dismiss()
                                                    if (it.size>0){
                                                        results.text=(list.filter { it!=null }.size.toString())
                                                        anim(results,R.anim.show_frame)
                                                    }
                                                    if (it.size<2){
                                                        stop=false
                                                        return@observe
                                                    }
                                                    it.forEach {
                                                        if (list.size%3==0)
                                                            list.add(null)
                                                        list.add(fromJson(it,product_data::class))
                                                    }
                                                    adapter.notify(list.size,list.size+it.size)
                                                })
                                        }

                                    }
                                }
                            })

                        }
                  })
              }
        }
    }
    }
}