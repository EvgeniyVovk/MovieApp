package com.example.movieapp.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.movieapp.R
import com.example.movieapp.data.model.actormodel.Movies
import com.example.movieapp.databinding.ViewHolderActorsMovieBinding
import com.example.movieapp.ui.adapters.clicklisteners.ActorMoviesClickListener
import com.example.movieapp.ui.adapters.diffcallbacks.ActorMoviesDiffCallback

class ActorMoviesAdapter(private val actorMovieClickListener: ActorMoviesClickListener?):
    RecyclerView.Adapter<ActorMoviesAdapter.ActorMoviesViewHolder>(), View.OnClickListener {
    var movies: List<Movies> = emptyList()
        set(newValue){
            val diffResult = DiffUtil.calculateDiff(ActorMoviesDiffCallback(field, newValue))
            field = newValue
            diffResult.dispatchUpdatesTo(this)
        }

    class ActorMoviesViewHolder(
        val binding: ViewHolderActorsMovieBinding
    ): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ActorMoviesViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ViewHolderActorsMovieBinding.inflate(inflater, parent, false)

        binding.root.setOnClickListener(this)

        return ActorMoviesViewHolder(binding)
    }

    override fun getItemCount(): Int = movies.size

    override fun onBindViewHolder(holder: ActorMoviesViewHolder, position: Int) {
        val movie = movies[position]
        with(holder.binding) {
            holder.itemView.tag = movie
            textViewActorsMovieName.text = movie.name ?: movie.alternativeName
            Glide.with(imageViewActorsMovie.context)
                .load(R.drawable.img_placeholder)
                .into(imageViewActorsMovie)
        }
    }

    override fun onClick(p0: View?) {
        val movie = p0?.tag as Movies
        actorMovieClickListener?.onMovieDetails(movie.id!!)
    }
}