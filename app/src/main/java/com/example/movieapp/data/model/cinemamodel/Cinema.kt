package com.example.movieapp.data.model.cinemamodel

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "cinemas")
data class Cinema(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val name: String? = null,
    val description: String? = null,
    val distance: Double? = null,
    var isFavourite: Boolean = false
)
