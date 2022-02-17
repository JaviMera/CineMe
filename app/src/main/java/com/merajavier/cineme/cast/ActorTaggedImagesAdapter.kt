package com.merajavier.cineme.cast

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.merajavier.cineme.R
import com.merajavier.cineme.databinding.ActorDetailsFilmographyItemBinding
import com.merajavier.cineme.databinding.ActorPhotoItemBinding

class ActorTaggedImagesAdapter(

): ListAdapter<ActorTaggedImageDataItem, ActorTaggedImagesAdapter.ActorTaggedImageViewHolder>(DiffCallback) {

    class ActorTaggedImageViewHolder(
        private val binding: ActorPhotoItemBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(actorPhoto: ActorTaggedImageDataItem) {
            binding.photo = actorPhoto
        }
    }

    companion object DiffCallback : DiffUtil.ItemCallback<ActorTaggedImageDataItem>() {

        override fun areItemsTheSame(
            oldItem: ActorTaggedImageDataItem,
            newItem: ActorTaggedImageDataItem
        ): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(
            oldItem: ActorTaggedImageDataItem,
            newItem: ActorTaggedImageDataItem
        ): Boolean {
            return oldItem == newItem
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ActorTaggedImageViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = DataBindingUtil.inflate(
            inflater,
            R.layout.actor_photo_item,
            parent,
            false
        ) as ActorPhotoItemBinding

        return ActorTaggedImageViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ActorTaggedImageViewHolder, position: Int) {
        val actorCreditItem = getItem(position)

        actorCreditItem?.let {
            holder.bind(actorCreditItem)
        }
    }
}