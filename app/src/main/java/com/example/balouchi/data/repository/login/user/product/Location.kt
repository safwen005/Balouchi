package com.example.balouchi.data.repository.login.user.product

import com.fasterxml.jackson.annotation.JsonProperty
import java.io.Serializable


data class Location(
    @JsonProperty("country")
    var country:Int?=null,
    @JsonProperty("adress")
    var adress:String?=null,
    @JsonProperty("coord")
    var coord:DMS?=null
) : Serializable