package com.merajavier.cineme.movies.reviews

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.merajavier.cineme.R
import com.merajavier.cineme.databinding.DetailsMovieReviewItemBinding
import com.merajavier.cineme.databinding.ReviewItemBinding
import com.merajavier.cineme.databinding.ReviewItemBindingImpl

class ReviewsPagerAdapter(
) : PagingDataAdapter<ReviewDataItem, ReviewsPagerAdapter.ReviewViewHolder>(DiffCallback){

    class ReviewViewHolder(
        private val binding: ReviewItemBinding
    ) : RecyclerView.ViewHolder(binding.root){

        fun bind(reviewDataItem: ReviewDataItem){
            binding.review = reviewDataItem
        }
    }

    override fun onBindViewHolder(holder: ReviewViewHolder, position: Int) {
        val review = getItem(position)
        review?.let { holder.bind(it) }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReviewViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = DataBindingUtil.inflate(inflater, R.layout.review_item, parent, false) as ReviewItemBinding
        return ReviewViewHolder(binding)
    }

    companion object DiffCallback : DiffUtil.ItemCallback<ReviewDataItem>(){
        override fun areItemsTheSame(oldItem: ReviewDataItem, newItem: ReviewDataItem): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: ReviewDataItem, newItem: ReviewDataItem): Boolean {
            return oldItem == newItem
        }
    }
}

