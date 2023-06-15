package com.example.movieapp.data.model.moviemodel

import com.google.gson.annotations.SerializedName


data class Backdrop (
  @SerializedName("url")
  var url: String? = null,
  @SerializedName("previewUrl")
  var previewUrl: String? = null
)