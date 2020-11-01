package com.example.balouchi.data.repository.login.user

import android.app.Application
import android.content.Context
import android.graphics.drawable.Drawable
import android.util.Log
import android.view.View
import android.widget.ImageView
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.transition.AutoTransition
import androidx.transition.Transition
import androidx.transition.TransitionManager
import com.bumptech.glide.GenericTransitionOptions
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.example.balouchi.R
import com.example.balouchi.data.repository.login.user.product.DMS
import com.example.balouchi.util.*
import com.google.android.gms.safetynet.SafetyNet
import com.google.android.material.card.MaterialCardView
import com.google.gson.internal.LinkedTreeMap
import pl.droidsonroids.gif.GifDrawable
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.converter.gson.GsonConverterFactory
import java.lang.Exception
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.collections.ArrayList

@Singleton
class tools @Inject constructor() {

    var key="6LdCAPUUAAAAAJL2bWzChkyYMlNyLU2MZlnki_iD"

    val request=retrofit2.Retrofit.Builder()
        .addConverterFactory(GsonConverterFactory.create())

    val expandable_listenner:(View,View)-> Transition.TransitionListener ={
         f,s ->
         object: Transition.TransitionListener{
            override fun onTransitionCancel(transition: Transition) {

            }

            override fun onTransitionEnd(transition: Transition) {
                f.apply {
                    if (isGone()) visibile().also { s.apply { if(isVisible()) gone() } } else gone()
                }
            }

            override fun onTransitionPause(transition: Transition) {

            }

            override fun onTransitionResume(transition: Transition) {

            }

            override fun onTransitionStart(transition: Transition) {

            }
        }
    }



    fun captcha(final:pl.droidsonroids.gif.GifImageView,progress:View,empty:View,Context:Application) {
            if (!final.isVisible()){
                progress.visibile()
                empty.invisibile()
                SafetyNet.getClient(Context).verifyWithRecaptcha(key).addOnCompleteListener{
                    if (it.isSuccessful) {
                        progress.invisibile()
                        final.visibile()
                        val gifDrawable= GifDrawable(Context.resources,
                            R.drawable.completed
                        )
                        gifDrawable.loopCount=1
                        final.setImageDrawable(gifDrawable)
                        return@addOnCompleteListener
                    }
                    progress.invisibile()
                    empty.visibile()
                }
            }
    }

    fun glide(Application:Context,uri:Any?,into:ImageView,gif:pl.droidsonroids.gif.GifImageView?=null){

        Glide
            .with(Application)
            .load(uri).listener(object:RequestListener<Drawable>{
                override fun onLoadFailed(
                    e: GlideException?,
                    model: Any?,
                    target: Target<Drawable>?,
                    isFirstResource: Boolean
                ): Boolean {
                    return false
                }

                override fun onResourceReady(
                    resource: Drawable?,
                    model: Any?,
                    target: Target<Drawable>?,
                    dataSource: DataSource?,
                    isFirstResource: Boolean
                ): Boolean {
                    gif?.apply {
                        gone()
                    }
                    return false
                }
            })
            .transition(GenericTransitionOptions.with(R.anim.show))
            .centerInside()
            .into(into)
    }

    fun Retrofit(text:String):LiveData<String?>{
        val result=MutableLiveData<String?>()
        result.apply {
            request.baseUrl("https://translate.googleapis.com/")
                .build().create(Request::class.java).apply {
                    myrequest(text).enqueue(object: Callback<Any> {
                        override fun onFailure(call: Call<Any>?, t: Throwable?) {
                            value=null
                        }
                        override fun onResponse(call: Call<Any>?, response: Response<Any>?) {
                            try {
                                value = (response!!.body() as ArrayList<ArrayList<ArrayList<*>>>)[0][0][0].toString()
                            }catch (e:Exception){
                                value=null
                            }
                        }
                    })

                }
            return this
    }
    }

    fun getLatLong(adress:String):LiveData<DMS?>{
        val result=MutableLiveData<DMS?>()
        result.apply {
          request.baseUrl("https://api.opencagedata.com/geocode/v1/").build().create(Request::class.java).test(adress).enqueue(object:Callback<Any>{
              override fun onFailure(call: Call<Any>?, t: Throwable?) {
                  value=null
              }

              override fun onResponse(call: Call<Any>?, response: Response<Any>?) {
                      response?.apply {
                          body()?.let {
                              try {
                              value=fromJson<DMS>(((((response.body() as LinkedTreeMap<*,*>).get("results") as? ArrayList<*>)?.get(0)) as LinkedTreeMap<*,*>).get("geometry")!!,DMS::class)
                              }catch (e:Exception){
                                  value=null
                              }
                              return@onResponse
                          }
                      }
                  value=null
              }
          })

    }
        return result
    }


    fun expandable(c:View,f:View,s:View){
        TransitionManager.beginDelayedTransition(c as MaterialCardView,AutoTransition().addListener(expandable_listenner(f,s)))
    }


}