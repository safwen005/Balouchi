package com.example.balouchi.recycler

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.balouchi.R
import com.example.balouchi.ui.conversation.conversation
import com.example.balouchi.data.repository.login.user.chat.Conversation
import com.example.balouchi.data.repository.login.user.tools
import com.example.balouchi.util.gone
import com.example.balouchi.util.isVisible
import com.example.balouchi.util.visibile
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.img_me.view.*
import kotlinx.android.synthetic.main.img_me.view.img_date
import kotlinx.android.synthetic.main.img_me.view.photo
import kotlinx.android.synthetic.main.img_you.view.*
import kotlinx.android.synthetic.main.text_me.view.*
import kotlinx.android.synthetic.main.text_me.view.date
import kotlinx.android.synthetic.main.text_me.view.texting
import kotlinx.android.synthetic.main.text_you.view.*
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

class chating_recycler(var array: ArrayList<Conversation>) :RecyclerView.Adapter<RecyclerView.ViewHolder>(){

    val user:FirebaseAuth
    val simpleDateFormat:DateFormat
    val calendar:Calendar
    val tools:tools

    init {
        user=FirebaseAuth.getInstance()
        calendar=Calendar.getInstance()
        simpleDateFormat=SimpleDateFormat.getInstance()
        simpleDateFormat.timeZone=TimeZone.getTimeZone("GMT")
        tools= tools()
    }


    override fun getItemViewType(position: Int): Int {

        array[position].img?.let {
            if (array[position].sender==user.uid){
                return 0
            }
            return 1
        }
        if (array[position].sender==user.uid){
            return 2
        }
        return 3
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        val inf=LayoutInflater.from(parent.context)
        return when(viewType){
            0 -> img_me(inf.inflate(R.layout.img_me, parent, false))
            1 -> img_you(inf.inflate(R.layout.img_you, parent, false))
            2 -> text_me(inf.inflate(R.layout.text_me, parent, false))
            else ->  text_you(inf.inflate(R.layout.text_you, parent, false))
        }
    }

    override fun getItemCount(): Int = array.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        when(getItemViewType(position)){
            0 -> {
                (holder as img_me).itemView.apply {
                    tools.glide(context.applicationContext, array[position].img, photo)
                    img_date.text = time(array[position].date!!)
                    holder.itemView.setOnClickListener {
                        time_0.apply {
                            if (isVisible())
                                gone()
                            else visibile()
                        }
                    }
                }
            }
            1 -> {
                (holder as img_you).itemView.apply {
                    tools.glide(context.applicationContext, array[position].img, photo)
                    img_date.text = time(array[position].date!!)
                    holder.itemView.setOnClickListener {
                        time_1.apply {
                            if (isVisible())
                                gone()
                            else visibile()
                        }
                    }
                }
            }
            2 -> {
                (holder as text_me).itemView.apply {
                        texting.text = array[position].message
                        date.text = time(array[position].date!!)
                        holder.itemView.setOnClickListener {
                            time_2.apply {
                                if (isVisible())
                                    gone()
                                else visibile()
                            }
                        }
                }
            }
            3 -> {
                (holder as text_you).itemView.apply {
                    texting.text = array[position].message
                    date.text = time(array[position].date!!)
                    holder.itemView.setOnClickListener {
                        time_3.apply {
                            if (isVisible())
                                gone()
                            else visibile()
                        }
                    }
                }
             }
          }
       }



    inner class img_me(v: View):RecyclerView.ViewHolder(v)
    inner class img_you(v: View):RecyclerView.ViewHolder(v)
    inner class text_me(v: View):RecyclerView.ViewHolder(v)
    inner class text_you(v: View):RecyclerView.ViewHolder(v)

    fun time(time:Long):String{
        calendar.timeInMillis = time
        calendar.add(Calendar.HOUR,1)
        return simpleDateFormat.format(calendar.time)
    }

}
