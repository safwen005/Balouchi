package com.example.balouchi.ui.all_categories

import android.app.AlertDialog
import android.view.View
import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.balouchi.R
import com.example.balouchi.recycler.all_categories_recycler
import com.example.balouchi.ui.home.home
import com.example.balouchi.util.anim
import com.example.balouchi.util.manage_edit
import com.example.balouchi.util.max
import kotlinx.android.synthetic.main.allcategories.*

class All_Viewmodel : ViewModel() {

    lateinit var categories: Array<String>
    lateinit var lifecycleOwner: all_categories
    lateinit var Activity:home
    var mydialog:AlertDialog?=null


    fun Click(view:View){
        when(view.id){
            R.id.add_prod ->  Activity.view.NavController.navigate(R.id.action_all_categories_to_post2)
        }
    }

    fun prepare(){
        lifecycleOwner.apply {
                max(search)
            (requireActivity() as home).apply {
                Activity=this
                categories=resources.getStringArray(R.array.categories)
                all_categories_recycler.apply {
                    layoutManager= LinearLayoutManager(context, RecyclerView.VERTICAL,false)
                    adapter= all_categories_recycler(Activity,categories)

                    manage_edit(edit=lifecycleOwner.search,o = {
                        it?.also {
                                txt ->
                            if (it.isNotEmpty()){
                                adapter=all_categories_recycler(requireContext(),categories.filter { (it.contains(txt,true)) }.toTypedArray())
                                anim(this, R.anim.show)
                                return@manage_edit
                            }
                            adapter= all_categories_recycler(Activity,categories)
                        }
                    })

                }
            }


       }
    }

}