package com.merajavier.cineme.cast

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.merajavier.cineme.R
import com.merajavier.cineme.databinding.ActorDetailsFilmographyItemBinding

class ActorCreditsAdapter()
    : ListAdapter<ActorCreditDataItem, ActorCreditsAdapter.ActorCreditViewHolder>(DiffCallback){

    class ActorCreditViewHolder(
        private val binding: ActorDetailsFilmographyItemBinding
    ) : RecyclerView.ViewHolder(binding.root){
        fun bind(actorCredit: ActorCreditDataItem){
            binding.actorCredit = actorCredit
        }
    }

    companion object DiffCallback : DiffUtil.ItemCallback<ActorCreditDataItem>() {

        override fun areItemsTheSame(oldItem: ActorCreditDataItem, newItem: ActorCreditDataItem): Boolean {
            return oldItem.id == newItem.id && oldItem.order == newItem.order
        }

        override fun areContentsTheSame(oldItem: ActorCreditDataItem, newItem: ActorCreditDataItem): Boolean {
            return oldItem == newItem
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ActorCreditViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = DataBindingUtil.inflate(inflater, R.layout.actor_details_filmography_item, parent, false) as ActorDetailsFilmographyItemBinding

        return ActorCreditViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ActorCreditViewHolder, position: Int) {
        val actorCreditItem = getItem(position)

        actorCreditItem?.let {
            holder.bind(actorCreditItem)
        }
    }
}