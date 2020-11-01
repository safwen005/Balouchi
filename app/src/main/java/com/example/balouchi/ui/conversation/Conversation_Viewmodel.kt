package com.example.balouchi.ui.conversation

import android.app.AlertDialog
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.view.View
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.balouchi.R
import com.example.balouchi.data.repository.login.user.chat.Conversation
import com.example.balouchi.data.repository.login.user.chat.last
import com.example.balouchi.data.repository.login.user.user.user_data
import android.os.*
import com.example.balouchi.data.repository.login.user.user.Notificationn
import com.example.balouchi.data.repository.login.user.user.storage
import com.example.balouchi.recycler.chating_recycler
import com.example.balouchi.ui.home.home
import com.example.balouchi.util.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.Query
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask
import kotlinx.android.synthetic.main.conversation.*
import kotlinx.android.synthetic.main.conversation_toolbar.view.*
import java.io.ByteArrayOutputStream
import java.util.logging.Handler
import kotlin.time.ExperimentalTime

class Conversation_Viewmodel : ViewModel() {

    lateinit var lifecycleOwner: conversation

    lateinit var Activity:home

    lateinit var Firestore:FirebaseFirestore

    lateinit var path: String


    lateinit var mylast:last

    lateinit var hanlder:android.os.Handler

    lateinit var myrun:(android.os.Handler,String)->Runnable

    var move=true

    var mydialog:AlertDialog?=null
    var list = ArrayList<Conversation>()
    var Listener: ListenerRegistration?=null
    val mylist = ArrayList<Conversation>()
    var online:ListenerRegistration?=null
    var enough = false
    var stop = true
    var picture:String?=null
    lateinit var mystorage_camera: storage
    lateinit var myusername:String
    lateinit var image:StorageReference
    var random:String?=null

    @ExperimentalTime
    fun prepare(){
        lifecycleOwner.apply {
            (requireActivity() as home).apply {
                Activity=this
                mydialog=getSpots()
                Firestore=FirebaseFirestore.getInstance()
                max(lifecycleOwner.msgg, max = 6)
                manage_edit(edit = lifecycleOwner.msgg,a = {
                    it?.apply{
                        if (isEmpty())
                        {
                            if (!all.isVisible()){
                                all.visibile()
                                anim(all,R.anim.show)
                            }
                            return@manage_edit
                        }
                        if (all.isVisible()){
                            anim(all,R.anim.hide){
                                all.gone()
                            }
                        }
                    }
                })
                (mytool as Toolbar).apply {
                    navigationIcon =svg(R.drawable.ic_back)
                    setNavigationOnClickListener {
                        onBackPressed()
                    }
                    mydialog!!.show()
                    Firestore.document("users/"+auth.currentUser?.uid).get().addOnSuccessListener {
                        it.toObject(user_data::class.java)?.let {
                            picture=it.personal_info?.ppicture?.path
                            myusername=it.personal_info?.username!!
                        }
                    prepare_data()
                    scroll()
                    }
                }
            }

        }
    }

    fun Click(view:View){
        Activity.apply {
        lifecycleOwner.apply {
        when(view.id){
            R.id.photoo -> {
                photoo.gone()
                others.visibile()
                anim(others,R.anim.show)
            }

            R.id.mycamera -> {
                if (camera_storage==null)
                    camera_storage=mystorage_camera
                camera_storage!!.start(false)
            }

            R.id.storagee -> {
                if (camera_storage==null)
                    camera_storage=mystorage_camera
                camera_storage!!.start(true)
            }

            R.id.send -> {
                if (msgg.filter().isNotEmpty()) {
                    wait_send.visibile()
                    send.gone()

                    val conv = Conversation(
                        message = msgg.filter(),
                        sender = auth.currentUser!!.uid,
                        vue = false,
                        token =mylast.token,
                        username=myusername
                    )

                    val data= hashMapOf(
                        "path" to path, "conversation" to ToMap(conv)

                    ,"noti" to ToMap(Notificationn(
                            myusername,
                            picture = picture,
                            path = path,
                        )),"receiver" to mylast.uid!!,
                        "online" to mylast.online!!)

                    msgg.setText("")
                    if (empty.isVisible())
                        empty.gone()

                    manageUser.Function<Boolean>(
                        "sendit",data
                    ).observe(viewLifecycleOwner,
                        {
                            if (it) {
                                all.visibile()
                                anim(all,R.anim.show)
                                wait_send.gone()
                                send.visibile()
                            }
                        })
                }

            }
        }
     }
   }
 }

    fun Display(test:Boolean,data: Intent?){
        Activity.apply {
        lifecycleOwner.apply {
            data?.let {
                upload_picture(if (test) it.data else (it.extras?.get("data") as Bitmap)).addOnSuccessListener {
                    image.downloadUrl.addOnSuccessListener {
                        val conv = Conversation(
                            img =it.toString() ,
                            sender = FirebaseAuth.getInstance().currentUser!!.uid,
                            vue = false,
                            path = path,
                            token =mylast.token,
                            username=myusername
                        )

                        val mydata= hashMapOf("path" to path, "conversation" to ToMap(conv))
                        mydata.put("noti",ToMap(Notificationn(
                            myusername,
                            picture = picture,
                            path = path,
                        )))

                        mydata.put("receiver",mylast.uid!!)

                        manageUser.Function<Boolean>(
                            "sendit",mydata
                        ).observe(viewLifecycleOwner,
                            {
                                others.gone()
                                photoo.visibile()
                                anim(photoo,R.anim.show)
                            })
                    }
                  }
               }
            }
        }
    }

