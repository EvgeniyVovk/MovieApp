package com.example.movieapp.data.network

import com.example.movieapp.data.model.actormodel.Actor
import com.example.movieapp.data.model.entitymoviemodel.MovieEntity
import com.example.movieapp.data.model.moviemodel.Movie
import com.example.movieapp.data.model.moviesearchmodel.MovieSearchResponse
import com.example.movieapp.utils.Constants.API_KEY
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {

    @GET("v1.3/movie/random")
    suspend fun getRandomMovie(): Response<Movie>

    @GET("v1/person/{actorId}")
    suspend fun getActor(@Path("actorId") actorId: Int): Response<Actor>

    @GET("v1.3/movie/{movieId}")
    suspend fun getMovieApi(@Path("movieId") movieId: Int): Response<Movie>

    @GET("v1.3/movie")
    suspend fun searchMovies(
        @Query("sortField") sortField: String,
        @Query("sortType") sortType: Int,
        @Query("page") page: Int = 1,
        @Query("limit") limit: Int = 15,
        @Query("name") query: String? = null,
        @Query("alternativeName") enName: String? = null): Response<MovieSearchResponse>
}