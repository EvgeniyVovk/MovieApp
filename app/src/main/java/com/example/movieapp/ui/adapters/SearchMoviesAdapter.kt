package com.example.movieapp.ui.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.movieapp.R
import com.example.movieapp.data.model.entitymoviemodel.MovieEntity
import com.example.movieapp.databinding.ViewHolderMovieSearchBinding
import com.example.movieapp.ui.adapters.clicklisteners.MovieClickListener
import com.example.movieapp.ui.adapters.diffcallbacks.MovieDiffCallback

class SearchMoviesAdapter(
    private val movieClickListener: MovieClickListener?):
    RecyclerView.Adapter<SearchMoviesAdapter.MoviesViewHolder>(), View.OnClickListener {
    var movies: MutableList<MovieEntity> = mutableListOf()
        set(newValue){
            val diffResult = DiffUtil.calculateDiff(MovieDiffCallback(field, newValue))
            field = newValue
            diffResult.dispatchUpdatesTo(this)
        }

    class MoviesViewHolder(
        val binding: ViewHolderMovieSearchBinding
    ): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MoviesViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ViewHolderMovieSearchBinding.inflate(inflater, parent, false)

        binding.root.setOnClickListener(this)
        binding.imageViewLikeButtonMovieSearch.setOnClickListener(this)

        return MoviesViewHolder(binding)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: MoviesViewHolder, position: Int) {
        val movie = movies[position]
        with(holder.binding){
            holder.itemView.tag = movie
            imageViewLikeButtonMovieSearch.tag = movie
            if (movie.isFavourite) {
                imageViewLikeButtonMovieSearch.setImageResource(R.drawable.image_like_filled)
            } else {
                imageViewLikeButtonMovieSearch.setImageResource(R.drawable.image_like)
            }
            textViewMovieTitleSearch.text = movie.name ?: movie.alternativeName
            textViewMovieTimeSearch.text = movie.movieLength?.let { "%d минут".format(it) } ?: "Неизвестно"
            textViewGenreOfMovieSearch.text = movie.genres.joinToString { it.name ?: "" }
            textViewMovieReview.text = movie.description
            textViewReviewsNumber.text = movie.votes?.kp?.let { "Оценок: %s".format(it) }
            ratingBarSearch.rating = movie.rating?.kp?.toFloat()?.div(2) ?: 0f
            textViewKinopoiskRatingSearch.text = movie.rating?.kp?.let {
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
            R.id.imageView_like_button_movie_search -> {
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