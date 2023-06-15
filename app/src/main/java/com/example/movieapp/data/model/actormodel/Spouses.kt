package com.example.movieapp.data.model.actormodel

import com.google.gson.annotations.SerializedName


data class Spouses (
  @SerializedName("id")
  var id: Int? = null,
  @SerializedName("name")
  var name: String? = null,
  @SerializedName("divorced")
  var divorced: Boolean? = null,
  @SerializedName("divorcedReason")
  var divorcedReason: String? = null,
  @SerializedName("sex")
  var sex: String? = null,
  @SerializedName("children")
  var children: Int? = null,
  @SerializedName("relation")
  var relation: String? = null
)