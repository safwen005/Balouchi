package com.example.balouchi.recycler

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.recyclerview.widget.RecyclerView
import com.example.balouchi.R
import com.example.balouchi.data.repository.login.user.chat.last
import com.example.balouchi.data.repository.login.user.tools
import com.example.balouchi.ui.home.home
import com.example.balouchi.util.font
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.chatt.view.*
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class Chat_recycler(var c: Context, var list:ArrayList<last>) : RecyclerView.Adapter<Chat_recycler.myhold>() {





    val date_format:DateFormat
    val calendar:Calendar

    init {
        calendar = Calendar.getInstance()
        date_format=SimpleDateFormat.getInstance()
        date_format.timeZone=TimeZone.getTimeZone("GMT")
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): myhold {
        return myhold(LayoutInflater.from(c).inflate(R.layout.chatt,parent,false))
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: myhold, position: Int) {
        val user=FirebaseAuth.getInstance().currentUser!!.uid

        holder.itemView.apply {
            list[position].apply {
                if (picture==null)
                    profile_image.setBackgroundResource(R.drawable.ic_avatar)
                    else tools().glide(c.applicationContext,picture,profile_image)
                    name.text=username
            lasts?.apply {
                content.text=message ?: if (sender==user) "أرسلت صورة !" else "أرسل لك صورة !"
                date?.let {
                    calendar.timeInMillis = it
                    calendar.add(Calendar.HOUR,1)
                }
                time.text = date_format.format(calendar.time)
                    if (!vue!! && list[position].lasts?.sender!=user)
                        content.setTextColor(ContextCompat.getColor(context, R.color.green))
            }

            c.font(name,content)

            setOnClickListener {
                (c as home).view.NavController.navigate(R.id.action_message_to_conversation,
                    if (list.size>0) bundleOf("data" to list[position]) else null)
            }
                    online?.let {
                        disponible.setBackgroundResource(if (it) R.drawable.disponible else R.drawable.offline)
                    }
            }
        }

    }


    inner class myhold(v: View):RecyclerView.ViewHolder(v)
}