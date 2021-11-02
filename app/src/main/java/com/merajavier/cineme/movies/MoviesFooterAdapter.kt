package com.merajavier.cineme.movies

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import androidx.recyclerview.widget.RecyclerView
import com.merajavier.cineme.R
import com.merajavier.cineme.databinding.RecyclerViewProgressBarBinding

class MoviesFooterAdapter(
) : LoadStateAdapter<MoviesFooterAdapter.SearchMoviesFooterViewHolder>(){

    class SearchMoviesFooterViewHolder(
        private val binding: RecyclerViewProgressBarBinding
    ) : RecyclerView.ViewHolder(binding.root){

        fun bind(loadState: LoadState){
            when(loadState){
                is LoadState.NotLoading -> binding.showProgress = false
                is LoadState.Loading -> binding.showProgress = true
                is LoadState.Error -> binding.showProgress = false
            }
        }
    }

    override fun onBindViewHolder(holder: SearchMoviesFooterViewHolder, loadState: LoadState) {
        holder.bind(loadState)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        loadState: LoadState
    ): SearchMoviesFooterViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = DataBindingUtil.inflate(
            inflater,
            R.layout.recycler_view_progress_bar,
            parent,
            false
        ) as RecyclerViewProgressBarBinding
        return SearchMoviesFooterViewHolder(binding)
    }
}