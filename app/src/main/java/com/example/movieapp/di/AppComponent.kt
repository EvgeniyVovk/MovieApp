package com.example.movieapp.di

import android.content.Context
import com.example.movieapp.ui.screens.*
import com.example.movieapp.ui.widgets.*
import dagger.BindsInstance
import dagger.Component
import dagger.Module
import javax.inject.Qualifier

@Component(modules = [AppModule::class])
interface AppComponent {
    fun inject(fragment: HostFragment)
    fun inject(fragment: MovieListFragment)
    fun inject(fragment: MovieDetailFragment)
    fun inject(fragment: MovieFragment)
    fun inject(fragment: ActorDetailFragment)
    fun inject(fragment: SearchFragment)
    fun inject(fragment: FavouriteMoviesFragment)
    fun inject(fragment: CinemaMapFragment)
    fun inject(fragment: NearestCinemasFragment)
    fun inject(fragment: FavouriteCinemasFragment)
    fun inject(fragment: CinemaTabsFragment)
    fun inject(fragment: ErrorFragment)

    @Component.Builder
    interface Builder {
        @BindsInstance
        fun context(context: Context): Builder
        fun build(): AppComponent
    }
}

@Module(includes = [NetworkModule::class, AppBindModule::class, DataBaseModule::class])
class AppModule


@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class Prod


@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class Stage