package com.example.movieapp.data.repository

import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.movieapp.data.model.actormodel.Actor
import com.example.movieapp.data.model.cinemamodel.Cinema
import com.example.movieapp.data.model.entitymoviemodel.MovieEntity
import com.example.movieapp.data.result.Result

interface Repository {
    suspend fun getRandomMovie(): Result<MovieEntity>
    suspend fun saveAllData(movies: List<MovieEntity>)
    suspend fun getMovies(): List<MovieEntity>
    suspend fun getMoviesCount(): Int
    suspend fun deleteAllMovies()
    suspend fun getMovieById(movieId: Int): MovieEntity?
    suspend fun getActorById(actorId: Int): Result<Actor>
    suspend fun getMovieByIdApi(movieId: Int): Result<MovieEntity>
    suspend fun searchMovies(query: String, langTag: String): Result<List<MovieEntity>>
    suspend fun saveMovie(movie: MovieEntity)
    suspend fun deleteMovieById(movieId: Int)
    suspend fun saveFavouriteCinema(cinema: Cinema)
    suspend fun getAllFavouriteCinemas(): List<Cinema>
    suspend fun deleteCinemaByDescription(cinemaDescription: String)
    suspend fun getCinemaByDescription(cinemaDescription: String): Cinema?
}