package com.example.balouchi.recycler

import android.content.Context
import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.recyclerview.widget.RecyclerView
import com.example.balouchi.R
import com.example.balouchi.ui.home.home
import com.example.balouchi.util.font
import com.example.balouchi.util.invisibile
import com.example.balouchi.util.underline
import kotlinx.android.synthetic.main.all_categorie_recycler.view.*

class all_categories_recycler(var c:Context, var array: Array<String>) : RecyclerView.Adapter<all_categories_recycler.holder>() {


    override fun getItemViewType(position: Int): Int {
        return position
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): all_categories_recycler.holder {

        return holder(LayoutInflater.from(c).inflate(R.layout.all_categorie_recycler, parent, false))

    }

    override fun getItemCount(): Int {
        return array.size+2
    }

    override fun onBindViewHolder(holder: all_categories_recycler.holder, position: Int) {
        holder.itemView.apply {
           if (position<array.size){
               namee.text=array[position]
               c.font(namee)
               namee.underline()
               setOnClickListener {
                   (c as home).view.NavController. navigate(R.id.action_all_categories_to_specific_categorie,
                       bundleOf("categorie" to position+1)
                   )
               }
               return
           }
            invisibile()
        }

    }

    inner class holder(var v:View):RecyclerView.ViewHolder(v)
}