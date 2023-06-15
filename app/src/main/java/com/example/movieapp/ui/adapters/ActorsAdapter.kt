package com.example.movieapp.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.movieapp.databinding.ViewHolderActorBinding
import com.bumptech.glide.Glide
import com.example.movieapp.R
import com.example.movieapp.data.model.entitymoviemodel.PersonsEntity
import com.example.movieapp.ui.adapters.clicklisteners.ActorClickListener
import com.example.movieapp.ui.adapters.diffcallbacks.ActorDiffCallback

class ActorsAdapter(private val actorClickListener: ActorClickListener?):
    RecyclerView.Adapter<ActorsAdapter.ActorsViewHolder>(), View.OnClickListener {
    var actors: List<PersonsEntity> = emptyList()
        set(newValue){
            val diffResult = DiffUtil.calculateDiff(ActorDiffCallback(field, newValue))
            field = newValue
            diffResult.dispatchUpdatesTo(this)
        }

    class ActorsViewHolder(
        val binding: ViewHolderActorBinding
    ): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ActorsViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ViewHolderActorBinding.inflate(inflater, parent, false)

        binding.root.setOnClickListener(this)

        return ActorsViewHolder(binding)
    }

    override fun getItemCount(): Int = actors.size

    override fun onBindViewHolder(holder: ActorsViewHolder, position: Int) {
        val actor = actors[position]
        with(holder.binding){
            holder.itemView.tag = actor
            textViewActor.text = actor.name
            actor.photo?.let {
                if (it.isNotBlank()) {
                    Glide.with(imageViewActor.context)
                        .load(actor.photo)
                        .placeholder(R.drawable.actor_placeholder_image)
                        .error(R.drawable.actor_placeholder_image)
                        .into(imageViewActor)
                } else {
                    imageViewActor.setImageResource(R.drawable.img_placeholder)
                }
            }
        }
    }

    override fun onClick(p0: View?) {
        val actor = p0?.tag as PersonsEntity
        actorClickListener?.onActorDetails(actor.id!!)
    }
}