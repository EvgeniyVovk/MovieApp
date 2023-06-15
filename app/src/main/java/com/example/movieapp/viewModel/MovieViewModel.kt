package com.example.movieapp.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.movieapp.data.model.actormodel.Actor
import com.example.movieapp.data.model.entitymoviemodel.MovieEntity
import com.example.movieapp.data.repository.Repository
import com.example.movieapp.data.result.Result
import com.example.movieapp.ui.adapters.clicklisteners.ActorClickListener
import com.example.movieapp.ui.adapters.clicklisteners.ActorMoviesClickListener
import com.example.movieapp.ui.adapters.clicklisteners.MovieClickListener
import com.example.movieapp.utils.ScreenStates
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class MovieViewModel(private val repository: Repository): ViewModel() {

    private val _screenState: MutableStateFlow<ScreenStates> =
        MutableStateFlow(ScreenStates.Loading)
    val screenState: StateFlow<ScreenStates> = _screenState.asStateFlow()

    private val _listToShow: MutableStateFlow<List<MovieEntity>> = MutableStateFlow(listOf())
    val listToShow: StateFlow<List<MovieEntity>> = _listToShow.asStateFlow()

    private val _movieClickInterfaceImpl: MutableLiveData<MovieClickListener?> =
        MutableLiveData(null)
    val movieClickInterfaceImpl: LiveData<MovieClickListener?> = _movieClickInterfaceImpl

    fun setClickInterface(clickInterface: MovieClickListener) {
        _movieClickInterfaceImpl.value = clickInterface
    }

    private val _actorClickInterfaceImpl: MutableLiveData<ActorClickListener?> =
        MutableLiveData(null)
    val actorClickInterfaceImpl: LiveData<ActorClickListener?> = _actorClickInterfaceImpl

    fun setActorClickInterface(clickInterface: ActorClickListener) {
        _actorClickInterfaceImpl.value = clickInterface
    }

    private val _actorMovieClickInterfaceImpl: MutableLiveData<ActorMoviesClickListener?> =
        MutableLiveData(null)
    val actorMovieClickInterfaceImpl: LiveData<ActorMoviesClickListener?> =
        _actorMovieClickInterfaceImpl

    fun setActorMoviesClickInterface(clickInterface: ActorMoviesClickListener) {
        _actorMovieClickInterfaceImpl.value = clickInterface
    }

    private val _actorToShow: MutableStateFlow<Actor?> = MutableStateFlow(null)
    val actorToShow: StateFlow<Actor?> = _actorToShow.asStateFlow()

    private val _movieToShowDetail: MutableStateFlow<MovieEntity?> = MutableStateFlow(null)
    val movieToShowDetail: StateFlow<MovieEntity?> = _movieToShowDetail.asStateFlow()

    private val _detailMovieScreenState: MutableStateFlow<ScreenStates> =
        MutableStateFlow(ScreenStates.Loading)
    val detailMovieScreenState: StateFlow<ScreenStates> = _detailMovieScreenState.asStateFlow()

    private val _detailActorScreenState: MutableStateFlow<ScreenStates> =
        MutableStateFlow(ScreenStates.Loading)
    val detailActorScreenState: StateFlow<ScreenStates> = _detailActorScreenState.asStateFlow()

    private val _favouriteMoviesList: MutableStateFlow<List<MovieEntity>?> =
        MutableStateFlow(null)
    val favouriteMoviesList: StateFlow<List<MovieEntity>?> = _favouriteMoviesList.asStateFlow()

    init {
        loadData()
    }

    fun saveMovieToDB(movie: MovieEntity){
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                repository.saveMovie(movie)
            }
        }
    }

    fun deleteMovieFromDB(movieId: Int) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                repository.deleteMovieById(movieId)
            }
        }
    }

    fun updateData() {
        _screenState.tryEmit(ScreenStates.Loading)
        loadData()
    }

    fun getMovieById(movieId: Int) {
        viewModelScope.launch {
            _detailMovieScreenState.emit(ScreenStates.Loading)
            withContext(Dispatchers.IO) {
                val movie = repository.getMovieById(movieId)
                if (movie != null) {
                    _movieToShowDetail.emit(movie)
                    _detailMovieScreenState.emit(ScreenStates.Ready)
                } else {
                    getMovieByIdApi(movieId)
                }
            }
        }
    }

    private suspend fun getMovieByIdApi(movieId: Int) {
        _detailMovieScreenState.emit(ScreenStates.Loading)
        when (val movie = repository.getMovieByIdApi(movieId)) {
            is Result.EnqueueError, is Result.ConnectionError, is Result.ServerError -> {
                _detailMovieScreenState.emit(ScreenStates.ConnectionError)
            }
            is Result.Success -> {
                _detailMovieScreenState.emit(ScreenStates.Ready)
                _movieToShowDetail.emit(movie.data)
            }
        }
    }

    fun getActorById(id: Int) {
        viewModelScope.launch {
            _detailActorScreenState.emit(ScreenStates.Loading)
            when (val actor = repository.getActorById(id)) {
                is Result.EnqueueError, is Result.ServerError, is Result.ConnectionError -> {
                    _detailActorScreenState.emit(ScreenStates.ConnectionError)
                }
                is Result.Success -> {
                    _detailActorScreenState.emit(ScreenStates.Ready)
                    _actorToShow.emit(actor.data)
                }
            }
        }
    }

    private fun loadData() {
        val deferredList: MutableList<Deferred<Result<MovieEntity>>> = mutableListOf()
        val movies: MutableList<MovieEntity> = mutableListOf()
        var resultFlag = true
        viewModelScope.launch {
            repeat(4) {
                deferredList.add(async { repository.getRandomMovie() })
            }
            val results = deferredList.awaitAll()
            for (result in results) {
                if (!checkResult(result, movies)) {
                    if (result is Result.ConnectionError) _screenState.emit(ScreenStates.ConnectionError)
                    else _screenState.emit(ScreenStates.ServerError)
                    resultFlag = false
                    break
                }
            }
            if (resultFlag) {
                _screenState.emit(ScreenStates.Ready)
                _listToShow.emit(movies)
            }
        }
    }

    private fun checkResult(
        result: Result<MovieEntity>,
        movies: MutableList<MovieEntity>
    ): Boolean {
        return when (result) {
            is Result.Success -> {
                movies.add(result.data)
                true
            }
            is Result.ServerError, is Result.ConnectionError, is Result.EnqueueError -> {
                false
            }
        }
    }

    fun loadFavouriteMovies() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                _favouriteMoviesList.emit(repository.getMovies())
            }
        }
    }
}