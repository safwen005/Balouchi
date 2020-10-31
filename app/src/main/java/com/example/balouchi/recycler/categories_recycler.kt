package com.example.balouchi.recycler

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import androidx.core.os.bundleOf
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.balouchi.R
import com.example.balouchi.ui.home.home
import com.example.balouchi.util.invisibile
import com.example.balouchi.util.refresh
import com.example.balouchi.util.visibile
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdLoader
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.formats.MediaView
import com.google.android.gms.ads.formats.UnifiedNativeAd
import com.google.android.gms.ads.formats.UnifiedNativeAdView
import com.google.android.material.card.MaterialCardView
import kotlinx.android.synthetic.main.ad.view.*
import kotlinx.android.synthetic.main.categories_recycler.view.*


class categories_recycler(var c:Context, var array:ArrayList<categories?>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var u:UnifiedNativeAd?=null
    override fun getItemViewType(position: Int): Int {
        return position
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        array[viewType]?.let {
            return myholder(LayoutInflater.from(c).inflate(R.layout.categories_recycler, parent, false))
        }
            return ad(LayoutInflater.from(c).inflate(R.layout.ad_container, parent, false))
    }

    override fun getItemCount(): Int = array.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        if (holder is myholder ){
            holder.itemView.apply {
                categorie_name.text=array[position]?.categorie
                recyr.layoutManager=LinearLayoutManager(c,RecyclerView.HORIZONTAL,true)
                recyr.adapter=array[position]?.recycler
                watch__all.setOnClickListener {
                    (c as home).view.NavController.navigate(R.id.action_news_to_specific_categorie,
                        bundleOf("categorie" to (array.filter { it!=null }.indexOf(array[position]))+1))
                }
            }
            return
        }
        else
             c.refresh(holder.itemView as MaterialCardView,u)

        }


    inner class myholder(var v: View) : RecyclerView.ViewHolder(v)

    inner class ad(var v: View) : RecyclerView.ViewHolder(v)




}
