package com.example.movieapp.data.model.actormodel

import com.google.gson.annotations.SerializedName


data class BirthPlace (
  @SerializedName("value")
  var value: String? = null
)