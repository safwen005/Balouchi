package com.example.balouchi.recycler

import android.app.AlertDialog
import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import com.example.balouchi.R
import com.example.balouchi.data.repository.login.user.product.Pictures
import com.example.balouchi.data.repository.login.user.product.Products
import com.example.balouchi.ui.complete_post.complete_post
import com.example.balouchi.ui.home.home
import com.example.balouchi.util.*
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.complete_post.*
import kotlinx.android.synthetic.main.image.view.*


class images_adapter(var c: complete_post,var images:ArrayList<Any>,var mypath:String?=null) : RecyclerView.Adapter<images_adapter.myhold>() {

   var Alert:AlertDialog?=null
   val Activity:home
   val dialog:AlertDialog
   val firestore:FirebaseFirestore

    init {
        Activity=(c.requireActivity() as home)
        dialog=Activity.getSpots()
        firestore= FirebaseFirestore.getInstance()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): myhold {
        return myhold(LayoutInflater.from(Activity).inflate(R.layout.image,parent,false))
    }

    override fun getItemCount(): Int = images.size

    override fun onBindViewHolder(holder: myhold, position: Int) {

        holder.itemView.apply {
            Activity.apply {
                images[position].also {
                    mytools.glide(Activity,
                        when{
                            (it is Uri || it is Bitmap) -> it
                             it is Pictures -> it.path
                            else -> null
                        }
                        , myimage, gif)
            }

            cancel.setOnClickListener {
                mytest.gone()
                mytest2.visibile()
                anim(mytest2,R.anim.show)
            }

            setOnClickListener {
                if (mytest.isGone()){
                    mytest2.gone()
                    mytest.visibile()
                    anim(mytest,R.anim.show)
                }
            }

            remove.setOnClickListener {
                if (Alert==null)
                    Alert=otherTools.Alert(this, yes = {
                        if (images[position] is Uri || images[position] is Bitmap){
                            images.removeAt(position)
                            c.vieww.valid(test=false)

                            this@images_adapter.notify(position,images.size,null)

                            return@Alert
                        }
                        dialog.apply {
                            show()
                            mypath!!.apply {
                                  storage.getReference(substring(lastIndexOf("/")+1,length)+"/"+(images[position] as Pictures).key).delete()
                                 .addOnSuccessListener {
                                     firestore.collection("products").document(this).update(
                                                 "pictures",FieldValue.arrayRemove(images[position] as Pictures)
                                             ).addOnCompleteListener {
                                             mytest.gone()
                                             mytest2.visibile()
                                             anim(mytest2,R.anim.show)
                                             dismiss()
                                             if (it.isSuccessful){
                                                 toastg("تم الحذف بنجاح !")
                                                 images.removeAt(position)

                                                 this@images_adapter.notify(position,images.size,null)

                                                 return@addOnCompleteListener
                                             }
                                             toastr("حاول مجددا !")
                                       }
                                }
                                .addOnFailureListener {
                                    toastr("حاول مجددا !")
                                    dismiss()
                                }

                            }
                        }
                    })
                Alert!!.show()
            }

            }
        }
    }

    inner class myhold(v: View):RecyclerView.ViewHolder(v)
}