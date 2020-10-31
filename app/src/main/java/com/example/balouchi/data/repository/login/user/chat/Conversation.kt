package com.example.balouchi.data.repository.login.user.chat

import com.fasterxml.jackson.annotation.JsonProperty
import java.io.Serializable

data class Conversation(
    @JsonProperty("date")
    var date:Long?=null,
    @JsonProperty("message")
    var message:String?=null,
    @JsonProperty("sender")
    var sender:String?=null,
    @JsonProperty("img")
    var img:String?=null,
    @JsonProperty("vue")
    var vue:Boolean?=null,
    @JsonProperty("path")
    var path:String?=null,
    @JsonProperty("token")
    var token:String?=null,
    @JsonProperty("username")
    var username:String?=null
) :Serializable