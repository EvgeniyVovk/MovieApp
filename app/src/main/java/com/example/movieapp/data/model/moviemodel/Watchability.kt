package com.example.movieapp.data.model.moviemodel

import com.example.movieapp.data.model.moviemodel.Items
import com.google.gson.annotations.SerializedName


data class Watchability (

  @SerializedName("items" ) var items : ArrayList<Items> = arrayListOf()

)