package com.example.balouchi.data.repository.login.user.user

import android.util.ArrayMap
import com.example.balouchi.data.repository.login.user.product.Products
import com.example.balouchi.data.repository.login.user.user.auth.contact
import com.fasterxml.jackson.annotation.JsonProperty


data class user_data(
    @JsonProperty("contacts")
    var contacts :ArrayList<contact>?=ArrayList(),
    @JsonProperty("lastlogin")
    var lastlogin :Long?=null,
    @JsonProperty("description")
    var description:String?=null,
    @JsonProperty("personal_info")
    var personal_info: Personal_Info?=null,
    @JsonProperty("verified")
    var verified:Boolean=true,
    @JsonProperty("rating")
    var rating:Float=0f,
    @JsonProperty("hide_show")
    var hide_show: Hide_Show?=Hide_Show(),
    @JsonProperty("street_adress")
    var street_adress:String?=null,
    @JsonProperty("active_notification")
    var active_notification:Boolean=true,
    @JsonProperty("uid")
    var uid:String?=null,
    @JsonProperty("token")
    var token:String?=null
)