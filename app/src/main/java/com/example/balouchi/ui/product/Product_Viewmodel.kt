package com.example.balouchi.ui.product

import android.annotation.SuppressLint
import android.app.ActivityOptions
import android.app.AlertDialog
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Build
import android.view.View
import androidx.core.content.res.ResourcesCompat
import androidx.core.os.bundleOf
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.transition.TransitionManager
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.example.balouchi.MapsActivity
import com.example.balouchi.R
import com.example.balouchi.data.repository.login.user.chat.last
import com.example.balouchi.data.repository.login.user.product.Products
import com.example.balouchi.data.repository.login.user.product.comments
import com.example.balouchi.data.repository.login.user.user.Notificationn
import com.example.balouchi.data.repository.login.user.user.auth.manage_user
import com.example.balouchi.data.repository.login.user.user.user_data
import com.example.balouchi.images
import com.example.balouchi.recycler.Comments_recycler
import com.example.balouchi.ui.home.home
import com.example.balouchi.util.*
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.product.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap
import kotlin.time.ExperimentalTime
import kotlin.time.milliseconds


class Product_Viewmodel : ViewModel() {

    lateinit var lifecycleOwner: product
    lateinit var Product_View: View
    lateinit var Activity: home
    lateinit var path: String
    lateinit var product: Products
    var myComments = ArrayList<comments>()
    var dialog: AlertDialog? = null
    var calendar = Calendar.getInstance()
    lateinit var Firestore:FirebaseFirestore
    val simple = SimpleDateFormat.getInstance()
    var snake:Snackbar?=null

    @ExperimentalTime
    fun Click(V: View) {
        Activity.apply {
            when (V.id) {
                R.id.p_img -> {
                    (product.pictures?.size)?.let {
                        if (it>0){
                            var transition: ActivityOptions? = null
                            if (Build.VERSION.SDK_INT >= 21) {
                                p_img.transitionName = "img"
                                transition = ActivityOptions.makeSceneTransitionAnimation(
                                    this, android.util.Pair(p_img, "img")
                                )
                            }
                            startActivity(Intent(this, images::class.java).apply {
                                val pictures = ArrayList<String>().also { list ->
                                    product.pictures?.forEach {
                                        list.add(it.path!!)
                                    }
                                }
                                putStringArrayListExtra("pictures", pictures)
                            }, transition?.toBundle())

                        }
                    }

                }
                R.id.add_product -> view.NavController.navigate(R.id.action_product_to_post2)
                R.id.pprofile -> view.NavController.navigate(
                    R.id.action_product_to_profilee,
                    bundleOf("uid" to product.profile!!.uid)
                )
                R.id.opencc -> {
                    TransitionManager.beginDelayedTransition(
                        lifecycleOwner.comments_card,
                        null
                    )
                    comments_recycler.apply {
                        if (isVisible()) {
                            gone()
                            load_more.gone()
                            lifecycleOwner.opencc.svg(R.drawable.ic_up)
                            return@apply
                        }
                        load_more.visibile()
                        lifecycleOwner.opencc.svg(R.drawable.ic_down)
                        visibile()
                    }
                }
                R.id.load_more -> loadmore()
                R.id.add_favorite -> {
                    dialog!!.show()

                    Firestore.collection("products").document(auth.currentUser!!.uid).update(
                        "favorite",FieldValue.arrayUnion(product.path)
                    ).addOnCompleteListener {
                        log(it.result)
                        log(it.exception?.message)
                         dialog!!.dismiss()
                        if (it.isSuccessful){
                            toastg("تمت إضافة المنشور بنجاح !")
                            return@addOnCompleteListener
                        }
                        toastr("هنالك مشكلة")
                    }

                }

                R.id.add_comment -> {
                    lifecycleOwner.apply {
                        if (testinputs(comment)){
                            add_comment.gone()
                            mywait.visibile()

                            val time=Timestamp.now().toDate().time.milliseconds.toLongMilliseconds()
                            val cmnt = comments(
                                auth.currentUser!!.uid,
                                rating.rating,
                                time.toString(),
                                comment.filter()
                            )

                                Firestore.document("users/"+auth.currentUser?.uid).get().addOnSuccessListener {
                                it.toObject(user_data::class.java)?.let {
                                    val data=hashMapOf(
                                        "path" to path,
                                        "mycomment" to ToMap(cmnt),
                                        "username" to it.personal_info?.username,
                                        "token" to product.profile?.token
                                    )

                                        data.apply {
                                            put("noti",ToMap(Notificationn(
                                                it.personal_info?.username,
                                                true,
                                                picture = it.personal_info?.ppicture?.path,
                                                path = path
                                            )))
                                            put("receiver",product.profile?.uid)
                                        }

                                    manageUser.Function<Boolean>(
                                        "addcom", data
                                    ).observe(
                                        lifecycleOwner,
                                        {
                                            if (it) {
                                                mywait.gone()
                                                add_comment.visibile()
                                                if (myrating.isVisible()) {
                                                    myrating.gone()
                                                    anim(myrating, R.anim.hide)
                                                }
                                                toastg("تمت إضافة التعليق !")
                                                comment.setText("")
                                                rating.rating = 0f
                                                loadmore(true)
                                            }
                                     })
                                 }
                             }
                         }
                     }
                 }

                R.id.get_direction -> {
                    try {
                        product.Location.coord?.apply {
                            startActivity(Intent(Activity, MapsActivity::class.java).apply {
                                putExtra("lat", lat)
                                putExtra("lng", lng)
                            })
                            return
                        }
                        toastr("لا يوجد عنوان !")
                    } catch (e: Exception) {
                        toastr("هنالك مشكلة حاول مجددا !")
                    }
                }
                R.id.phonee -> {
                    if (permissions(android.Manifest.permission.CALL_PHONE)){
                        log("click")
                        call()
                        return
                    }
                    if (loadnever("call")!!) {
                            snake= prepare_snake(lifecycleOwner.myroot, "المكالمة")
                            snake!!.show()
                            return
                    }
                    request_permissions(android.Manifest.permission.CALL_PHONE)
                }
            }
        }
    }

