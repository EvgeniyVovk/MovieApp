package com.example.movieapp.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.movieapp.R
import com.example.movieapp.data.model.cinemamodel.Cinema
import com.example.movieapp.data.model.entitymoviemodel.MovieEntity
import com.example.movieapp.databinding.ViewHolderCinemaBinding
import com.example.movieapp.ui.adapters.clicklisteners.CinemaClickListener
import com.example.movieapp.ui.adapters.diffcallbacks.CinemaDiffCallback

class CinemaAdapter(
    private val cinemaClickListener: CinemaClickListener?
): RecyclerView.Adapter<CinemaAdapter.CinemasViewHolder>(), View.OnClickListener {
    var cinemas: MutableList<Cinema> = mutableListOf()
        set(newValue){
            val diffResult = DiffUtil.calculateDiff(CinemaDiffCallback(field, newValue))
            field = newValue
            diffResult.dispatchUpdatesTo(this)
        }

    class CinemasViewHolder(
        val binding: ViewHolderCinemaBinding
    ): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CinemasViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ViewHolderCinemaBinding.inflate(inflater, parent, false)

        binding.imageViewLikeButtonCinema.setOnClickListener(this)

        return CinemasViewHolder(binding)
    }

    override fun getItemCount(): Int = cinemas.size

    override fun onBindViewHolder(holder: CinemasViewHolder, position: Int) {
        val cinema = cinemas[position]
        with(holder.binding){
            imageViewLikeButtonCinema.tag = cinema
            if (cinema.isFavourite) {
                imageViewLikeButtonCinema.setImageResource(R.drawable.star_icon_filled)
            } else {
                imageViewLikeButtonCinema.setImageResource(R.drawable.star_icon)
            }
            textViewCinemaDistance.text = cinema.distance?.let { "%.2f км".format(it) }
            textViewCinemaDescription.text = cinema.description
            textViewCinemaName.text = cinema.name
        }
    }

    override fun onClick(p0: View?) {
        val cinema = p0?.tag as Cinema
        cinema.isFavourite = !cinema.isFavourite
        updateCinemaStatus(cinema)
        cinemaClickListener?.onLikeButtonClicked(cinema)
    }

    private fun updateCinemaStatus(cinema: Cinema) {
        val position = cinemas.indexOf(cinema)
        if (position >= 0) {
            cinemas[position] = cinema
            notifyItemChanged(position)
        }
    }
}