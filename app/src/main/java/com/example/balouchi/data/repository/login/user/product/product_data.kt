package com.example.balouchi.data.repository.login.user.product

import android.os.Parcelable
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty
import kotlinx.android.parcel.Parcelize
import java.io.Serializable

@Parcelize
@JsonIgnoreProperties(ignoreUnknown = true)
data class product_data(
    @JsonProperty("location")
    var Location:Location?=null,
    @JsonProperty("name")
    var name:String?=null,
    @JsonProperty("picture")
    var picture:String?=null,
    @JsonProperty("price")
    var price:String?=null,
    @JsonProperty("path")
    var path:String?=null,
    @JsonProperty("date")
    var date:String?=null,
    @JsonProperty("vue")
    var vue:Int?=null

): Serializable,Parcelable