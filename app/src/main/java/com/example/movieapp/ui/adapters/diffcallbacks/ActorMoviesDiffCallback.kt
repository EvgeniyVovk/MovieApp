package com.example.movieapp.ui.adapters.diffcallbacks

import androidx.recyclerview.widget.DiffUtil
import com.example.movieapp.data.model.actormodel.Movies

class ActorMoviesDiffCallback(
    private val oldMovieList: List<Movies>,
    private val newMovieList: List<Movies>
): DiffUtil.Callback(){
    override fun getOldListSize(): Int = oldMovieList.size
    override fun getNewListSize(): Int = newMovieList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldActor = oldMovieList[oldItemPosition]
        val newActor = newMovieList[newItemPosition]
        return oldActor.id == newActor.id
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldActor = oldMovieList[oldItemPosition]
        val newActor = newMovieList[newItemPosition]
        return oldActor == newActor
    }
}