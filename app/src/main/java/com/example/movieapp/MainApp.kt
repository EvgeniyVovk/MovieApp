package com.example.movieapp

import android.app.Application
import android.content.Context
import com.example.movieapp.di.AppComponent
import com.example.movieapp.di.DaggerAppComponent
import com.example.movieapp.utils.Constants
import com.yandex.mapkit.MapKitFactory

class MainApp: Application() {
    lateinit var appComponent: AppComponent
        private set

    override fun onCreate() {
        super.onCreate()
        MapKitFactory.setApiKey(Constants.MAPKIT_API_KEY)
        appComponent = DaggerAppComponent.builder()
            .context(context = this)
            .build()
    }
}

val Context.appComponent: AppComponent
    get() = when (this) {
        is MainApp -> appComponent
        else -> applicationContext.appComponent
    }