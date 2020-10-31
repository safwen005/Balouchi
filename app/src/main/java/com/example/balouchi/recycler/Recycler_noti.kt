package com.example.balouchi.recycler

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.core.os.bundleOf
import androidx.recyclerview.widget.RecyclerView
import com.example.balouchi.R
import com.example.balouchi.data.repository.login.user.chat.last
import com.example.balouchi.data.repository.login.user.user.Notificationn
import com.example.balouchi.data.repository.login.user.user.user_data
import com.example.balouchi.ui.home.home
import com.example.balouchi.ui.notification.notification
import com.example.balouchi.util.ToMap
import com.example.balouchi.util.anim
import com.example.balouchi.util.getSpots
import com.example.balouchi.util.log
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.noti.view.*
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList
import kotlin.time.ExperimentalTime
import kotlin.time.milliseconds


class Recycler_noti(var c: Context,val list:ArrayList<Notificationn?>,var anim:Boolean=false,val noti:notification) : RecyclerView.Adapter<Recycler_noti.myhold>() {

    val calendar:Calendar
    val simple:DateFormat
    val firestore:FirebaseFirestore

    init {
        calendar=Calendar.getInstance()
        simple=SimpleDateFormat.getInstance()
        simple.timeZone= TimeZone.getTimeZone("GMT")
        firestore= FirebaseFirestore.getInstance()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): myhold {
        return myhold(LayoutInflater.from(c).inflate(R.layout.noti,parent,false))
    }

    override fun getItemCount(): Int {
        return list.size
    }

    @ExperimentalTime
    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: myhold, position: Int) {

        list[position]!!.apply {
                holder.itemView.apply {
                    if (type==null){
                        content.apply {
                            setTextColor(ResourcesCompat.getColor(resources,R.color.green,null))
                            text="مرحبا بك !"
                        }
                        img.setBackgroundResource(R.drawable.ic_face_smile)
                        return
                    }

                    content.text=if (type!!) "لديك تعليق جديد من : " else "لديك رسالة من : "
                    list[position]?.date?.let {
                        calendar.timeInMillis =it
                    }
                    calendar.add(Calendar.HOUR,1)
                    date.text=simple.format(calendar.time)

                    if (anim){
                        if (position==0){
                            root.setBackgroundResource(R.drawable.layout_toolbar)
                            c.anim(root,R.anim.show_frame)
                        }
                    }

                    if (picture==null)
                        img.setBackgroundResource(R.drawable.ic_avatar)
                    else (c as home).mytools.glide(c.applicationContext,picture,img)

                    if (!path!!.contains("/")){
                       firestore.document("users/"+FirebaseAuth.getInstance().currentUser?.uid).get().addOnSuccessListener {
                            it.toObject(user_data::class.java)?.let {
                                user ->

                                username.text=user.personal_info?.username
                                setOnClickListener {
                                    c.getSpots().apply {
                                        show()
                                            dismiss()
                                            (c as home).view.NavController.navigate(
                                                R.id.action_message_to_conversation,
                                                bundleOf(
                                                    "data" to last(
                                                        picture,
                                                        sender,
                                                        user.lastlogin,
                                                        path = path,
                                                        token = user.token,
                                                        uid = user.uid,
                                                        online =
                                                        if (user.lastlogin == null) false else (((Timestamp.now().toDate().time.milliseconds.toLongMilliseconds()) - user.lastlogin!!) <= 60000)
                                                    )
                                                )
                                            )
                                    }
                                }
                            }
                        }
                        return
                    }

                    setOnClickListener {
                            (c as home).view.NavController.navigate(R.id.action_message_to_product, bundleOf("path" to path))
                    }

                    username.text=sender

             }
         }

    }

    inner class myhold(v: View):RecyclerView.ViewHolder(v)
}