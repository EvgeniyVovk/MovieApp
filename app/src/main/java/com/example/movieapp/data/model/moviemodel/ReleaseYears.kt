package com.example.movieapp.data.model.moviemodel

import com.google.gson.annotations.SerializedName


data class ReleaseYears (

  @SerializedName("start" ) var start : Int? = null,
  @SerializedName("end"   ) var end   : Int? = null

)