    fun call(){
        Activity.apply {
        val intent = Intent(
            Intent.ACTION_CALL,
            Uri.parse("tel:" + Uri.encode(product.profile!!.phone!!).trim())
        )
        (intent.resolveActivity(packageManager))?.let {
            startActivity(intent)
            return@apply
        }
        toastr("لا يوجد تطبيق للأتصال !")
        }
    }

    fun loadmore(up: Boolean = false) {
        Activity.apply {
        lifecycleOwner.apply {
            if (!up) {
                load_more.gone()
                more_comments.visibile()
            }
            manageUser.Function<ArrayList<HashMap<*, *>>>(
                "getcomment", hashMapOf(
                    "path" to product.path,
                    "pos" to if (up) 0 else myComments.size,
                    "step" to if (up) 1 else 2
                )
            ).observe(this,
                {
                    if (!up) {
                        load_more.visibile()
                        more_comments.gone()
                    }
                    it?.let {
                        if (!opencc.isVisible() && it.size > 0)
                            opencc.visibile()

                        if (up) {
                            myComments.add(0, fromJson(it[0], comments::class))
                            comments_recycler.adapter?.notify(0, myComments.size)
                            return@observe
                        }
                        it.forEach { myComments.add(fromJson(it, comments::class)) }
                        comments_recycler.adapter?.notify(
                            myComments.size,
                            myComments.size + it.size
                        )
                        return@observe
                    }
                    if (myComments.size == 0) {
                        toastr("لا توجد تعليقات !")
                        return@observe
                    }
                    toastr("لا توجد تعليقات أخرى !")

                })
        }
    }
    }


