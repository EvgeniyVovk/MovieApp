package com.example.movieapp.data.network

import com.example.movieapp.utils.Constants.API_KEY
import okhttp3.Interceptor
import okhttp3.Response

class ApiInterceptor: Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
            .newBuilder()
            .addHeader("accept", "application/json")
            .addHeader("X-API-KEY", API_KEY)
            .build()
        return chain.proceed(request)
    }
}