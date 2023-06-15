package com.example.movieapp.viewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.movieapp.data.model.entitymoviemodel.MovieEntity
import com.example.movieapp.data.repository.Repository
import com.example.movieapp.data.result.Result
import com.example.movieapp.utils.ScreenStates
import com.google.mlkit.nl.languageid.LanguageIdentification
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SearchViewModel(private val repository: Repository): ViewModel() {

    private val _listToShow: MutableStateFlow<List<MovieEntity>> = MutableStateFlow(listOf())
    val listToShow: StateFlow<List<MovieEntity>> = _listToShow.asStateFlow()

    private val _screenState: MutableStateFlow<ScreenStates?> = MutableStateFlow(null)
    val screenState: StateFlow<ScreenStates?> = _screenState.asStateFlow()

    private val pattern = "^[A-Za-z\\d. ]+$".toRegex()

    fun searchMovies(query: String) {
        var lang = ""
        lang = if (query.matches(pattern)){
            "en"
        } else {
            "ru"
        }
        viewModelScope.launch {
            _screenState.emit(ScreenStates.Loading)
            when (val result = repository.searchMovies(query, lang)){
                is Result.EnqueueError, is Result.ConnectionError, is Result.ServerError -> {
                    if (_listToShow.value.isEmpty()) {
                        _screenState.emit(ScreenStates.ConnectionError)
                    }
                }
                is Result.Success -> {
                    _screenState.emit(ScreenStates.Ready)
                    val movieList: MutableList<MovieEntity> = mutableListOf()
                    for (item in result.data) {
                        withContext(Dispatchers.IO){
                            val movie = repository.getMovieById(item.id!!)
                            if (movie != null) {
                                movieList.add(movie)
                            } else {
                                movieList.add(item)
                            }
                        }
                    }
                    _listToShow.emit(movieList)
                }
            }
        }
    }
}