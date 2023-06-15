package com.example.movieapp.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.movieapp.data.model.cinemamodel.Cinema
import com.example.movieapp.data.model.entitymoviemodel.*
import com.example.movieapp.data.model.moviemodel.Genres
import com.example.movieapp.data.model.moviemodel.Movie
import com.example.movieapp.data.model.moviemodel.Persons
import com.example.movieapp.utils.Converters

@Database(
    entities = [MovieEntity::class, Cinema::class],
    version = 2,
    exportSchema = true
)
@TypeConverters(Converters::class)
abstract class MovieDB: RoomDatabase() {
    abstract fun getMovieDao(): MovieDao
}