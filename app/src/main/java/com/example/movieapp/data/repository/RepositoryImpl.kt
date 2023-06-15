package com.example.movieapp.data.repository

import android.util.Log
import com.example.movieapp.data.database.MovieDao
import com.example.movieapp.data.model.actormodel.Actor
import com.example.movieapp.data.model.cinemamodel.Cinema
import com.example.movieapp.data.model.entitymoviemodel.MovieEntity
import com.example.movieapp.data.model.moviemodel.Movie
import com.example.movieapp.data.model.moviemodel.toMovieEntity
import com.example.movieapp.data.model.moviemodel.toMovieEntityList
import com.example.movieapp.data.model.moviesearchmodel.MovieSearchResponse
import com.example.movieapp.data.network.ApiService
import com.example.movieapp.data.result.Result
import retrofit2.Response
import javax.inject.Inject

class RepositoryImpl @Inject constructor(
    private val apiService: ApiService,
    private val movieDao: MovieDao
) : Repository {

    override suspend fun getRandomMovie(): Result<MovieEntity> {
        var result: Result<MovieEntity> = Result.EnqueueError("EnqueueError: fetchData doesnt work")
        try {
            val call = apiService.getRandomMovie()
            if (call.isSuccessful) {
                call.body()?.toMovieEntity()?.let {
                    result = Result.Success(it)
                } ?: run {
                    result = Result.ServerError(
                        "ServerError",
                        call.errorBody()?.source()?.buffer?.snapshot()?.utf8()
                    )
                }
            } else {
                result = checkCodeError(call)
            }
        } catch (e: Exception) {
            result = Result.ConnectionError("Connection Error", e.message)
        }
        return result
    }

    override suspend fun getActorById(actorId: Int): Result<Actor> {
        var result: Result<Actor> = Result.EnqueueError("EnqueueError: getActorById doesnt work")
        try {
            val call = apiService.getActor(actorId)
            if (call.isSuccessful) {
                call.body()?.let {
                    result = Result.Success(it)
                } ?: run {
                    result = Result.ServerError(
                        "ServerError",
                        call.errorBody()?.source()?.buffer?.snapshot()?.utf8()
                    )
                }
            } else {
                result = checkCodeError(call)
            }
        } catch (e: Exception) {
            result = Result.ConnectionError("Connection Error", e.message)
        }
        return result
    }

    override suspend fun getMovieByIdApi(movieId: Int): Result<MovieEntity> {
        var result: Result<MovieEntity> =
            Result.EnqueueError("EnqueueError: getActorById doesnt work")
        try {
            val call = apiService.getMovieApi(movieId)
            if (call.isSuccessful) {
                call.body()?.toMovieEntity()?.let {
                    result = Result.Success(it)
                } ?: run {
                    result = Result.ServerError(
                        "ServerError",
                        call.errorBody()?.source()?.buffer?.snapshot()?.utf8()
                    )
                }
            } else {
                result = checkCodeError(call)
            }
        } catch (e: Exception) {
            result = Result.ConnectionError("Connection Error", e.message)
        }
        return result
    }

    override suspend fun searchMovies(query: String, langTag: String): Result<List<MovieEntity>> {
        var result: Result<List<MovieEntity>> =
            Result.EnqueueError("EnqueueError: getActorById doesnt work")
        try {
            val call = if (langTag == "en") {
                apiService.searchMovies("votes.kp", -1, 1, 15, null, query)
            } else {
                apiService.searchMovies("votes.kp", -1, 1, 15, query)
            }
            if (call.isSuccessful) {
                call.body()?.movieList?.toMovieEntityList()?.let {
                    result = Result.Success(it)
                } ?: run {
                    result = Result.ServerError(
                        "ServerError",
                        call.errorBody()?.source()?.buffer?.snapshot()?.utf8()
                    )
                }
            } else {
                result = checkCodeError(call)
            }
        } catch (e: Exception) {
            result = Result.ConnectionError("Connection Error", e.message)
        }
        return result
    }

    override suspend fun saveMovie(movie: MovieEntity) {
        movieDao.insertMovie(movie)
    }

    override suspend fun deleteMovieById(movieId: Int) {
        movieDao.deleteMovieById(movieId)
    }

    override suspend fun saveFavouriteCinema(cinema: Cinema) =
        movieDao.insertFavouriteCinema(cinema)

    override suspend fun getAllFavouriteCinemas(): List<Cinema> = movieDao.getAllFavouriteCinemas()

    override suspend fun deleteCinemaByDescription(cinemaDescription: String) =
        movieDao.deleteCinemaByDescription(cinemaDescription)

    override suspend fun getCinemaByDescription(cinemaDescription: String): Cinema? =
        movieDao.getCinemaByDescription(cinemaDescription)

    override suspend fun saveAllData(movies: List<MovieEntity>) = movieDao.insertAllMovies(movies)


    override suspend fun getMovies(): List<MovieEntity> = movieDao.getAllMovies()

    override suspend fun getMoviesCount(): Int = movieDao.getMoviesCount()

    override suspend fun deleteAllMovies() = movieDao.deleteAllMovies()

    override suspend fun getMovieById(movieId: Int): MovieEntity? = movieDao.getMovieById(movieId)

    private fun checkCodeError(call: Response<*>): Result.ServerError {
        when {
            call.code() in 400..499 -> {
                return Result.ServerError(
                    "Client Error: ${
                        call.errorBody()?.source()?.buffer?.snapshot()?.utf8()
                    }"
                )
            }
            call.code() in 500..599 -> {
                return Result.ServerError(
                    "Server Error: ${
                        call.errorBody()?.source()?.buffer?.snapshot()?.utf8()
                    }"
                )
            }
            else -> {
                return Result.ServerError(
                    "Unknown Error: ${
                        call.errorBody()?.source()?.buffer?.snapshot()?.utf8()
                    }"
                )
            }
        }
    }
}