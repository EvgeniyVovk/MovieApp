package com.example.movieapp.data.model.moviesearchmodel

import com.example.movieapp.data.model.moviemodel.Movie
import com.google.gson.annotations.SerializedName

data class MovieSearchResponse (
    @SerializedName("docs")
    val movieList: List<Movie>
    )