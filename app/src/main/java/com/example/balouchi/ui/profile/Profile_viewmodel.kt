package com.example.balouchi.ui.profile

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.view.View
import androidx.core.content.res.ResourcesCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import com.bumptech.glide.Glide
import com.example.balouchi.R
import com.example.balouchi.data.repository.login.user.product.myproducts
import com.example.balouchi.data.repository.login.user.product.product_data
import com.example.balouchi.data.repository.login.user.user.user_data
import com.example.balouchi.ui.profile_items.profile_items
import com.example.balouchi.recycler.profile_viewpager
import com.example.balouchi.ui.home.home
import com.example.balouchi.util.*
import com.facebook.Profile
import com.google.android.material.tabs.TabLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.profile.*
import kotlin.apply
import kotlin.collections.ArrayList
import kotlin.collections.HashMap


class Profile_viewmodel : ViewModel(),View.OnClickListener{

    lateinit var lifecycleOwner: profile
    lateinit var dialog: AlertDialog
    lateinit var myauth: FirebaseAuth
    lateinit var Firestore:FirebaseFirestore

    var Activity: home? = null

    var stop = false

    var my_uid: String? = null


    fun Prepare() {
        (lifecycleOwner.requireActivity() as home).apply {
                Activity = this
                dialog = getSpots()
            myauth=FirebaseAuth.getInstance()
            Firestore= FirebaseFirestore.getInstance()
        }
        lifecycleOwner.apply {
            refreshh.setOnRefreshListener {
                LoadInfo()
                refreshh.isRefreshing = false
            }
            max(edit_edit, max = 7)
            scroll.smoothScrollTo(0,0)
        }
    }


    @SuppressLint("SetTextI18n")

