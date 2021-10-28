package com.merajavier.cineme.login.account

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.merajavier.cineme.R
import com.merajavier.cineme.databinding.UserFavoriteMovieItemBinding
import com.merajavier.cineme.movies.MovieDataItem

class FavoriteMoviesAdapter(
) : ListAdapter<MovieDataItem, FavoriteMoviesAdapter.FavoriteMovieViewHolder>(DiffCallback){

    class FavoriteMovieViewHolder(
        private val binding: UserFavoriteMovieItemBinding
    ) : RecyclerView.ViewHolder(binding.root){

        fun bind(movieDataItem: MovieDataItem) {
            binding.movie = movieDataItem
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoriteMovieViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = DataBindingUtil.inflate(inflater, R.layout.user_favorite_movie_item, parent, false) as UserFavoriteMovieItemBinding
        return FavoriteMovieViewHolder(binding)
    }

    override fun onBindViewHolder(holder: FavoriteMovieViewHolder, position: Int) {
        val movie = getItem(position)
        holder.bind(movie)
    }

    companion object DiffCallback : DiffUtil.ItemCallback<MovieDataItem>() {

        override fun areItemsTheSame(oldItem: MovieDataItem, newItem: MovieDataItem): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: MovieDataItem, newItem: MovieDataItem): Boolean {
            return oldItem == newItem
        }
    }
}