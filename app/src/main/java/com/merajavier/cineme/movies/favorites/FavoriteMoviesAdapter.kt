package com.merajavier.cineme.movies.favorites

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.merajavier.cineme.R
import com.merajavier.cineme.databinding.UserFavoriteMovieItemBinding

class FavoriteMoviesAdapter(
    private val onFavoriteRemoveListener: OnFavoriteRemoveClickListener
) : ListAdapter<FavoriteMovieDataItem, FavoriteMoviesAdapter.FavoriteMovieViewHolder>(DiffCallback){

    class FavoriteMovieViewHolder(
        private val binding: UserFavoriteMovieItemBinding
    ) : RecyclerView.ViewHolder(binding.root){

        fun bind(movie: FavoriteMovieDataItem, onRemoveClickListener: OnFavoriteRemoveClickListener) {
            binding.favoriteMovieRemove.setOnClickListener {
                onRemoveClickListener.onClick(movie)
            }
            binding.movie = movie
        }
    }

    class OnFavoriteRemoveClickListener(val clickListener: (movie: FavoriteMovieDataItem) -> Unit){
        fun onClick(movie: FavoriteMovieDataItem) = clickListener(movie)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoriteMovieViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = DataBindingUtil.inflate(inflater, R.layout.user_favorite_movie_item, parent, false) as UserFavoriteMovieItemBinding
        return FavoriteMovieViewHolder(binding)
    }

    override fun onBindViewHolder(holder: FavoriteMovieViewHolder, position: Int) {
        val movie = getItem(position)
        holder.bind(movie, onFavoriteRemoveListener)
    }

    companion object DiffCallback : DiffUtil.ItemCallback<FavoriteMovieDataItem>() {

        override fun areItemsTheSame(oldItem: FavoriteMovieDataItem, newItem: FavoriteMovieDataItem): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(
            oldItem: FavoriteMovieDataItem,
            newItem: FavoriteMovieDataItem
        ): Boolean {
            return oldItem == newItem
        }
    }
}