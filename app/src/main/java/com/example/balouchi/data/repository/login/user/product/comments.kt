package com.example.balouchi.data.repository.login.user.product

import com.fasterxml.jackson.annotation.JsonProperty
import java.io.Serializable

data  class comments  (
    @JsonProperty("profile")
    var profile:Any?=null,
    @JsonProperty("rating")
    var rating:Float?=null,
    @JsonProperty("date")
    var date:String?=null,
    @JsonProperty("comment")
    var comment:String?=null):Serializable