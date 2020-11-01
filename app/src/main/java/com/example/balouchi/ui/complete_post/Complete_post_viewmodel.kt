package com.example.balouchi.ui.complete_post

import android.app.AlertDialog
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Typeface
import android.net.Uri
import android.os.Build
import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.balouchi.R
import com.example.balouchi.data.repository.login.user.product.Pictures
import com.example.balouchi.data.repository.login.user.product.Products
import com.example.balouchi.data.repository.login.user.user.storage
import com.example.balouchi.recycler.images_adapter
import com.example.balouchi.ui.home.home
import com.example.balouchi.util.*
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask
import kotlinx.android.synthetic.main.complete_post.*
import java.io.ByteArrayOutputStream


class Complete_post_viewmodel : ViewModel() {

    lateinit var lifecycleOwner: complete_post
    var dialog: AlertDialog? = null
    var list = ArrayList<Any>()

    lateinit var myadapter: images_adapter
    lateinit var Firestore:FirebaseFirestore
    var product: Products? = null
    lateinit var image: StorageReference
    lateinit var random: String
    lateinit var MyActivity: home
    lateinit var mypath: String
    var Pictures = ArrayList<Any>()

    lateinit var mystorage_camera: storage

    fun prepare() {
        lifecycleOwner.apply {
            MyActivity = (requireActivity() as home)
            MyActivity.apply {
                mystorage_camera = storage(lifecycleOwner)
                camera_storage = mystorage_camera
                dialog = getSpots()
                c_images.apply {
                    layoutManager = GridLayoutManager(MyActivity, 2)
                    myadapter = images_adapter(lifecycleOwner, list)
                    adapter = myadapter
                    Firestore=FirebaseFirestore.getInstance()
                }
                myimages.underline()
                mydescription.underline()
                font(mydescription, uploadit, myedit)
                max(myedit, max = 7)
            }

            product = (arguments?.getSerializable("product") as Products)

            product!!.path?.let {
                product!!.apply {
                    post.text = "تعديل"
                    myedit.setText(Description?.description)
                    pictures!!.forEach { list.add(Pictures(it.path, it.key)) }
                    path!!.apply { mypath = substring(lastIndexOf("/") + 1, length) }
                    myadapter.mypath = path
                    c_images.adapter = myadapter
                }
            }
        }
    }

    fun Click(vv: View) {
        lifecycleOwner.apply {
            MyActivity.apply {
                when (vv.id) {
                    R.id.post -> {
                        if (check_inputs(myedit))
                            dialog!!.apply {
                                product?.apply {
                                    show()
                                    product?.Description?.description=lifecycleOwner.myedit.filter()
                                    (if (path == null) add_product() else manageUser.Function<Boolean>(
                                        "update_product",
                                        hashMapOf(
                                            "data" to ToMap(product!!),
                                            "path" to product!!.path
                                        )
                                    ).also {
                                        product!!.Description?.description =
                                            lifecycleOwner.myedit.filter()
                                    }).observe(lifecycleOwner, Observer {
                                        if (it == false) {
                                            dismiss()
                                            toastr("هنالك خطأ في البيانات !")
                                            return@Observer
                                        }
                                        if (it is String)
                                            it.apply {
                                                product!!.path = this
                                                mypath = substring(lastIndexOf("/") + 1, length)
                                            }
                                        Pictures.addAll(list.filter { (it is Uri || it is Bitmap) })
                                        if (Pictures.size > 0) {
                                            (it == true).uploadAll()
                                            return@Observer
                                        }
                                        ad()
                                        toastg(" تم " + (if (it == true) "التعديل " else " التحميل ") + "بنجاح !")
                                        dialog!!.dismiss()
                                        go(home::class)
                                        finish()


                                    })
                                }
                            }
                    }
                    R.id.back -> onBackPressed()
                    R.id.bold -> {

                        myedit.typeface = Typeface.defaultFromStyle(
                            if (myedit.typeface.isBold) Typeface.NORMAL.also {
                                bold.svg(R.drawable.ic_bold)
                            } else Typeface.BOLD.also {
                                bold.svg(R.drawable.ic_bold_g)
                            }
                        )
                        anim(bold, R.anim.show)
                    }

                    R.id.italique -> myedit.typeface = Typeface.defaultFromStyle(Typeface.ITALIC)

                    R.id.ligned -> myedit.underline()

                    R.id.upload -> {
                        uploading.invisibile()
                        anim(from, R.anim.show_upload)
                        from.visibile()
                    }

                    R.id.mystorage -> mystorage_camera.start(true)


                    R.id.camera -> mystorage_camera.start(false)
                }
            }

        }
    }

    fun Boolean.uploadAll(index: Int = 0) {
        MyActivity.apply {
            if (index < Pictures.size) {
                random = random()
                putFile(Pictures[index], mypath).addOnSuccessListener {
                    image.getDownloadUrl().addOnSuccessListener {
                        val value = Pictures(it.toString(), random)

                        Firestore.collection("products")
                            .document(product!!.path!!).update(
                            "pictures", FieldValue.arrayUnion(value)
                        ).addOnCompleteListener {
                            if (it.isSuccessful) {
                                uploadAll(index + 1)
                                return@addOnCompleteListener
                            }
                            dialog!!.dismiss()
                            toastr("حاول مجددا !")
                        }


                    }
                    return@addOnSuccessListener
                }
                return
            }
            toastg(" تم " + (if (this@uploadAll == true) "التعديل " else " التحميل ") + "بنجاح !")
            dialog!!.dismiss()
            go(home::class)
            finish()
        }

    }

    fun putFile(Picture: Any, path: String): UploadTask {
        MyActivity.apply {
            image = storage.getReference(path).child(random)
            if (Picture is Uri)
                return image.putFile(Picture)
            else {
                val baos = ByteArrayOutputStream()
                (Picture as Bitmap).compress(Bitmap.CompressFormat.JPEG, 100, baos)
                return image.putBytes(baos.toByteArray())
            }
        }
    }

    fun Display(test: Boolean, data: Intent?) {
        lifecycleOwner.c_images.adapter!!.apply {
            list.apply {
                if (test) {
                    if (Build.VERSION.SDK_INT >= 18) {
                        data?.clipData?.apply {
                            (0..itemCount - 1).forEach {
                                add(getItemAt(it).uri)
                            }
                            valid(itemCount)
                            notify(size + itemCount, size)
                            return
                        }
                    }
                    valid()
                    add(data?.data!!)
                    notify(size + 1, size)
                    return
                }
                valid()
                add((data?.extras?.get("data") as Bitmap))
                notify(size + 1, size)
            }
        }
    }

    fun valid(n: Int = 1, test: Boolean = true) {
        lifecycleOwner.apply {
            number.apply {
                text = if (test) text.toString().inc(n) else text.toString().dec(n)
                valid.svg(
                    if (text.toString().toInt() > 0) R.drawable.ic_valid_g else R.drawable.ic_valid
                )
            }
        }
    }


    fun add_product(): LiveData<Any> {
        val result = MutableLiveData<Any>()
        MyActivity.apply {
                dialog!!.show()
                product?.Description?.description=myedit.filter()
                manageUser.Function<Any>(
                    "add_product", hashMapOf(
                        "product" to ToMap
                            (product!!),
                        "categorie" to product?.Description!!.categorie, "uid" to auth.uid
                    )
                ).observe(lifecycleOwner, {
                    result.value = it
                })
            return result
        }
    }


}

