package com.example.movieapp.ui.adapters.diffcallbacks

import androidx.recyclerview.widget.DiffUtil
import com.example.movieapp.data.model.entitymoviemodel.MovieEntity

class MovieDiffCallback(
    private val oldMovieList: List<MovieEntity>,
    private val newMovieList: List<MovieEntity>
): DiffUtil.Callback(){
    override fun getOldListSize(): Int = oldMovieList.size
    override fun getNewListSize(): Int = newMovieList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldMovie = oldMovieList[oldItemPosition]
        val newMovie = newMovieList[newItemPosition]
        return oldMovie.id == newMovie.id
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldMovie = oldMovieList[oldItemPosition]
        val newMovie = newMovieList[newItemPosition]
        return oldMovie == newMovie
    }
}