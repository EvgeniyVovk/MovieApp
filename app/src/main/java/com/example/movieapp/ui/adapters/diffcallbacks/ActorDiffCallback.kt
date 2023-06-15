package com.example.movieapp.ui.adapters.diffcallbacks

import androidx.recyclerview.widget.DiffUtil
import com.example.movieapp.data.model.entitymoviemodel.PersonsEntity

class ActorDiffCallback(
    private val oldActorList: List<PersonsEntity>,
    private val newActorList: List<PersonsEntity>
): DiffUtil.Callback(){
    override fun getOldListSize(): Int = oldActorList.size
    override fun getNewListSize(): Int = newActorList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldActor = oldActorList[oldItemPosition]
        val newActor = newActorList[newItemPosition]
        return oldActor.id == newActor.id
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldActor = oldActorList[oldItemPosition]
        val newActor = newActorList[newItemPosition]
        return oldActor == newActor
    }
}