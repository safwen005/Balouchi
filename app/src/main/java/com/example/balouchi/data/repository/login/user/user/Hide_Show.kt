package com.example.balouchi.data.repository.login.user.user

import com.fasterxml.jackson.annotation.JsonProperty

data class Hide_Show(
    @JsonProperty("iemail")
    var iemail:Boolean=true,
    @JsonProperty("itel")
    var itel:Boolean=true,
    @JsonProperty("ilocation")
    var ilocation:Boolean=true
)