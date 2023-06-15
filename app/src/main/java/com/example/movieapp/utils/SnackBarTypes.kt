package com.example.movieapp.utils

sealed class SnackBarTypes{
    object ConnectionError: SnackBarTypes()
    object Loading: SnackBarTypes()
    object ServerError: SnackBarTypes()
}