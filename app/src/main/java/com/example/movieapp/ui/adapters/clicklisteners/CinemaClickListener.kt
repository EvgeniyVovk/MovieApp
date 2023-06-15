package com.example.movieapp.ui.adapters.clicklisteners

import com.example.movieapp.data.model.cinemamodel.Cinema

interface CinemaClickListener {
    fun onLikeButtonClicked(cinema: Cinema)
}