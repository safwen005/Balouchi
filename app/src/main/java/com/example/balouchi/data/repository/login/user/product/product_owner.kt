package com.example.balouchi.data.repository.login.user.product

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty
import java.io.Serializable


data class  product_owner(
    @JsonProperty("username")
    var username:String?=null,
    @JsonProperty("picture")
    var picture:String?=null,
    @JsonProperty("lastlogin")
    var lastlogin:String?=null,
    @JsonProperty("phone")
    var phone:String?=null,
    @JsonProperty("uid")
    var uid:String?=null,
    @JsonProperty("token")
    var token:String?=null
): Serializable