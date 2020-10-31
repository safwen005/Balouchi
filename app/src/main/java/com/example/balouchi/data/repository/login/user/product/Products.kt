package com.example.balouchi.data.repository.login.user.product

import android.os.Parcelable
import com.fasterxml.jackson.annotation.JsonProperty
import kotlinx.android.parcel.Parcelize
import java.io.Serializable
import kotlin.collections.ArrayList

@Parcelize
data class Products(
    @JsonProperty("pictures")
        var pictures:ArrayList<Pictures>?=ArrayList(),
    @JsonProperty("name")
        var name:String?=null,
    @JsonProperty("buy_sell")
        var buy_sell:Boolean?=null,
    @JsonProperty("vue")
        var vue:Int?=0,
    @JsonProperty("location")
        var Location: Location = Location(),
    @JsonProperty("price")
        var price:Double?=null,
    @JsonProperty("description")
        var Description:Description?=Description(),
    @JsonProperty("comments")
        var comments:ArrayList<comments>?=ArrayList(),
    @JsonProperty("profile")
        var profile:product_owner?=null,
    @JsonProperty("path")
        var path:String?=null
) : Serializable , Parcelable
