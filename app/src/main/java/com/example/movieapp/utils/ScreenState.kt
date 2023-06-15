package com.example.movieapp.utils

sealed class ScreenStates{
    object Loading: ScreenStates()
    object Ready: ScreenStates()
    object ServerError: ScreenStates()
    object ConnectionError: ScreenStates()
}