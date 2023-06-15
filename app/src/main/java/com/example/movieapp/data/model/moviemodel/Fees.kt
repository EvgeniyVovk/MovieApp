package com.example.movieapp.data.model.moviemodel

import com.google.gson.annotations.SerializedName


data class Fees (

  @SerializedName("world"  ) var world  : World?  = World(),
  @SerializedName("russia" ) var russia : Russia? = Russia(),
  @SerializedName("usa"    ) var usa    : Usa?    = Usa()

)