    @ExperimentalTime
    @SuppressLint("SetTextI18n")
    fun prepare() {
        lifecycleOwner.apply {
                (lifecycleOwner.requireActivity() as home).apply {
                    Activity = this
                    dialog = getSpots()
                    mytool.navigationIcon = svg(R.drawable.ic_back)
                    Firestore= FirebaseFirestore.getInstance()
                    mytool.setNavigationOnClickListener {
                        onBackPressed()
                    }
                    controlle.apply {
                        setFocusable(true)
                        isFocusableInTouchMode = true
                        requestFocus()
                    }
                }

            Activity.apply {
                simple.timeZone= TimeZone.getTimeZone("GMT")
            max(comment, max = 2)
            dialog?.show()
            manageUser.Function<Any>(
                "theprod", hashMapOf("path" to path,
                    "exist" to load_vue(path).also { if (!it) save_vue(path) })
            ).observe(
                lifecycleOwner,
                {
                    dialog?.dismiss()
                    if (it is Boolean){
                        toastr("هنالك مشكلة !")
                        Activity.onBackPressed()
                    }
                    if (it is HashMap<*,*>){
                    product = fromJson(it, Products::class)
                    if (product.comments!!.size == 0)
                        lifecycleOwner.opencc.gone()
                    product.apply {
                        Location.coord?.let {
                            get_direction.visibile()
                        }
                        chat.setOnClickListener {
                            dialog!!.apply {
                                show()
                                manageUser.Function<Any>("startchat" , hashMapOf("uid1" to auth.currentUser!!.uid,"uid2" to profile?.uid)).observe(viewLifecycleOwner
                                ) {
                                    thepath ->
                                    if (thepath is String) {
                                        Firestore.document("users/"+profile?.uid).get().addOnSuccessListener {
                                                doc ->
                                                doc.toObject(user_data::class.java)?.apply {
                                                             dismiss()
                                                             view.NavController.navigate(
                                                                 R.id.action_product_to_conversation,
                                                                 bundleOf(
                                                                     "data" to last(
                                                                         picture = personal_info?.ppicture?.path,
                                                                         path = thepath,
                                                                         token = profile?.token,
                                                                         username = personal_info?.username,
                                                                         online =if (lastlogin==null) false else ((Timestamp.now().toDate().time.milliseconds.toLongMilliseconds())-lastlogin!!)<=60000,
                                                                         uid = uid,
                                                                         lastlogin = lastlogin
                                                                     )
                                                                 )
                                                             )
                                               }
                                         }
                                    }
                                }
                            }
                        }
                        mytool.title = "         ${name}         "
                        p_name.text = name

                        if (pictures!!.size>0)
                        mytools.glide(
                            Activity.application,
                            pictures!![0].path,
                            lifecycleOwner.p_img
                        )
                        else lifecycleOwner.p_img.setBackgroundResource(R.drawable.ic_image_not_found_black)
                        buysell.text = (if (buy_sell!!) "شراء" else "بيع")
                        Description?.date?.toLong()?.let {
                            calendar.timeInMillis = it
                            calendar.add(Calendar.HOUR,1)
                            mydate.text = simple.format(calendar.time)
                        }
                        p_vue.text = vue.toString()
                        p_location.text = Location.adress ?: "بدون عنوان"
                        p_price.text = price.toString() + " دينار "
                        if (product.comments?.size!! > 0)
                            if (comments!!.size > 0 && myrating.isVisible())
                                myrating.gone()
                        Description!!.apply {
                            p_categorie.text =
                                resources.getStringArray(R.array.categories)[categorie!!-1]
                            p_condition.text = resources.getStringArray(R.array.casee)[condition!!-1]
                            p_garanty.text = (if (garanty == 0) "نعم" else "لا")
                            p_description.text = description
                            p_description.underline()
                        }
                        profile?.apply {
                            uid?.let {
                                if (it == auth.currentUser!!.uid){
                                    contacts.gone()
                                    pprofile.gone()
                                }
                            }
                            if (picture==null)
                                mypicture.setBackgroundResource(R.drawable.ic_avatar)
                            else
                            mytools.glide(Activity.application, picture, mypicture, gif)
                            myname.text = username
                            lastlogin?.toLong()?.let {
                                calendar.timeInMillis = it
                                mylastlogin.text = simple.format(calendar.time)
                            }
                            if (phone == null)
                                phone_layout.gone()
                        }
                        myComments.clear()
                        myComments.addAll(product.comments!!)

                        comments_recycler.apply {
                            layoutManager = LinearLayoutManager(
                                Activity,
                                RecyclerView.VERTICAL,
                                false
                            )
                            adapter = Comments_recycler(this@Product_Viewmodel, myComments)
                        }

                    }
                    dialog!!.dismiss()
                    }
                })

            }
        }
    }
    fun delete_item(position: Int) {
        myComments.removeAt(position)
        lifecycleOwner.comments_recycler.adapter?.notify(position, myComments.size,null)
        if (myComments.size == 0)
            lifecycleOwner.opencc.gone()
    }
}