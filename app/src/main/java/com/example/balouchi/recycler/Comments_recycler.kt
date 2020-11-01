package com.example.balouchi.recycler

import android.app.Activity
import android.util.Log
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.PopupMenu
import androidx.core.os.bundleOf
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import androidx.transition.AutoTransition
import androidx.transition.TransitionManager
import com.example.balouchi.R
import com.example.balouchi.data.repository.login.user.product.Products
import com.example.balouchi.data.repository.login.user.product.comments
import com.example.balouchi.data.repository.login.user.product.product_data
import com.example.balouchi.data.repository.login.user.product.product_owner
import com.example.balouchi.ui.home.home
import com.example.balouchi.ui.product.Product_Viewmodel
import com.example.balouchi.util.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.comments_layout.view.*
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap


class Comments_recycler(var p: Product_Viewmodel, var comments: ArrayList<comments>): RecyclerView.Adapter<Comments_recycler.myhold>(){

    val c:home
    var view:View?=null
    val calendar:Calendar
    val simple:DateFormat
    val firestore:FirebaseFirestore
    val auth:FirebaseAuth
    var popup:PopupMenu?=null
    lateinit var data:comments

    init {
        c=p.Activity
        calendar= Calendar.getInstance()
        simple=SimpleDateFormat.getInstance()
        firestore= FirebaseFirestore.getInstance()
        auth=FirebaseAuth.getInstance()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Comments_recycler.myhold {
        return myhold(LayoutInflater.from(c).inflate(R.layout.comments_layout, parent, false))
    }

    override fun getItemCount(): Int {
      return comments.size
    }

    override fun onBindViewHolder(holder: Comments_recycler.myhold, position: Int) {

     comments[position].apply {
               data=this
               (profile as? HashMap<*,*>)?.apply {
                holder.itemView.apply {
                 if (get("picture")==null)
                     profile_image.setBackgroundResource(R.drawable.ic_avatar)
                 else  c.mytools.glide(c.application, get("picture"),profile_image,gif)

                   c.anim(this, R.anim.show)
               name.text=get("username").toString()
                   date?.toLong()?.let {
                       calendar.timeInMillis=it
                       ddate.text=simple.format(calendar.time)
                   }
               rate.rating=rating!!
               ccomment.text=comment
               if (get("uid")==c.auth.currentUser!!.uid)
                   settings.visibile()
                   profile_image.setOnClickListener {
                       c.view.NavController.navigate(
                           R.id.action_product_to_profilee, bundleOf(
                               "uid" to get("uid")
                           )
                       )
                   }


                   theupdate.setOnClickListener {
                               wait_update.visibile()
                               theupdate.gone()
                                data= comments(
                                   c.auth.currentUser!!.uid,
                                   therating.rating,
                                   "2021",
                                   thecomment.text.toString().trim()
                               )
                               comments[position]=data
                               c.manageUser.Function<Any>(
                                   "upcomment", hashMapOf(
                                       "path" to p.product.path,
                                       "position" to position,
                                       "comment" to
                                               ToMap(
                                                   data
                                               )
                                   )
                               )
                                   .observe(p.lifecycleOwner, Observer {
                                       wait_update.gone()
                                       theupdate.visibile()
                                       it?.let {
                                           view=null
                                           c.toastg("تم التغيير بنجاح !")
                                           c.anim(update, R.anim.hide) {
                                               update.gone()
                                               commentt.visibile()
                                               rate.rating= data.rating!!
                                               ccomment.text=data.comment
                                               ddate.text=data.date
                                               settings.svg(R.drawable.ic_settings_mine)
                                               c.anim(commentt, R.anim.show_frame)
                                               c.ad()
                                           }

                                           return@Observer
                                       }
                                       c.toastg("مشكلة ، حاول مجددا !")
                                   })
                   }

                   settings.setOnClickListener {
                       if (commentt.isVisible()){
                           if (popup==null){
                           popup = PopupMenu(c, it)
                           popup?.getMenuInflater()
                               ?.inflate(R.menu.update_delete, popup?.getMenu())
                           popup?.setOnMenuItemClickListener(object : PopupMenu.OnMenuItemClickListener {
                               override fun onMenuItemClick(item: MenuItem): Boolean {
                                   when(item.itemId){
                                       R.id.delete  -> {
                                               c.anim(commentt,R.anim.hide) {
                                                   commentt.invisibile()
                                                   wait.visibile()
                                                               comments[position].profile=auth.uid
                                                               firestore.collection("products").document(p.product.path!!).update(
                                                                  "comments",FieldValue.arrayRemove(comments[position])
                                                               ).addOnCompleteListener {
                                                                   if ((it.isSuccessful)){
                                                                       gone()
                                                                       p.delete_item(position)
                                                                       visibile()
                                                                       commentt.visibile()
                                                                       wait.invisibile()
                                                                       c.toastg("تم الحذف بنجاح !")
                                                                       return@addOnCompleteListener
                                                                   }
                                                                   commentt.visibile()
                                                                   wait.invisibile()
                                                                   c.toastg("مشكلة ، حاول مجددا !")
                                                               }
                                               }
                                       }
                                       R.id.update -> {
                                           view?.apply {
                                               c.anim(update,R.anim.hide){
                                                   update.gone()
                                                   commentt.visibile()
                                                   settings.svg(R.drawable.ic_settings_mine)
                                                   c.anim(commentt,R.anim.show_frame)
                                               }
                                           }
                                           view=holder.itemView
                                               c.anim(commentt,R.anim.hide){
                                                   update.visibile()
                                                   thecomment.setText(ccomment.text.toString())
                                                   settings.svg(R.drawable.ic_cancel_location)
                                                   therating.rating=rate.rating
                                                   commentt.gone()
                                                   c.anim(update,R.anim.show_frame)
                                                   max(thecomment,max=2)
                                           }
                                       }
                                   }
                                   return true
                               }
                           })
                           }
                           popup?.show()
                           return@setOnClickListener
                       }
                           view=null
                           c.anim(update,R.anim.hide){
                               update.gone()
                               commentt.visibile()
                               settings.svg(R.drawable.ic_settings_mine)
                               c.anim(commentt,R.anim.show_frame)
                           }

               }
           }
       }
    }

    }

    inner class myhold(v: View):RecyclerView.ViewHolder(v)
}