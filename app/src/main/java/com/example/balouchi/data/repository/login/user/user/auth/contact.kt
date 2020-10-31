package com.example.balouchi.data.repository.login.user.user.auth

import com.fasterxml.jackson.annotation.JsonProperty

data class contact(
    @JsonProperty("messages")
    val picture:String?=null,
    @JsonProperty("with")
    val name:String?=null)