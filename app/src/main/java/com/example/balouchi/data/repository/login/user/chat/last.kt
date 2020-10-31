package com.example.balouchi.data.repository.login.user.chat

import android.os.Parcelable
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty
import kotlinx.android.parcel.Parcelize
import java.io.Serializable

@JsonIgnoreProperties(ignoreUnknown = true)
data class last(
    @JsonProperty("picture")
    var picture:String?=null,
    @JsonProperty("username")
    var username:String?=null,
    @JsonProperty("lastlogin")
    var lastlogin:Long?=null,
    @JsonProperty("lasts")
    var lasts:Conversation?=null,
    @JsonProperty("path")
    var path:String?=null,
    @JsonProperty("token")
    var token:String?=null,
    @JsonProperty("uid")
    var uid:String?=null,
    @JsonProperty("online")
    var online:Boolean?=null
) : Serializable