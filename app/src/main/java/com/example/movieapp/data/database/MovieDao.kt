package com.example.movieapp.data.database

import androidx.room.*
import com.example.movieapp.data.model.cinemamodel.Cinema
import com.example.movieapp.data.model.entitymoviemodel.MovieEntity

@Dao
interface MovieDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllMovies(movies: List<MovieEntity>)

    @Query("SELECT * FROM movies")
    suspend fun getAllMovies(): List<MovieEntity>

    @Query("SELECT COUNT(*) FROM movies")
    suspend fun getMoviesCount(): Int

    @Query("DELETE FROM movies")
    suspend fun deleteAllMovies()

    @Query("SELECT * FROM movies WHERE id = :id")
    suspend fun getMovieById(id: Int): MovieEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMovie(movie: MovieEntity)

    @Query("DELETE FROM movies WHERE id = :movieId")
    suspend fun deleteMovieById(movieId: Int)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFavouriteCinema(cinema: Cinema)

    @Query("SELECT * FROM cinemas")
    suspend fun getAllFavouriteCinemas(): List<Cinema>

    @Query("DELETE FROM cinemas WHERE description = :cinemaDescription")
    suspend fun deleteCinemaByDescription(cinemaDescription: String)

    @Query("SELECT * FROM cinemas WHERE description = :cinemaDescription")
    suspend fun getCinemaByDescription(cinemaDescription: String): Cinema?
}