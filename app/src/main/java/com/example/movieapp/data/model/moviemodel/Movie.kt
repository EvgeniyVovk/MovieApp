package com.example.movieapp.data.model.moviemodel

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.example.ExternalId
import com.google.gson.annotations.SerializedName

data class Movie (
    @SerializedName("id")
    var id: Int? = null,

    @SerializedName("externalId")
    var externalId: ExternalId? = ExternalId(),

    @SerializedName("name")
    var name: String? = null,

    @SerializedName("alternativeName")
    var alternativeName: String? = null,

    @SerializedName("enName")
    var enName: String? = null,

    @SerializedName("names")
    var names: ArrayList<Names> = arrayListOf(),

    @SerializedName("type")
    var type: String? = null,

    @SerializedName("typeNumber")
    var typeNumber: Int? = null,

    @SerializedName("year")
    var year: Int? = null,

    @SerializedName("description")
    var description: String? = null,

    @SerializedName("shortDescription")
    var shortDescription: String? = null,

    @SerializedName("slogan")
    var slogan: String? = null,

    @SerializedName("status")
    var status: String? = null,

    @SerializedName("rating")
    var rating: Rating? = Rating(),

    @SerializedName("votes")
    var votes: Votes? = Votes(),

    @SerializedName("movieLength")
    var movieLength: Int? = null,

    @SerializedName("ratingMpaa")
    var ratingMpaa: String? = null,

    @SerializedName("ageRating")
    var ageRating: Int? = null,

    @SerializedName("logo")
    var logo: Logo? = Logo(),

    @SerializedName("poster")
    var poster: Poster? = Poster(),

    @SerializedName("backdrop")
    var backdrop: Backdrop? = Backdrop(),

    @SerializedName("videos")
    var videos: Videos? = Videos(),

    @SerializedName("genres")
    var genres: ArrayList<Genres> = arrayListOf(),

    @SerializedName("countries")
    var countries: ArrayList<Countries> = arrayListOf(),

    @SerializedName("persons")
    var persons: ArrayList<Persons> = arrayListOf(),

    @SerializedName("reviewInfo")
    var reviewInfo: ReviewInfo? = ReviewInfo(),

    @SerializedName("seasonsInfo")
    var seasonsInfo: ArrayList<SeasonsInfo> = arrayListOf(),

    @SerializedName("budget")
    var budget: Budget? = Budget(),

    @SerializedName("fees")
    var fees: Fees? = Fees(),

    @SerializedName("premiere")
    var premiere: Premiere? = Premiere(),

    @SerializedName("similarMovies")
    var similarMovies: ArrayList<SimilarMovies> = arrayListOf(),

    @SerializedName("sequelsAndPrequels")
    var sequelsAndPrequels: ArrayList<SequelsAndPrequels> = arrayListOf(),

    @SerializedName("watchability")
    var watchability: Watchability? = Watchability(),

    @SerializedName("releaseYears")
    var releaseYears: ArrayList<ReleaseYears> = arrayListOf(),

    @SerializedName("top10")
    var top10: Int? = null,

    @SerializedName("top250")
    var top250: Int? = null,

    @SerializedName("ticketsOnSale")
    var ticketsOnSale: Boolean? = null,

    @SerializedName("totalSeriesLength")
    var totalSeriesLength: Int? = null,

    @SerializedName("seriesLength")
    var seriesLength: Int? = null,

    @SerializedName("isSeries")
    var isSeries: Boolean? = null,

    @SerializedName("audience")
    var audience: ArrayList<Audience> = arrayListOf(),

    @SerializedName("facts")
    var facts: ArrayList<Facts> = arrayListOf(),

    @SerializedName("imagesInfo")
    var imagesInfo: ImagesInfo? = ImagesInfo(),

    @SerializedName("productionCompanies")
    var productionCompanies: ArrayList<ProductionCompanies> = arrayListOf(),
    @SerializedName("distributors")
    var distributors: Distributors? = Distributors(),
)


