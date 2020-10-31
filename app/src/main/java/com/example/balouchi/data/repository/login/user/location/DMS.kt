package com.example.balouchi.data.repository.login.user.product

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty
import java.io.Serializable
@JsonIgnoreProperties(ignoreUnknown = true)
data class DMS(
    @JsonProperty("lat")
    var lat:Double?=null,
    @JsonProperty("lng")
    var lng:Double?=null
):Serializable