package com.example.balouchi.data.repository.login.user.user

import com.example.balouchi.data.repository.login.user.chat.last
import com.example.balouchi.data.repository.login.user.user.auth.contact
import com.fasterxml.jackson.annotation.JsonProperty
import java.io.Serializable

data class Notificationn(
    @JsonProperty("sender")
    var sender:String?=null,
    @JsonProperty("type")
    var type:Boolean?=null,
    @JsonProperty("date")
    var date:Long?=null,
    @JsonProperty("picture")
    var picture:String?=null,
    @JsonProperty("path")
    var path: String?=null

):Serializable