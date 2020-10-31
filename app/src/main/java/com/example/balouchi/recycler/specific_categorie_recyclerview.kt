package com.example.balouchi.recycler

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.os.bundleOf
import androidx.recyclerview.widget.RecyclerView
import com.example.balouchi.R
import com.example.balouchi.data.repository.login.user.product.product_data
import com.example.balouchi.ui.home.home
import com.example.balouchi.util.*
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdLoader
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.formats.MediaView
import com.google.android.gms.ads.formats.UnifiedNativeAd
import com.google.android.gms.ads.formats.UnifiedNativeAdView
import com.google.android.material.card.MaterialCardView
import kotlinx.android.synthetic.main.ad.view.*
import kotlinx.android.synthetic.main.second_item.view.*

class specific_categorie_recyclerview (var c:Context,var array:ArrayList<product_data?>): RecyclerView.Adapter<RecyclerView.ViewHolder>()  {

    val Activity:home

    var u: UnifiedNativeAd?=null

    init {
       Activity=(c as home)
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RecyclerView.ViewHolder  {
        array[viewType]?.let {
            return myhold(LayoutInflater.from(c).inflate(R.layout.second_item,parent,false))
        }
        return ad(LayoutInflater.from(c).inflate(R.layout.ad_container, parent, false))
    }
    override fun getItemCount(): Int {
        return array.size
    }
    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder:RecyclerView.ViewHolder, position: Int) {
        Activity.apply {
        if (holder is myhold) {
                array.get(position)?.apply {
                    holder.itemView.apply {
                        c.anim(this,R.anim.search_anim)
                        nname.text = name
                        llocation.text = Location!!.adress
                        pprice.text =price+" دينار "
                        setOnClickListener { v ->
                            view.NavController.navigate(R.id.action_specific_categorie_to_product,
                                bundleOf("path" to path!!))
                        }
                        picture?.let{
                            mytools.glide(c.applicationContext, it, iimg, gif)
                            return
                        }
                            iimg.svg(R.drawable.ic_image_not_found)
                            gif.gone()
                    }
                }
            return
        }
            refresh(holder.itemView as MaterialCardView,u)
        }
    }

    inner class myhold(v: View):RecyclerView.ViewHolder(v)

    inner class ad(var v: View) : RecyclerView.ViewHolder(v)



}