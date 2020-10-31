package com.example.balouchi.data.repository.login.user.product

import com.fasterxml.jackson.annotation.JsonProperty
import java.io.Serializable

data class Pictures(
    @JsonProperty("path")
    var path:String?=null,
    @JsonProperty("key")
    var key:String?=null
) : Serializable