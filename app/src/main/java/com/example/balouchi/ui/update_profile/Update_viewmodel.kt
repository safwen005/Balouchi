package com.example.balouchi.ui.update_profile

import android.app.AlertDialog
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import com.example.balouchi.R
import com.example.balouchi.all_settings
import com.example.balouchi.data.repository.login.user.user.auth.manage_user
import com.example.balouchi.data.repository.login.user.user.storage
import com.example.balouchi.data.repository.login.user.user.user_data
import com.example.balouchi.util.*
import com.facebook.Profile
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask
import kotlinx.android.synthetic.main.update_profile.*
import java.io.ByteArrayOutputStream
import kotlin.apply

class Update_viewmodel : ViewModel() {

    lateinit var lifecycleOwner: update_profile

    lateinit var Activity:all_settings

    var dialog:AlertDialog?=null

    lateinit var mystorage_camera: storage

    var picture:Any?=null

    lateinit var random:String

    lateinit var Fire:FirebaseFirestore

    lateinit var image:StorageReference

   lateinit var firebase:HashMap<String,Any>

   lateinit var firestore_update:HashMap<String,Any>

    lateinit var auth:FirebaseAuth

    fun Click(view: View){
        Activity.apply {
        when(view.id){

            R.id.storage ->  {
                if (camera_storage==null)
                    camera_storage=mystorage_camera
                camera_storage!!.start(true)

            }

            R.id.mycamera -> {
                if (camera_storage==null)
                    camera_storage=mystorage_camera
                camera_storage!!.start(false)
            }

            R.id.myupload -> {
                lifecycleOwner.apply{
                    if (check_inputs(username, myemail)) {
                        if (!isEmailValid(myemail.text.toString().trim())) {
                            myemail.apply {
                                error = "نمط الإيميل غير صحيح"
                                requestFocus()
                            }
                            toastr("نمط الإيميل غير صحيح")
                            return
                        }
                        if (username.text.toString().trim().length < 4 && username.text.toString().trim()!=auth.currentUser?.displayName) {
                            toastr("الأسم ضعيفة")
                            username.apply {
                                error = "الأسم ضعيف"
                                requestFocus()
                            }
                            return
                        }

                        picture?.let {
                            auth.currentUser!!.apply {
                                upload((username.filter()!=displayName) || (myemail.filter()!=email) )
                            }
                            return
                        }
                        upload_info()

                }

                }
            }

            R.id.cancel -> onBackPressed()
            R.id.up -> {
                    anim(mycamera, R.anim.camera)
                    anim(storage, R.anim.storage)
                    mycamera.visibile()
                    storage.visibile()
                    up.invisibile()
            }

            }
        }
    }

    fun upload_info(){
        lifecycleOwner.apply{
            Activity.apply {
                getSpots().apply {
                    collect_data()
                    show()
                    manage.Function<Boolean>(
                        "update_profile", hashMapOf("uid" to myuser!!.uid,"info" to firebase)
                    ).observe(lifecycleOwner,
                        {
                            if (it){
                                Firestore.document("users/"+auth.currentUser!!.uid).update(
                                    firestore_update
                                ).addOnCompleteListener {
                                    if (it.isSuccessful){
                                        if (firestore_update.size>1){
                                            authh.forget(lifecycleOwner.myemail.filter()).observe(lifecycleOwner,  {
                                                dismiss()
                                                if (it == 1) {
                                                    toastg("لقد أرسلنا لك رسالة عبر الأيميل")
                                                    toastg("الرجاء ، سجل الدخول مرة اخرى !")
                                                    auth.signOut()
                                                    finishAffinity()
                                                    return@observe
                                                }
                                                dismiss()
                                                toastr("آسف حاول لاحقا")
                                            })
                                            return@addOnCompleteListener
                                        }
                                        auth.currentUser!!.reload().addOnSuccessListener {
                                            dismiss()
                                            toastg("تم التعديل بنجاح !")
                                            Activity.onBackPressed()
                                        }
                                        return@addOnCompleteListener
                                    }
                                    toastr("آسف حاول لاحقا")
                                }
                                return@observe
                            }
                            toastr("الإيميل مستعمل !")
                            dismiss()
                            lifecycleOwner.myemail.apply {
                                error = "الإيميل مستعمل !"
                                requestFocus()
                            }
                        })
                }
            }
        }
    }

    fun upload_picture():UploadTask{
        dialog!!.show()
        Activity.apply {
            random= random()
            image= mystorage.getReference("mine").child(random)
            image.apply {


            if (picture is Uri)
               return putFile(picture as Uri)
                val byte=ByteArrayOutputStream()
                    (picture as Bitmap).compress(Bitmap.CompressFormat.JPEG, 100,byte)
                 return putBytes(byte.toByteArray())

            }
        }
    }

