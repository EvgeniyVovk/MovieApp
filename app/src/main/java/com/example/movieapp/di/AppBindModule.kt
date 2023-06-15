package com.example.movieapp.di

import com.example.movieapp.data.repository.Repository
import com.example.movieapp.data.repository.RepositoryImpl
import dagger.Binds
import dagger.Module

@Module
interface AppBindModule {

    @Suppress("FunctionName")
    @Binds
    fun bindRepositoryImpl_to_Repository(
        repositoryImpl: RepositoryImpl
    ): Repository
}