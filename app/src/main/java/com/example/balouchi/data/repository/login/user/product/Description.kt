package com.example.balouchi.data.repository.login.user.product

import com.fasterxml.jackson.annotation.JsonProperty
import java.io.Serializable

data class Description(
    @JsonProperty("categorie")
    var categorie:Int?=null,
    @JsonProperty("date")
    var date:String?=null,
    @JsonProperty("condition")
    var condition:Int?=null,
    @JsonProperty("garanty")
    var garanty:Int?=null,
    @JsonProperty("description")
    var description: String?=null
       ) : Serializable