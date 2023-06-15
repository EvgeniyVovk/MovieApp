package com.example.movieapp.data.model.moviemodel

import com.google.gson.annotations.SerializedName


data class Votes (

  @SerializedName("kp"                 ) var kp                 : String? = null,
  @SerializedName("imdb"               ) var imdb               : String? = null,
  @SerializedName("tmdb"               ) var tmdb               : Double?    = null,
  @SerializedName("filmCritics"        ) var filmCritics        : Double?    = null,
  @SerializedName("russianFilmCritics" ) var russianFilmCritics : Double?    = null,
  @SerializedName("await"              ) var await              : Double?    = null

)