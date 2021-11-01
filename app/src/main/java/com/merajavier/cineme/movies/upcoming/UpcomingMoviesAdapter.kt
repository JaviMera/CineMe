package com.merajavier.cineme.movies.upcoming

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.PagerAdapter
import com.merajavier.cineme.R
import com.merajavier.cineme.databinding.UpcomingMovieItemBinding
import com.merajavier.cineme.movies.MovieDataItem

class UpcomingMoviesAdapter(

) : PagingDataAdapter<MovieDataItem, UpcomingMoviesAdapter.UpcomingMovieViewHolder>(DiffCallback){

    class UpcomingMovieViewHolder(
        private val binding: UpcomingMovieItemBinding
    ) : RecyclerView.ViewHolder(binding.root){

        fun bind(movie: MovieDataItem) {
            binding.movie = movie
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UpcomingMovieViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = DataBindingUtil.inflate(
            inflater,
            R.layout.upcoming_movie_item,
            parent,
            false
        ) as UpcomingMovieItemBinding
        return UpcomingMovieViewHolder(binding)
    }

    override fun onBindViewHolder(holder: UpcomingMovieViewHolder, position: Int) {
        var movie = getItem(position)
        movie?.let { holder.bind(it) }
    }

    companion object DiffCallback : DiffUtil.ItemCallback<MovieDataItem>(){
        override fun areItemsTheSame(oldItem: MovieDataItem, newItem: MovieDataItem): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: MovieDataItem, newItem: MovieDataItem): Boolean {
            return oldItem == newItem
        }
    }
}