    fun upload(test:Boolean){
        Activity.apply {
            Firestore.document("users/"+auth.currentUser!!.uid).get().addOnSuccessListener {
                it.toObject(user_data::class.java)?.apply {
                    personal_info?.ppicture?.key?.let {
                        mystorage.getReference("mine").child(it).delete()
                    }
                    upload_picture().addOnSuccessListener {
                        image.downloadUrl.addOnSuccessListener {
                            it?.apply {
                                upload_new(random,toString()).observe(lifecycleOwner, Observer {
                                    if (it){
                                        if (test){
                                            upload_info()
                                            return@Observer
                                        }
                                        auth.currentUser!!.reload().addOnSuccessListener {
                                            dialog!!.dismiss()
                                            toastg("تم التعديل بنجاح !")
                                            Activity.onBackPressed()
                                            ad()
                                        }

                                        return@Observer
                                    }
                                    toastr("آسف حاول لاحقا")
                                })
                            }
                        }
                    }

                }
            }
        }
    }

    fun upload_new(key:String,path:String): LiveData<Boolean> {

        val result=MutableLiveData<Boolean>()
        result.apply {
        manage_user().Function<Boolean>("update_profile", hashMapOf("uid" to Activity.auth.currentUser!!.uid,"info" to hashMapOf("photoURL" to path))).observe(lifecycleOwner,
             {
                 if (it){
                     Fire.document("users/"+Activity.auth.currentUser!!.uid).update(
                         hashMapOf("personal_info.ppicture" to hashMapOf("key" to random,"path" to path)) as Map<String, Any>
                     ).addOnCompleteListener {
                         if (it.isSuccessful){
                             result.value=true
                             return@addOnCompleteListener
                         }
                         result.value=false
                     }
                     return@observe
                 }
                 result.value=false
            })
        }
        return result
    }

    fun collect_data(){
        lifecycleOwner.apply{
            if (myemail.text.toString().trim()!=myuser!!.email){
                firebase.put("email",myemail.text.toString().trim())
                firebase.put("emailVerified",false)

                firestore_update.put("personal_info.pemail",myemail.text.toString().trim())
                firestore_update.put("verified",false)

            }

            if (username.text.toString().trim()!=myuser!!.displayName){
                firebase.put("displayName",username.text.toString().trim())
                firestore_update.put("personal_info.username",username.text.toString().trim())
            }
        }
    }

    fun prepare(){
        lifecycleOwner.apply {
            (requireActivity() as all_settings).apply {
                dialog=getSpots()
                Activity=this
                mystorage_camera= storage(lifecycleOwner,root)
                camera_storage=mystorage_camera
                Fire= FirebaseFirestore.getInstance()
                auth=FirebaseAuth.getInstance()
            }
            firebase=HashMap()
            firestore_update=HashMap()
            Activity.apply {
            max(username, myemail, max = 1)
            myuser?.apply {
                username.setText(displayName)
                myemail.setText(email)
                photoUrl?.let {
                    if (isFacebook()) {
                        loaduri(
                            "https://graph.facebook.com/${
                                Profile.getCurrentProfile()
                                    .getId()
                            }/picture?type=large&redirect=true&width=600&height=600",
                            profile_image
                        )
                        return@apply
                    }
                    loaduri(it, profile_image)
                    return@apply
                }
                profile_image.setBackgroundResource((R.drawable.ic_avatar))
            }

            mycamera.bringToFront()
            storage.bringToFront()

             anim(show_profile,R.anim.open_edit)

                 manage_edit(edit=username,o = {
                if (it.toString() == myuser!!.displayName && myemail.text.toString() == myuser!!.email && apply.isVisible()) {
                    apply.gone()
                    anim(apply, R.anim.hide)
                    return@manage_edit
                }
                if ((it.toString() != myuser!!.displayName || myemail.text.toString() == myuser!!.email) && !apply.isVisible()) {
                    apply.visibile()
                    anim(apply, R.anim.show_frame)
                    return@manage_edit
                }

            })

            manage_edit(edit=myemail,o = {
                if (it.toString() == myuser!!.email!! && username.text.toString() == myuser!!.displayName && apply.isVisible()) {
                    apply.gone()
                    anim(apply, R.anim.hide)
                    return@manage_edit
                }
                if ((it.toString() != myuser!!.email || username.text.toString() == myuser!!.displayName) && !apply.isVisible()) {
                    apply.visibile()
                    anim(apply, R.anim.show_frame)
                    return@manage_edit
                }
            })



            }
        }
    }

    fun Display(test:Boolean,data:Intent?){
        lifecycleOwner.apply {
        data?.let {
            if (!apply.isVisible()){
                apply.visibile()
                Activity.anim(apply, R.anim.show_frame)
            }
            if (test){
                picture=it.data
                profile_image.setImageURI(it.data)
                return
            }
           picture=(it.extras?.get("data") as Bitmap)
           profile_image.setImageBitmap((it.extras?.get("data") as Bitmap))
         }

       }
    }


}