package com.example.balouchi.data.repository.login.user

import android.telecom.Call
import okhttp3.ResponseBody
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path
import retrofit2.http.Query

interface Request {

    @GET("translate_a/single?client=gtx&sl=en&tl=ar&dt=t&")
    fun myrequest(@Query("q") text:String?):retrofit2.Call<Any>

    @GET("json?key=7afc8da91dcd4a3b9e18df1adcd886c7&")
    fun test(@Query("q") adress:String?):retrofit2.Call<Any>
}