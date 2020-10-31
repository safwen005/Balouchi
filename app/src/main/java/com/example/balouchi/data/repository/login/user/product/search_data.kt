package com.example.balouchi.data.repository.login.user.product

import com.fasterxml.jackson.annotation.JsonProperty
import java.io.Serializable

data class search_data(
    @JsonProperty("name")
    var name:String?=null,
    @JsonProperty("categorie")
    var categorie:Int?=0,
    @JsonProperty("condition")
    var condition:Int?=0,
    @JsonProperty("garanty")
    var garanty:Int?=0,
    @JsonProperty("min")
    var min:Long?=0,
    @JsonProperty("max")
    var max:Long?=100000,
    @JsonProperty("country")
    var country:Int?=0,
    ):Serializable