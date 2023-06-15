package com.example.movieapp.ui.adapters.clicklisteners

import com.example.movieapp.data.model.entitymoviemodel.MovieEntity

interface MovieClickListener {
    fun onMovieDetails(movieId: Int)
    fun onLikeButtonClicked(movie: MovieEntity)
}