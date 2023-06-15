package com.example.movieapp.di

import com.example.movieapp.data.network.ApiInterceptor
import com.example.movieapp.data.network.ApiService
import com.example.movieapp.utils.Constants
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@Module
class NetworkModule {

    @Provides
    fun provideApiService(): ApiService {
        val client = OkHttpClient.Builder().apply {
            addInterceptor(ApiInterceptor())
        }.build()

        val gson = GsonBuilder()
            .setLenient()
            .create()

        val retrofit = Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()

        return retrofit.create(ApiService::class.java)
    }
}