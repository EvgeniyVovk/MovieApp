package com.example.movieapp.data.model.actormodel

import com.google.gson.annotations.SerializedName


data class Actor (
    @SerializedName("id")
    var id: Int? = null,
    @SerializedName("name")
    var name: String? = null,
    @SerializedName("enName")
    var enName: String? = null,
    @SerializedName("photo")
    var photo: String? = null,
    @SerializedName("sex")
    var sex: String? = null,
    @SerializedName("growth")
    var growth: Double? = null,
    @SerializedName("birthday")
    var birthday: String? = null,
    @SerializedName("death")
    var death: String? = null,
    @SerializedName("age")
    var age: Int? = null,
    @SerializedName("birthPlace")
    var birthPlace: List<BirthPlace>? = listOf(),
    @SerializedName("deathPlace")
    var deathPlace: List<DeathPlace>? = listOf(),
    @SerializedName("spouses")
    var spouses: List<Spouses>? = listOf(),
    @SerializedName("countAwards")
    var countAwards: Int? = null,
    @SerializedName("profession")
    var profession: List<Profession>? = listOf(),
    @SerializedName("facts")
    var facts: List<Facts>? = listOf(),
    @SerializedName("movies")
    var movies: List<Movies>? = listOf()
)