    fun upload_picture(pic:Any?): UploadTask {
        Activity.apply {
            random= random()
            image= storage.getReference(mylast.path!!).child(random!!)
            image.apply {
                if (pic is Uri)
                    return putFile(pic)
                else {
                    val byte= ByteArrayOutputStream()
                    (pic as Bitmap).compress(Bitmap.CompressFormat.JPEG, 100,byte)
                    return putBytes(byte.toByteArray())
                }

            }
        }
    }



    @ExperimentalTime
    fun prepare_data(){
        Activity.apply {
        lifecycleOwner.apply {
            mystorage_camera= storage(lifecycleOwner,myroot)
            camera_storage=mystorage_camera
            (arguments?.getSerializable("data") as? last)?.let {
                save_chat(it.uid)
                mylast=it
            }
                list.clear()
                mylist.clear()
                mytool.client.text=mylast.username
                path = mylast.path!!
                if (mylast.picture!=null)
                mytools.glide(application, mylast.picture, mytool.profile_image)
                else  mytool.profile_image.setBackgroundResource(R.drawable.ic_avatar)
                rec.layoutManager =
                    LinearLayoutManager(Activity, RecyclerView.VERTICAL, false)
                rec.adapter = chating_recycler(list)

                    Firestore.collection("chat").document(path).collection("messages").orderBy("date",Query.Direction.DESCENDING).get().addOnSuccessListener {

                        try {
                        if (it.documents.isEmpty()){
                            enough = true
                            empty.visibile()
                        }
                        else {
                                it.documents.slice(0,3).forEach {
                                    list.add(0, it.toObject(Conversation::class.java)!!)
                                }
                                rec.adapter?.notify(0,it.documents.size)

                                list.last().apply {
                                    if (!vue!! && sender != auth.currentUser!!.uid) {
                                        Firestore.document(path!!)
                                            .update(hashMapOf("vue" to true) as Map<String, Any>)
                                    }
                                }
                        }

                    mydialog!!.dismiss()
                    more_messages.gone()
                    hanlder=Handler()
                    myrun= {
                        handler , uid ->
                        object:Runnable{
                            override fun run() {
                                if (move){
                                    check_online(hanlder,uid) {
                                        mylast.online=false
                                        move=false
                                        lifecycleOwner.mytool.disponible.setBackgroundResource(R.drawable.offline)
                                    }
                                    return
                                }
                                hanlder.removeCallbacks(this)
                            }
                        }
                    }
                    if (mylast.online!!)
                    {
                        mytool.disponible.setBackgroundResource(R.drawable.disponible)
                        anim(mytool.disponible,R.anim.show)
                        myrun(hanlder,mylast.uid!!).run()
                    }

                    online=Firestore.document("users/"+mylast.uid).addSnapshotListener {

                            value, error ->
                            value?.toObject(user_data::class.java)?.apply {
                                lastlogin?.let {
                                    if (it!=mylast.lastlogin){
                                        if (!mylast.online!!){
                                            mylast.lastlogin=it
                                            mylast.online=true
                                            move=true
                                            mytool.disponible.setBackgroundResource(R.drawable.disponible)
                                            anim(mytool.disponible,R.anim.show)
                                            myrun(hanlder,mylast.uid!!).run()
                                        }
                                    }
                                    return@addSnapshotListener
                                }
                                if (mylast.online!!){
                                    mylast.online=false
                                    move=false
                                    lifecycleOwner.mytool.disponible.setBackgroundResource(R.drawable.offline)
                                    anim(mytool.disponible,R.anim.show)
                                }

                            }
                    }

                    Listener = Firestore.collection("chat").document(path)
                        .collection("messages").addSnapshotListener { value, error ->
                            if (stop) {
                                mylist.clear()
                                value?.documents?.apply {
                                    forEach {
                                        it.toObject(Conversation::class.java)?.let {
                                            mylist.add(it)
                                        }
                                    }
                                }

                                mylist.sortWith(compareBy { it.date })
                                if (mylist.last().date!=list.last().date){
                                    mydialog!!.dismiss()
                                    list.add(mylist.last())

                                    rec.adapter?.apply {
                                        notify(itemCount,list.size)
                                    }

                                    rec.smoothScrollToPosition(list.size - 1)

                                    list.last().apply {
                                        if (sender != auth.currentUser?.uid) {
                                            Firestore.document(path!!)
                                                .update(hashMapOf("vue" to true) as Map<String, Any>)
                                        }
                                    }
                                }
                            }
                        }
                        }catch (e:Exception){
                            toastr("هنالك مشكلة !")
                            Activity.onBackPressed()
                        }

                }.addOnFailureListener {
                        toastr("هنالك مشكلة !")
                        Activity.onBackPressed()
                }

         }
       }
    }

    fun scroll(){
        lifecycleOwner.apply {
            Activity.apply {
                rec.addOnScrollListener(object : RecyclerView.OnScrollListener() {
                    override fun onScrollStateChanged(
                        recyclerView: RecyclerView,
                        newState: Int
                    ) {
                        if (!recyclerView.canScrollVertically(-1) && newState == RecyclerView.SCROLL_STATE_IDLE) {
                            if (!enough) {
                                if (stop) {
                                    stop = false
                                    more_messages.visibile()

                                        Firestore.collection("chat").document(path).collection("messages").orderBy("date",Query.Direction.DESCENDING).get().addOnSuccessListener {
                                        more_messages.gone()
                                        stop = true

                                        if (it.documents.size==list.size)
                                            enough = true
                                        else {

                                                it.documents.slice(list.size,list.size+3).forEach {
                                                    list.add(
                                                        0,
                                                         it.toObject(Conversation::class.java)!!)
                                                }

                                                rec.adapter?.apply {
                                                    notify(0,itemCount)
                                                }
                                        }

                                    }
                            }
                        }
                    }
                }
            })
        }
    }
 }
}