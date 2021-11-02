package com.merajavier.cineme.movies.search

import androidx.lifecycle.*
import androidx.paging.ExperimentalPagingApi
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import com.merajavier.cineme.movies.MovieDataItem
import com.merajavier.cineme.movies.MoviesPagerRepository
import com.merajavier.cineme.network.NetworkSearchRepositoryInterface

@ExperimentalPagingApi
class SearchMoviesViewModel(
    private val networkSearchRepositoryInterface: NetworkSearchRepositoryInterface
) : ViewModel(){

    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean>
        get() = _loading

    private val _upcomingMovies = MutableLiveData<List<MovieDataItem>>()
    val movies: LiveData<List<MovieDataItem>>
        get() = _upcomingMovies

    fun fetchMovies(movieTitle: String) : LiveData<PagingData<MovieDataItem>> {
        return MoviesPagerRepository()
            .searchMoviesPagingData(
                networkSearchRepositoryInterface
                , movieTitle
            )
            .map {
                it.map { movie -> movie }
            }
            .cachedIn(viewModelScope)
    }
}