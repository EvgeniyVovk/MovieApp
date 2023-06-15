package com.example.movieapp.ui.adapters.diffcallbacks

import androidx.recyclerview.widget.DiffUtil
import com.example.movieapp.data.model.cinemamodel.Cinema
import com.example.movieapp.data.model.entitymoviemodel.PersonsEntity

class CinemaDiffCallback(
    private val oldCinemaList: List<Cinema>,
    private val newCinemaList: List<Cinema>
): DiffUtil.Callback(){
    override fun getOldListSize(): Int = oldCinemaList.size
    override fun getNewListSize(): Int = newCinemaList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldCinema = oldCinemaList[oldItemPosition]
        val newCinema = newCinemaList[newItemPosition]
        return oldCinema.name == newCinema.name
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldCinema = oldCinemaList[oldItemPosition]
        val newCinema = newCinemaList[newItemPosition]
        return oldCinema == newCinema
    }
}