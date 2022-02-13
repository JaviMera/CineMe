package com.merajavier.cineme.cast

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.merajavier.cineme.R
import com.merajavier.cineme.databinding.DetailsMovieActorItemBinding

class ActorsRecyclerAdapter(
  private val onActorClickListener: OnActorClickListener
) : ListAdapter<ActorDataItem, ActorsRecyclerAdapter.ActorViewHolder>(DiffCallback) {

    class ActorViewHolder(
        private val binding: DetailsMovieActorItemBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(actor: ActorDataItem){
            binding.actor = actor
        }
    }

    class OnActorClickListener(val clickListener: (actorId: Int) -> Unit){
        fun onClick(actorId: Int) = clickListener(actorId)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ActorViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = DataBindingUtil.inflate(inflater, R.layout.details_movie_actor_item, parent, false) as DetailsMovieActorItemBinding

        return ActorViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ActorViewHolder, position: Int) {
        val actor = getItem(position)

        actor?.let{ actorItem ->
            holder.itemView.setOnClickListener {
                onActorClickListener.onClick(actorItem.id)
            }
            holder.bind(actorItem)
        }
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    companion object DiffCallback : DiffUtil.ItemCallback<ActorDataItem>() {

        override fun areItemsTheSame(oldItem: ActorDataItem, newItem: ActorDataItem): Boolean {
            return oldItem.id == newItem.id && oldItem.order == newItem.order
        }

        override fun areContentsTheSame(oldItem: ActorDataItem, newItem: ActorDataItem): Boolean {
            return oldItem == newItem
        }
    }
}