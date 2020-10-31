package com.example.balouchi.data.repository.login.user.product

import com.fasterxml.jackson.annotation.JsonProperty

data class myproducts(
    @JsonProperty("mine")
    var mine:ArrayList<product_data?>?=ArrayList(),
    @JsonProperty("favorite")
    var favorite:ArrayList<product_data?>?=ArrayList()
)