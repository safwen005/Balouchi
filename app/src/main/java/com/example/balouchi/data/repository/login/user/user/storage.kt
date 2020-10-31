package com.example.balouchi.data.repository.login.user.user

import android.content.Intent
import android.os.Build
import android.provider.MediaStore
import android.view.View
import androidx.fragment.app.Fragment
import com.example.balouchi.util.*
import com.google.android.material.snackbar.Snackbar

class storage(var fragment: Fragment, var myview: View?=null) {

    lateinit var mystorage: Intent
    var mycamera: Intent?=null
    var isStorage:Boolean=true
    var snake:Snackbar?=null
    var Activity=fragment.requireActivity()


    fun prepare() {
        mystorage =
            Intent(if (Build.VERSION.SDK_INT >= 19) Intent.ACTION_OPEN_DOCUMENT else Intent.ACTION_GET_CONTENT).apply {
                type = "image/*"
                addCategory(Intent.CATEGORY_OPENABLE)
                if (Build.VERSION.SDK_INT >= 18)
                 putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
                if (Build.VERSION.SDK_INT >= 19){
                    setFlags(Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION)
                    return@apply
                }
                setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            }

        mycamera= Intent(MediaStore.ACTION_IMAGE_CAPTURE)
    }

    fun start(storage: Boolean) {
        prepare()
        isStorage=storage
        Activity.apply {
            if (if (storage) (permissions(android.Manifest.permission.READ_EXTERNAL_STORAGE)) else (permissions(
                    android.Manifest.permission.CAMERA
                )) ){
                if (storage){
                    mystorage.resolveActivity(packageManager)?.let {
                        fragment.startActivityForResult(
                            Intent.createChooser(
                                mystorage,
                                "Choose pictures"
                            ), 1
                        )
                    }
                    return
                }
                mycamera!!.resolveActivity(Activity.packageManager)?.let {
                    fragment.startActivityForResult(
                        Intent.createChooser(
                            mycamera,
                            "Choose Camera App"
                        ), 2
                    )
                }

                return
            }
            if (loadnever(if (storage) "storage" else "camera")!!) {
                myview?.let{
                        snake= prepare_snake(it, if (storage) "التخزين" else "الكاميرا")
                    snake!!.show()
                    return
                }
                toastr(" يجب أن تفعل صلاحية " + if (storage) "التخزين" else "الكاميرا")
                return
            }

            request_permissions(if (storage) android.Manifest.permission.READ_EXTERNAL_STORAGE else android.Manifest.permission.CAMERA)

        }
    }

}