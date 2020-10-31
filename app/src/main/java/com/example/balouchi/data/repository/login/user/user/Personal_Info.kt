package com.example.balouchi.data.repository.login.user.user

import android.graphics.Picture
import com.example.balouchi.data.repository.login.user.product.Pictures
import com.fasterxml.jackson.annotation.JsonProperty

data class Personal_Info(
    @JsonProperty("ppicture")
    var ppicture : Pictures?=Pictures(),
    @JsonProperty("username")
    var username :String?=null,
    @JsonProperty("pemail")
    var pemail:String?=null,
    @JsonProperty("ptel")
    var ptel:String?=null)