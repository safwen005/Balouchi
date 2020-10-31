package com.example.balouchi.recycler

import android.R.attr.fragment
import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.os.Build
import android.transition.ChangeTransform
import android.transition.Fade
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.recyclerview.widget.RecyclerView
import com.example.balouchi.*
import com.example.balouchi.data.repository.login.user.product.product_data
import com.example.balouchi.ui.home.home
import com.example.balouchi.ui.product.product
import com.example.balouchi.util.go
import com.example.balouchi.util.gone
import com.example.balouchi.util.log
import kotlinx.android.synthetic.main.categories_recycler.view.*
import kotlinx.android.synthetic.main.fragment_news.*
import kotlinx.android.synthetic.main.fragment_news.view.*
import kotlinx.android.synthetic.main.fragment_news.view.recycler
import kotlinx.android.synthetic.main.item.view.*


class items_recycler(var c:Context,var array:ArrayList<product_data?>) : RecyclerView.Adapter<items_recycler.holder>() {

    val Activity=(c as home)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): items_recycler.holder {
          return holder(LayoutInflater.from(c).inflate(R.layout.item,parent,false))
    }

    override fun getItemCount(): Int {
        return array.size
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: items_recycler.holder, position: Int) {

        array[position]?.let {
            holder.itemView.apply {
               name.text=it.name

               it.Location?.adress?.let {
                   location.text=it
               }

                price.text=it.price+" دينار "

                vue.text=it.vue.toString()

                setOnClickListener {v ->
                    Activity.view.NavController.navigate(R.id.action_news_to_product, bundleOf("path" to array[position]!!.path))
                }
                it.picture?.apply {
                    Activity.mytools.glide(c.applicationContext,this,img,gif)
                    return
                }
                img.setBackgroundResource(R.drawable.ic_image_not_found_black)
                gif.gone()
            }
        }
    }

    inner class holder(var v:View):RecyclerView.ViewHolder(v)
}