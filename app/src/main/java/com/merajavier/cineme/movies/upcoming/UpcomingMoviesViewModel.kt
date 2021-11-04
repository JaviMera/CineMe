package com.merajavier.cineme.movies.upcoming

import androidx.lifecycle.*
import androidx.paging.ExperimentalPagingApi
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import com.merajavier.cineme.movies.MovieDataItem
import com.merajavier.cineme.movies.MoviesPagerRepository
import com.merajavier.cineme.network.repositories.NetworkMoviesRepositoryInterface

@ExperimentalPagingApi
class UpcomingMoviesViewModel(
    private val networkMoviesRepository: NetworkMoviesRepositoryInterface
) : ViewModel() {

    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean>
        get() = _loading

    private val _upcomingMovies = MutableLiveData<List<MovieDataItem>>()
    val movies: LiveData<List<MovieDataItem>>
    get() = _upcomingMovies

    fun fetchMovies() : LiveData<PagingData<MovieDataItem>>{
        return MoviesPagerRepository()
            .upcomingPlayingMoviesPagingData(networkMoviesRepository)
            .map {
                it.map { movie -> movie }
            }
            .cachedIn(viewModelScope)
    }
}

