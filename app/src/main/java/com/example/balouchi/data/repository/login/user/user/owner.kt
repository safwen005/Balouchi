package com.example.balouchi.data.repository.login.user.user

import com.fasterxml.jackson.annotation.JsonProperty

data class owner(
    @JsonProperty("picture")
    val picture:String?=null,
    @JsonProperty("name")
    val name:String?=null,
    @JsonProperty("last_login")
    val last_login:String?=null)