package com.example.balouchi.recycler

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.os.bundleOf
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import com.example.balouchi.R
import com.example.balouchi.data.repository.login.user.product.product_data
import com.example.balouchi.ui.home.home
import com.example.balouchi.ui.profile_items.Profileitems_ViewModel
import com.example.balouchi.ui.profile_items.profile_items
import com.example.balouchi.util.*
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdLoader
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.formats.MediaView
import com.google.android.gms.ads.formats.UnifiedNativeAd
import com.google.android.gms.ads.formats.UnifiedNativeAdView
import com.google.android.material.card.MaterialCardView
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.ad.view.*
import kotlinx.android.synthetic.main.second_item.view.*

class profile_recyclerview(val profile: Profileitems_ViewModel, var array:ArrayList<product_data?>, val mine:Pair<Boolean,Boolean>): RecyclerView.Adapter<RecyclerView.ViewHolder>()  {

    var Alert:AlertDialog?=null
    var Activity:home
    var dialog:AlertDialog
    var View:View?=null
    var u: UnifiedNativeAd?=null

    init {
        Activity=profile.Activity
        dialog=Activity.getSpots()
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RecyclerView.ViewHolder  {
        array[viewType]?.let {
            return myhold(LayoutInflater.from(Activity).inflate(R.layout.second_item,parent,false))
        }
        return ad(LayoutInflater.from(Activity).inflate(R.layout.ad_container, parent, false))
    }
    override fun getItemCount(): Int {
        return array.size
    }
    override fun onBindViewHolder(holder:RecyclerView.ViewHolder, position: Int) {
        if (holder is myhold) {
            Activity.apply {
                array.get(position)?.apply {
                 holder.itemView.apply {
                     if (picture!=null)
                         mytools.glide(Activity.applicationContext, picture, iimg, gif)
                     else {
                         iimg.svg(R.drawable.ic_image_not_found)
                         gif.gone()
                     }
                    nname.text = name
                    llocation.text = Location!!.adress
                    pprice.text = price

                    setOnClickListener { v ->
                        view.NavController.navigate(R.id.action_profilee_to_product,
                            bundleOf("path" to path!!))
                    }
                       remove.setOnClickListener {
                        if (Alert==null)
                            Alert=otherTools.Alert(Activity, yes = {
                                dialog.show()
                                manageUser.Function<Boolean>(
                                    "deleteprod",
                                    hashMapOf(
                                        "mine" to mine.first,
                                        "uid" to auth.currentUser!!.uid,
                                        "path" to path
                                    )
                                ).observe(profile.lifecycleOwner,
                                     {
                                        if (it){
                                            toastg("تم الحذف بنجاح !")
                                            dialog.dismiss()
                                            profile.refresh(position)
                                        }

                                    })

                            })
                       Alert!!.show()
                  }

                    edit.setOnClickListener {
                        view.NavController.navigate(R.id.action_profilee_to_post22,bundleOf("path" to array[position]!!.path))
                    }

                    cancel.setOnClickListener {
                        show_hide(true,change,thepic)
                        this@profile_recyclerview.View=null
                    }

                     if (mine.second){
                             setOnLongClickListener {
                                 if (thepic.isVisible()){
                                     View?.apply {
                                         show_hide(true, change, thepic).takeIf { !thepic.isVisible()}
                                     }
                                     this@profile_recyclerview.View=null
                                     show_hide(false,change,thepic,edit.takeIf { !mine.first })
                                 }
                                 return@setOnLongClickListener true
                        }
                     }
                  }
                }
            }
           return
        }
            Activity.refresh(holder.itemView as MaterialCardView,u)
    }

    inner class myhold(v: View):RecyclerView.ViewHolder(v)

    inner class ad(var v: View) : RecyclerView.ViewHolder(v)

    @SuppressLint("InflateParams")


    fun show_hide(show:Boolean,change:View,thepic:View,edit:View?=null){

        if (show){
            change.gone()
            thepic.visibile()
            Activity.anim(thepic,R.anim.show)
            return
        }
        thepic.gone()
        change.visibile()
        edit?.apply {
            gone()
        }
        Activity.anim(change,R.anim.slide_left)
    }




}