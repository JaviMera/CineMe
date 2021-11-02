package com.merajavier.cineme.movies.search

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.merajavier.cineme.R
import com.merajavier.cineme.databinding.RecyclerViewMovieItemBinding
import com.merajavier.cineme.databinding.RecyclerViewProgressBarBinding
import com.merajavier.cineme.databinding.UpcomingMovieItemBinding
import com.merajavier.cineme.movies.MovieDataItem

class SearchMoviesAdapter(
    private val onMovieClickListener: OnMovieClickListener
) : PagingDataAdapter<MovieDataItem, SearchMoviesAdapter.SearchMovieViewHolder>(DiffCallback){

    class SearchMovieViewHolder(
        private val binding: RecyclerViewMovieItemBinding
    ) : RecyclerView.ViewHolder(binding.root){

        fun bind(movie: MovieDataItem) {
            binding.movie = movie
        }
    }

    class OnMovieClickListener(val clickListener: (movieId: Int) -> Unit){
        fun onClick(movieId: Int) = clickListener(movieId)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchMovieViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = DataBindingUtil.inflate(
            inflater,
            R.layout.recycler_view_movie_item,
            parent,
            false
        ) as RecyclerViewMovieItemBinding
        return SearchMovieViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SearchMovieViewHolder, position: Int) {
        val movie = getItem(position)
        holder.itemView.setOnClickListener {
            movie?.id?.let{ movieId ->
                onMovieClickListener.onClick(movieId)
            }
        }
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