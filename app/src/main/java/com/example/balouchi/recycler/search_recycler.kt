package com.example.balouchi.recycler

import android.content.Context
import android.graphics.Color
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.core.content.res.ResourcesCompat
import androidx.core.os.bundleOf
import androidx.recyclerview.widget.RecyclerView
import com.example.balouchi.R
import com.example.balouchi.ui.home.home
import com.example.balouchi.ui.product.product
import com.example.balouchi.ui.search.Search_viewmodel
import com.example.balouchi.util.anim
import com.example.balouchi.util.filter
import com.example.balouchi.util.go
import kotlinx.android.synthetic.main.search.*
import kotlinx.android.synthetic.main.search_name.view.*
import kotlin.math.sign

class search_recycler(var c: Search_viewmodel,var list:ArrayList<HashMap<String,*>>,val name:String?=null) : RecyclerView.Adapter<search_recycler.myhold>() {

    val Activity:home

    init {
        Activity=c.Activity!!
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): myhold {
        if (c.lifecycleOwnerr.mynamee.filter()!=name)
            c.grab(c.lifecycleOwnerr.mynamee.filter())
        return myhold(LayoutInflater.from(c.Activity).inflate(R.layout.search_name,parent,false))
    }

    override fun getItemCount(): Int {
            return list.size
    }

    override fun onBindViewHolder(holder: myhold, position: Int) {
        list.apply {
            Activity.apply {
        holder.itemView.apply {
            name?.let {
               val myname= SpannableString(get(position)["name"].toString()).apply {
                    setSpan(ForegroundColorSpan(Color.RED), if (indexOf(it)>-1) indexOf(it) else 0, indexOf(it)+it.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
                }

                search_name.text=myname

            }

               get(position)["country"]?.let {
                   country.text=resources.getStringArray(R.array.countrys)[it.toString().toInt()]
              }

                mytools.glide(c.Activity!!.applicationContext,get(position)["picture"],product_picture)
                anim( holder.itemView,R.anim.recycler)

            setOnClickListener {
                view.NavController.navigate(R.id.action_profilee_to_post22,bundleOf("path" to list[position]["path"]))
            }
        }
        }
    }
    }

    inner class myhold(v: View):RecyclerView.ViewHolder(v)
}