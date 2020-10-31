package com.example.balouchi.ui.news

import android.app.AlertDialog
import android.os.Bundle
import android.util.ArrayMap
import android.view.View
import android.view.WindowManager
import androidx.core.os.bundleOf
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.balouchi.R
import com.example.balouchi.data.repository.login.user.chat.last
import com.example.balouchi.data.repository.login.user.product.product_data
import com.example.balouchi.data.repository.login.user.user.user_data
import com.example.balouchi.recycler.categories
import com.example.balouchi.recycler.categories_recycler
import com.example.balouchi.recycler.items_recycler
import com.example.balouchi.ui.home.home
import com.example.balouchi.util.fromJson
import com.example.balouchi.util.getSpots
import com.example.balouchi.util.log
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.fragment_news.*
import kotlin.time.ExperimentalTime
import kotlin.time.milliseconds

class News_viewmodel : ViewModel() {

    var Activty:home?=null
    lateinit var lifecycleOwner:news
    lateinit var News_View:View
    lateinit var navController:NavController
    lateinit var mydialog:AlertDialog
    lateinit var Firestore:FirebaseFirestore
    var items_adapter=ArrayList<items_recycler>()
    var categories= arrayListOf<categories?>(null)
    lateinit var allCategories:Array<String>
    var move:String?=null
    lateinit var list:List<Int>

    fun Click(View:View){

         navController.apply {

         View.apply {

         when{
             id==R.id.myall ->              navigate(R.id.action_news_to_all_categories)
             list.contains(id) ->           navigate(R.id.action_news_to_specific_categorie,bundleOf("categorie" to list.indexOf(id)+1))
             id==R.id.float_add_news ->     navigate(R.id.action_news_to_post2)
         }

        }

        }

        }

    @ExperimentalTime
    fun prepare(move:String?=null){
            (lifecycleOwner.requireActivity() as home).apply {
                list=listOf(R.id.c1,R.id.c2,R.id.c3,R.id.c4)
                Activty=this
                mydialog=getSpots()
                navController = Navigation.findNavController(News_View)
                Firestore= FirebaseFirestore.getInstance()
                allCategories=Activty!!.resources.getStringArray(R.array.categories)
                lifecycleOwner.apply {
                    float_add_news.bringToFront()
                    refreshh.setOnRefreshListener {
                        categories=arrayListOf(null)
                        grab_items()
                        refreshh.isRefreshing = false
                    }
                }

            }
        move?.let {
            this.move=it
            start(it)
            return
        }

        grab_items()
    }

    fun grab_items(){
        mydialog.show()
        items_adapter.clear()
            categories.clear()
            Activty!!.manageUser.Function<ArrayList<ArrayList<HashMap<*,*>>>>("mnews").observe(lifecycleOwner,
                {
                    result ->

                   mydialog.dismiss()

                   (0..result.size-1).forEach {
                           val mylist= ArrayList<product_data?>()
                           result[it].forEach {
                               mylist.add(fromJson(it,product_data::class))
                           }
                           items_adapter.add(items_recycler(Activty!!,mylist))
                           if (categories.size%3==0)
                               categories.add(null)
                           categories.add(categories(allCategories[it],items_adapter[it]))
                   }

                   lifecycleOwner.recycler.apply {
                       layoutManager= LinearLayoutManager(Activty, RecyclerView.VERTICAL,false)
                       adapter= categories_recycler(Activty!!,categories)
                       setFocusable(false)
                   }


             })
       }

    @ExperimentalTime
    fun start(info: String){
        Activty!!.apply {
            getSpots().apply {
                    if (info.contains("*")){
                        show()
                        Firestore.document("users/"+info.substring(info.indexOf("*")+1,info.length)).get().addOnSuccessListener {
                                doc ->
                            doc.toObject(user_data::class.java)?.apply {
                                dismiss()
                                view.NavController.navigate(
                                    R.id.action_news_to_conversation,
                                    bundleOf(
                                        "data" to last(
                                            picture = personal_info?.ppicture?.path,
                                            path =  info.substring(0,info.indexOf("*")),
                                            token = token,
                                            username = personal_info?.username,
                                            online =if (lastlogin==null) false else ((Timestamp.now().toDate().time.milliseconds.toLongMilliseconds())-lastlogin!!)<=60000,
                                            uid = uid,
                                            lastlogin = lastlogin
                                        )
                                    )
                                )
                            }
                        }

                        return
                    }
                    view.NavController.navigate(R.id.action_news_to_product, bundleOf("path" to info))
            }
        }

    }
}