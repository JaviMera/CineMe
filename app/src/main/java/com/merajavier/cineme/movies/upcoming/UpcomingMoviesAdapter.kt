package com.merajavier.cineme.movies.upcoming

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.merajavier.cineme.R
import com.merajavier.cineme.databinding.UpcomingMovieItemBinding
import com.merajavier.cineme.movies.MovieDataItem

class UpcomingMoviesAdapter(

) : ListAdapter<UpcomingMovieDataItem, UpcomingMoviesAdapter.UpcomingMovieViewHolder>(DiffCallback){

    class UpcomingMovieViewHolder(
        private val binding: UpcomingMovieItemBinding
    ) : RecyclerView.ViewHolder(binding.root){

        fun bind(movie: UpcomingMovieDataItem) {
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
        holder.bind(movie)
    }

    companion object DiffCallback : DiffUtil.ItemCallback<UpcomingMovieDataItem>(){
        override fun areItemsTheSame(oldItem: UpcomingMovieDataItem, newItem: UpcomingMovieDataItem): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: UpcomingMovieDataItem, newItem: UpcomingMovieDataItem): Boolean {
            return oldItem == newItem
        }
    }
}