package com.merajavier.cineme.movies.reviews

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.map
import androidx.lifecycle.viewModelScope
import androidx.paging.ExperimentalPagingApi
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import com.merajavier.cineme.movies.MoviesPagerRepository
import com.merajavier.cineme.network.repositories.NetworkMoviesRepositoryInterface

class MovieReviewsViewModel(
    private val networkMoviesRepositoryInterface: NetworkMoviesRepositoryInterface
) : ViewModel () {

    @ExperimentalPagingApi
    fun fetchReviews(movieId : Int) : LiveData<PagingData<ReviewDataItem>>{
        return MoviesPagerRepository()
            .reviewsPagingData(networkMoviesRepositoryInterface, movieId)
            .map {
                it.map { review -> review }
            }
            .cachedIn(viewModelScope)
    }
}