    fun LoadInfo() {
        Prepare()
        dialog.show()
        myauth.currentUser!!.apply {
            lifecycleOwner.apply {
                view_pager.setSaveFromParentEnabled(false)
                Activity!!.myprofile = this@Profile_viewmodel
                Activity!!.apply {
                    arrayOf(
                        edit_desc,
                        cancel,
                        apply,
                        float_add
                    ).forEach { it.setOnClickListener(this@Profile_viewmodel) }

                    float_add.bringToFront()

                    Firestore.document("users/"+(my_uid ?: auth.currentUser?.uid)).get().addOnSuccessListener {

                        if (stop)
                            dialog.dismiss()
                        else stop = true
                            val result = it.toObject(user_data::class.java)
                            result?.apply {
                                lifecycleOwner.rating.rating = rating
                                lifecycleOwner.description.text = description
                                if (my_uid == null) {
                                    editt.visibile()
                                    update.invisibile()
                                    emaill.apply {
                                        text = if (!email.isNullOrEmpty()) " " + email.also {
                                            typeface = ResourcesCompat.getFont(
                                                context,
                                                R.font.et
                                            )
                                        } else "لا توجد إيميل"
                                    }
                                    tel.apply {
                                        text =
                                            if (!phoneNumber.isNullOrEmpty()) " | " + phoneNumber!!.substring(
                                                4,
                                                phoneNumber!!.length
                                            ).also {
                                                typeface =
                                                    ResourcesCompat.getFont(context, R.font.et)
                                            } else " لا يوجد هاتف  | "
                                    }
                                    lifecycleOwner.location.text =  street_adress ?: "لا يوجد عنوان"
                                    if (!displayName.isNullOrEmpty()) {
                                        name.text = displayName
                                        about.text = "حول " + "${displayName} ,"
                                    } else finish()

                                    if (photoUrl != null) {
                                        loaduri(
                                            if (isFacebook()) "https://graph.facebook.com/${
                                                Profile.getCurrentProfile().getId()
                                            }/picture?type=large&redirect=true&width=600&height=600" else photoUrl!!,
                                            profile_image
                                        )
                                    } else profile_image.setBackgroundResource((R.drawable.ic_avatar))

                                    if (verified) {
                                        validation.text = "مفعل"
                                        return@addOnSuccessListener
                                    }
                                    auth.currentUser!!.reload().addOnSuccessListener {
                                        if (isEmailVerified) {
                                            Firestore.document("users/"+auth.currentUser!!.uid).update(
                                                hashMapOf("verified" to true) as Map<String, Any>
                                            ).addOnCompleteListener {
                                                if (it.isSuccessful){
                                                    validation.text = "مفعل"
                                                    return@addOnCompleteListener
                                                }
                                                finish()
                                            }
                                        }
                                        return@addOnSuccessListener
                                    }
                                    validation.text = "غير مفعل"
                                    return@addOnSuccessListener
                                }
                                editt.gone()
                                personal_info?.apply {
                                    hide_show?.apply {
                                        name.text = username
                                        about.text = "حول " + "${username} ,"
                                        if (ppicture?.path != null)
                                            Glide.with(Activity!!).load(ppicture?.path).into(profile_image)
                                        else profile_image.setBackgroundResource((R.drawable.ic_avatar))

                                        emaill.apply {
                                            text =
                                                if (iemail && !pemail.isNullOrEmpty()) " " + pemail.also {
                                                    typeface = ResourcesCompat.getFont(
                                                        context,
                                                        R.font.et
                                                    )
                                                } else "لا توجد إيميل"
                                        }
                                        tel.apply {
                                            text =
                                                if (itel && ptel != null) (" | " + ptel.toString()).also {
                                                    typeface = ResourcesCompat.getFont(
                                                        context,
                                                        R.font.et
                                                    )
                                                } else " لا يوجد هاتف  | "
                                        }

                                     lifecycleOwner.location.text = if (ilocation) (street_adress ?: "لا يوجد عنوان") else "لا يوجد عنوان"

                                    }
                                }
                                validation.apply {
                                    if (verified) {
                                        text = "مفعل"
                                        return@addOnSuccessListener
                                    } else text = "غير مفعل"
                                }

                            }
                    }
                    LoadProducts(my_uid == null)
                }
            }
        }
    }

    fun LoadProducts(mine: Boolean) {
            Activity!!.apply {
                lifecycleOwner.apply {
                    stop()
                    manageUser.Function<Any>("myprod",hashMapOf("uid" to (my_uid ?: auth.currentUser!!.uid), "mine" to null , "pos" to 0)).observe(lifecycleOwner, Observer {
                            if (stop)
                                dialog.dismiss()
                            else stop = true

                            val myfavorite = arrayListOf<product_data?>(null)

                            val mymine = arrayListOf<product_data?>(null)

                            if (it is HashMap<*, *>) {
                                fromJson<myproducts>(it, myproducts::class).apply {
                                    myfavorite.addAll(favorite!!)
                                    mymine.addAll(this.mine!!)
                                }
                            }
                         else if (it is ArrayList<*>){
                                it.forEach {
                                    myfavorite.add(fromJson<product_data>(it, product_data::class))
                                }
                            }

                            view_pager.adapter = profile_viewpager(
                                lifecycleOwner.childFragmentManager,
                               arrayOf(
                                   profile_items(myfavorite, Pair(false,mine), load,my_uid),
                                   profile_items(mymine,Pair(true,mine), load,my_uid)
                               )
                            )

                            tab_layout.setupWithViewPager(view_pager)
                            tab_layout.apply {
                                tab(    Pair(0, R.drawable.ic_favorite),
                                        Pair(0,"المفضلة"),
                                        Pair(1, R.drawable.ic_my_items),
                                        Pair(1,if (mine) "أغراضي" else "أغراضه"),
                                        Pair(1, null)
                                    )
                                    changecolor(getTabAt(1), R.color.white)
                                    changecolor(getTabAt(0), R.color.blue)
                            }
                            return@Observer
                        })
                }
                }
            }

    override fun onClick(v: View?) {
        Prepare()
        lifecycleOwner.apply {

            when (v!!.id) {

                R.id.edit_desc -> start()

                R.id.cancel -> stop()

                R.id.apply -> upload()

                R.id.float_add -> Activity!!.view.NavController.navigate(R.id.action_profilee_to_post22)

            }
        }
    }


    fun goBack(): Boolean {
            var res = false
        lifecycleOwner.apply {
                if (edit.isVisible()) {
                    res = true
                    stop()
                }
            }
            return res
        }

    fun start() {
        lifecycleOwner.apply {
                edit.visibile()
                update.visibile()
                Activity!!.anim(update, R.anim.show_frame)
                description.invisibile()
                edit_desc.invisibile()
            }
        }

    fun stop() {
        lifecycleOwner.apply {
                edit.invisibile()
                edit_edit.setText("")
                update.invisibile()
                description.visibile()
                edit_desc.visibile()
            }
        }

    fun upload() {
        lifecycleOwner.apply {
                Activity!!.apply {
                    if (!edit_edit.text.toString().trim().isEmpty()) {
                        dialog.show()
                        val value = edit_edit.text.toString().trim()
                        Firestore.document("users/"+auth.currentUser!!.uid).update(
                            hashMapOf("description" to value) as Map<String, Any>
                        ).addOnCompleteListener {
                            dialog.dismiss()
                            if (it.isSuccessful){
                                toastg("تم التغيير بنجاح")
                                stop()
                                description.text = value
                                return@addOnCompleteListener
                            }
                            toastr("آسف حاول لاحقا")
                        }


                    }
                }
            }
        }

    fun TabLayout.tab(vararg list: Pair<Int, Any?>){
        list.forEach {
            it.apply {
            when(second){
                is Int -> this@tab.getTabAt(first)!!.setIcon(second as Int)
                is String -> this@tab.getTabAt(first)!!.text = second as String
                else -> this@tab.getTabAt(first)!!.select()
            }
        }
        }

    }

    }


