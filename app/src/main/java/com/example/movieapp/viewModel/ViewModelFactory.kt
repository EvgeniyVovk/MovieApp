package com.example.movieapp.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.movieapp.data.repository.Repository
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import javax.inject.Provider

class ViewModelFactory @AssistedInject constructor(
    private val repository: Provider<Repository>
): ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        val viewModel = when (modelClass) {
            MovieViewModel::class.java -> {
                MovieViewModel(repository.get())
            }
            SearchViewModel::class.java -> {
                SearchViewModel(repository.get())
            }
            CinemaViewModel::class.java -> {
                CinemaViewModel(repository.get())
            }
            else -> {
                throw IllegalStateException("Unknown view model class")
            }
        }
        return viewModel as T
    }

    @AssistedFactory
    interface Factory {
        fun create(): ViewModelFactory
    }
}