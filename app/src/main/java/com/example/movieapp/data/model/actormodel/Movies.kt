package com.example.movieapp.data.model.actormodel

import com.google.gson.annotations.SerializedName


data class Movies (
  @SerializedName("id")
  var id: Int? = null,
  @SerializedName("name")
  var name: String? = null,
  @SerializedName("alternativeName")
  var alternativeName: String? = null,
  @SerializedName("rating")
  var rating: Double? = null,
  @SerializedName("general")
  var general: Boolean? = null,
  @SerializedName("description")
  var description: String? = null,
  @SerializedName("enProfession")
  var enProfession: String? = null
)