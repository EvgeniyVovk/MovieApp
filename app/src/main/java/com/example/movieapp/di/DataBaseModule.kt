package com.example.movieapp.di

import android.content.Context
import androidx.room.Room
import com.example.movieapp.data.database.MovieDB
import dagger.Module
import dagger.Provides

@Module
class DataBaseModule {
    @Provides
    fun provideMovieDao(db: MovieDB) = db.getMovieDao()

    @Provides
    fun provideMovieDB(context: Context): MovieDB {
        return Room.databaseBuilder(context, MovieDB::class.java, "movie_database")
            .fallbackToDestructiveMigration()
            .build()
    }
}