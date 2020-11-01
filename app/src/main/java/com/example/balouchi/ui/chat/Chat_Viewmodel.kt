package com.example.balouchi.ui.chat

import android.app.AlertDialog
import android.media.MediaPlayer
import androidx.core.content.res.ResourcesCompat
import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.balouchi.R
import com.example.balouchi.data.repository.login.user.chat.Conversation
import com.example.balouchi.data.repository.login.user.chat.last
import com.example.balouchi.recycler.Chat_recycler
import com.example.balouchi.ui.home.home
import com.example.balouchi.util.*
import com.google.firebase.auth.FirebaseAuth
import android.os.*
import com.example.balouchi.data.repository.login.user.chat.myonline
import com.example.balouchi.data.repository.login.user.user.user_data
import com.google.firebase.firestore.*
import kotlinx.android.synthetic.main.chat.*
import kotlinx.android.synthetic.main.chat_empty.*
import kotlin.time.ExperimentalTime


class Chat_Viewmodel: ViewModel() {

    lateinit var lifecycleOwner: chat

    lateinit var Activty: home

    var dialog: AlertDialog? = null

    var mylast = ArrayList<last>()

    var list = ArrayList<ListenerRegistration>()

    @ExperimentalTime
    val myrun: (Handler, String, Int) -> Runnable = { handler, uid, pos ->
        object : Runnable {
            override fun run() {
                if (moves[pos].online) {
                    check_online(handler, uid) {
                        mylast[pos].online = false
                        moves[pos].online = false
                        lifecycleOwner.chat_recycler.adapter.notify(0, mylast.size, false)
                    }
                    return
                }
                handler.removeCallbacks(this)
            }
        }
    }


    var online = ArrayList<ListenerRegistration>()

    lateinit var firestore: FirebaseFirestore

    lateinit var myauth: FirebaseAuth

    val moves = ArrayList<myonline>()

    fun last_message(path: String): CollectionReference {
        return firestore.collection("chat/" + path + "/messages")
    }

    fun test_online(uid: String): DocumentReference {
        return firestore.document("users/" + uid)
    }

    @ExperimentalTime
    fun prepare() {
        log("chat prepare")
            lifecycleOwner.apply {
                (requireActivity() as home).apply {
                    save_chat("no")
                    Activty = this
                    dialog = getSpots()
                    firestore = FirebaseFirestore.getInstance()
                    myauth = FirebaseAuth.getInstance()
                    mylast.clear()
                    list.clear()

                    chat_recycler.apply {

                        layoutManager =
                            LinearLayoutManager(Activty, RecyclerView.VERTICAL, false)
                        adapter = Chat_recycler(Activty, mylast,{lifecycleOwner.onDestroy()})


                        nothing.typeface = ResourcesCompat.getFont(Activty, R.font.et)

                        val dialog = getSpots()
                        dialog.show()


                        Activty.manageUser.Function<Any>(
                            "thelasts",
                            hashMapOf("uid" to auth.currentUser?.uid)
                        ).observe(lifecycleOwner,
                            {
                                dialog.dismiss()
                                mylast.clear()
                                if (it is Boolean) {
                                    lifecycleOwner.empty.visibile()
                                    lifecycleOwner.chat_recycler.gone()
                                    return@observe
                                }
                                if (it is ArrayList<*>) {
                                    it.forEach { mylast.add(fromJson(it, last::class)) }
                                    mylast.sortWith(compareByDescending { it.lasts?.date })
                                    adapter = Chat_recycler(Activty, mylast,{lifecycleOwner.onDestroy()})
                                    mylast.forEach {
                                        moves.add(
                                            myonline(
                                                it.online!!,
                                                myrun(Handler(), it.uid!!, mylast.indexOf(it))
                                            )
                                        )
                                    }
                                }

                                val new = ArrayList<Conversation>()

                                mylast.forEach { thelast ->
                                    val pos = mylast.indexOf(thelast)
                                    if (thelast.online!!) {
                                        moves[pos].online = true
                                        moves[pos].run.run()
                                        adapter.notify(pos, mylast.size, false)
                                    }
                                    online.add(test_online(thelast.uid!!).addSnapshotListener { value, error ->
                                        value?.toObject(user_data::class.java)?.apply {
                                            lastlogin?.let {
                                                if (it != thelast.lastlogin) {
                                                    if (!thelast.online!!) {
                                                        mylast[pos].lastlogin = it
                                                        thelast.online = true
                                                        moves[pos].online = true
                                                        moves[pos].run.run()
                                                        adapter.notify(pos, mylast.size, false)
                                                        return@addSnapshotListener
                                                    }
                                                }
                                                return@addSnapshotListener
                                            }
                                            if (thelast.online!!){
                                                thelast.online = false
                                                moves[pos].online = false
                                                adapter.notify(pos, mylast.size, false)
                                            }

                                        }
                                    })



                                    list.add(last_message(thelast.path!!).addSnapshotListener { value, error ->
                                        value?.documents?.apply {
                                            forEach {
                                                it.toObject(Conversation::class.java)?.let {
                                                    new.add(it)
                                                }
                                            }
                                            new.sortWith(compareBy { it.date })

                                            if (new.size > 0) {
                                                if (new.last() != thelast.lasts) {
                                                    thelast.lasts = new.last()
                                                    mylast.sortWith(compareByDescending { it.lasts?.date })

                                                    adapter = Chat_recycler(Activty, mylast,{lifecycleOwner.onDestroy()})
                                                    MediaPlayer.create(Activty, R.raw.juntos)
                                                        .start()
                                                }
                                                new.clear()
                                            }
                                        }
                                    })
                                }

                            })
                    }

                }
            }
    }
}



