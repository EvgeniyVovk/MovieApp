package com.example.movieapp.data.model.moviemodel

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

data class Genres (
  @SerializedName("name")
  var name: String? = null
)