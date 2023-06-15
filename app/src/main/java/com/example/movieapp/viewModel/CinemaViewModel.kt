package com.example.movieapp.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.movieapp.data.model.cinemamodel.Cinema
import com.example.movieapp.data.model.entitymoviemodel.MovieEntity
import com.example.movieapp.data.repository.Repository
import com.example.movieapp.ui.adapters.clicklisteners.CinemaClickListener
import com.example.movieapp.ui.adapters.clicklisteners.MovieClickListener
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class CinemaViewModel(private val repository: Repository): ViewModel(){

    private val _selectedPositionTabIndex = MutableStateFlow(0)
    val selectedPositionTabIndex: MutableStateFlow<Int> = _selectedPositionTabIndex

    private val _cinemasToShow: MutableStateFlow<List<Cinema>> = MutableStateFlow(listOf())
    val cinemasToShow: StateFlow<List<Cinema>> = _cinemasToShow.asStateFlow()

    private val _favouriteCinemasToShow: MutableStateFlow<List<Cinema>?> = MutableStateFlow(null)
    val favouriteCinemasToShow: StateFlow<List<Cinema>?> = _favouriteCinemasToShow.asStateFlow()

    private val _cinemaClickInterfaceImpl: MutableLiveData<CinemaClickListener?> =
        MutableLiveData(null)
    val cinemaClickInterfaceImpl: LiveData<CinemaClickListener?> = _cinemaClickInterfaceImpl

    fun setCinemaClickInterface(clickInterface: CinemaClickListener) {
        _cinemaClickInterfaceImpl.value = clickInterface
    }

    fun saveTabIndex(position: Int){
        viewModelScope.launch {
            _selectedPositionTabIndex.emit(position)
        }
    }

    fun addCinemaToShow(cinemas: List<Cinema>) {
        viewModelScope.launch {
            val cinemaList: MutableList<Cinema> = mutableListOf()
            for (item in cinemas){
                withContext(Dispatchers.IO) {
                    val cinema = repository.getCinemaByDescription(item.description ?: "")
                    if (cinema != null) {
                        cinemaList.add(cinema)
                    } else {
                        cinemaList.add(item)
                    }
                }
            }
            _cinemasToShow.emit(cinemaList)
        }
    }

    fun saveFavouriteCinemaToDB(cinema: Cinema) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                repository.saveFavouriteCinema(cinema)
            }
        }
    }

    fun deleteCinemaFromDB(description: String){
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                repository.deleteCinemaByDescription(description)
            }
        }
    }

    fun loadFavouriteCinemas() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                _favouriteCinemasToShow.emit(repository.getAllFavouriteCinemas())
            }
        }
    }
}