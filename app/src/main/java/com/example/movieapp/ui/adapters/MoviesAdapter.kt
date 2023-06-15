package com.example.movieapp.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.movieapp.R
import com.example.movieapp.data.model.entitymoviemodel.MovieEntity
import com.example.movieapp.databinding.ViewHolderMovieBinding
import com.example.movieapp.ui.adapters.clicklisteners.MovieClickListener
import com.example.movieapp.ui.adapters.diffcallbacks.MovieDiffCallback

class MoviesAdapter(private val movieClickListener: MovieClickListener?):
    RecyclerView.Adapter<MoviesAdapter.MoviesViewHolder>(), View.OnClickListener {
    var movies: MutableList<MovieEntity> = mutableListOf()
    set(newValue){
        val diffResult = DiffUtil.calculateDiff(MovieDiffCallback(field, newValue))
        field = newValue
        diffResult.dispatchUpdatesTo(this)
    }

    class MoviesViewHolder(
        val binding: ViewHolderMovieBinding
    ): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MoviesViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ViewHolderMovieBinding.inflate(inflater, parent, false)

        binding.root.setOnClickListener(this)
        binding.imageViewLikeButtonMovie.setOnClickListener(this)

        return MoviesViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MoviesViewHolder, position: Int) {
        val movie = movies[position]
        with(holder.binding){
            holder.itemView.tag = movie
            imageViewLikeButtonMovie.tag = movie
            if (movie.isFavourite) {
                imageViewLikeButtonMovie.setImageResource(R.drawable.image_like_filled)
            } else {
                imageViewLikeButtonMovie.setImageResource(R.drawable.image_like)
            }
            textViewMovieTitleCard.text = movie.name ?: movie.alternativeName
            textViewMovieTimeCard.text = movie.movieLength?.let { "%d минут".format(it) } ?: "Неизвестно"
            textViewGenreOfMovieCard.text = movie.genres.joinToString { it.name ?: "" }
            textViewAgeRating.text = movie.ageRating?.let { "%d+".format(it) } ?: "NR"
            ratingBarMovieList.rating = movie.rating?.kp?.toFloat()?.div(2) ?: 0f
            textViewKinopoiskRating.text = movie.rating?.kp?.let {
                "%.2f".format(it) } ?: ""
            movie.poster?.previewUrl?.let {
                if (it.isNotBlank()){
                    Glide.with(imageViewMoviePoster.context)
                        .load(it)
                        .placeholder(R.drawable.img_placeholder)
                        .error(R.drawable.img_placeholder)
                        .into(imageViewMoviePoster)
                }
                else {
                    imageViewMoviePoster.setImageResource(R.drawable.img_placeholder)
                }
            }
        }
    }

    override fun getItemCount(): Int = movies.size

    override fun onClick(p0: View?) {
        val movie = p0?.tag as MovieEntity
        when (p0.id) {
            R.id.imageView_like_button_movie -> {
                movie.isFavourite = !movie.isFavourite
                updateMovieStatus(movie)
                movieClickListener?.onLikeButtonClicked(movie)
            }
            else -> movieClickListener?.onMovieDetails(movie.id!!)
        }
    }

    private fun updateMovieStatus(movie: MovieEntity) {
        val position = movies.indexOf(movie)
        if (position >= 0) {
            movies[position] = movie
            notifyItemChanged(position)
        }
    }
}