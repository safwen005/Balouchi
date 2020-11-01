package com.example.balouchi.ui.notification

import android.app.AlertDialog
import android.media.MediaPlayer
import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.balouchi.R
import com.example.balouchi.data.repository.login.user.user.Notificationn
import com.example.balouchi.recycler.Recycler_noti
import com.example.balouchi.ui.home.home
import com.example.balouchi.util.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.Query
import kotlinx.android.synthetic.main.notification.*

class Notification_Viewodel :ViewModel() {

    lateinit var lifecycleOwner: notification

    lateinit var Activity: home

    var Listener: ListenerRegistration?=null

    var go = true

    lateinit var firestore: FirebaseFirestore


    lateinit var myauth: FirebaseAuth

    lateinit var adapter: Recycler_noti

    var dialog: AlertDialog? = null

    lateinit var list:ArrayList<Notificationn?>

    fun prepare() {
            (lifecycleOwner.requireActivity() as home).apply {
                Activity = this
                dialog = getSpots()
                firestore = FirebaseFirestore.getInstance()
                myauth = FirebaseAuth.getInstance()
                list=ArrayList()
            }

            lifecycleOwner.apply {
                list.clear()
                dialog!!.show()

                firestore.collection("notifications").document("all").collection(
                    myauth.currentUser!!.uid
                ).orderBy("date", Query.Direction.DESCENDING).get().addOnSuccessListener {

                    dialog!!.dismiss()

                    if (it.documents.size == 0) {
                        empty.visibile()
                        noti_recycler.gone()
                    } else {
                        if (it.documents.size < 5)
                            go = false
                        it.documents.slice(0, 5).forEach {
                            list.add(it.toObject(Notificationn::class.java))
                        }
                        noti_recycler.layoutManager =
                            LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
                        adapter = Recycler_noti(requireContext(), list, noti = lifecycleOwner)
                        noti_recycler.adapter = adapter
                    }

                    val new = ArrayList<Notificationn>()
                    Listener = firestore.collection("notifications/all/" + myauth.currentUser!!.uid)
                        .addSnapshotListener {
                                value, error ->
                            value?.documents?.apply {
                                if (size>0) {
                                    new.clear()
                                    forEach {
                                        it.toObject(Notificationn::class.java)?.let {
                                            new.add(it)
                                        }
                                    }
                                    new.sortWith(compareByDescending { it.date })

                                    if (list.isEmpty() || new[0].date != list[0]!!.date) {
                                        list.add(0, new[0])
                                        lifecycleOwner.noti_recycler?.adapter?.apply {
                                            notify(0, list.size, true)
                                            adapter.anim = true
                                        }
                                        Activity.sound()
                                    }
                                    }
                                }
                            }
                        }
                    noti_recycler.apply {
                        noti_recycler.addOnScrollListener(object : RecyclerView.OnScrollListener() {
                            override fun onScrollStateChanged(
                                recyclerView: RecyclerView,
                                newState: Int
                            ) {
                                if (!recyclerView.canScrollVertically(1) && newState == RecyclerView.SCROLL_STATE_IDLE) {
                                    if (go) {
                                        dialog!!.show()
                                        val pos = list.size

                                        firestore.collection("notifications").document("all")
                                            .collection(
                                                myauth.currentUser!!.uid
                                            ).orderBy("date", Query.Direction.DESCENDING).get()
                                            .addOnSuccessListener {
                                                if (it.documents.size > 0) {
                                                    it.documents.slice(pos, pos + 5).forEach {
                                                        list.add(it.toObject(Notificationn::class.java))
                                                    }
                                                    adapter?.apply {
                                                        notify(pos - 1, itemCount)
                                                        return@addOnSuccessListener
                                                    }
                                                }
                                                go = false

                                            }

                                        dialog!!.dismiss()
                                    }
                                }
                            }
                        })
                    }
                }
            }
        }




