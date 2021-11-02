package com.merajavier.cineme.movies.search

import androidx.lifecycle.*
import androidx.paging.ExperimentalPagingApi
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import com.merajavier.cineme.common.ErrorResponse
import com.merajavier.cineme.common.TMDBApiResult
import com.merajavier.cineme.movies.MovieDataItem
import com.merajavier.cineme.movies.MoviesPagerRepository
import com.merajavier.cineme.movies.SingleLiveData
import com.merajavier.cineme.network.NetworkMoviesRepositoryInterface
import com.merajavier.cineme.network.NetworkSearchRepositoryInterface
import kotlinx.coroutines.launch
import timber.log.Timber
import java.lang.Exception

@ExperimentalPagingApi
class SearchMoviesViewModel(
    private val networkSearchRepositoryInterface: NetworkSearchRepositoryInterface,
    private val networkMovieRepository: NetworkMoviesRepositoryInterface
) : ViewModel(){

    private val _upcomingMovies = MutableLiveData<List<MovieDataItem>>()
    val movies: LiveData<List<MovieDataItem>>
        get() = _upcomingMovies

    private val _selectedMovie = SingleLiveData<MovieDataItem>()
    val movieSelected: LiveData<MovieDataItem>
        get() = _selectedMovie

    private val _queryTitle = MutableLiveData<String>()
    val queryTitle: LiveData<String>
    get() = _queryTitle

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

    fun getMovieDetails(movieId: Int){
        viewModelScope.launch {
            try{
                when(val response = networkMovieRepository.getDetails(movieId)){
                    is TMDBApiResult.Success -> {
                        val movie = response.data as MovieDataItem
                        _selectedMovie.postValue(movie)
                    }
                    is TMDBApiResult.Error -> {
                        Timber.i("Unable to get upcoming movies: ${response.message}")
                    }
                    is TMDBApiResult.Failure -> {
                        val upcomingMoviesFailure = response.data as ErrorResponse
                        Timber.i(upcomingMoviesFailure.statusMessage)
                    }
                }
            }catch(exception: Exception){
                Timber.i("Problem selecting movie: ${exception.localizedMessage}")
            }
        }
    }

    fun queryTitle(title: String) {
        _queryTitle.value = title
    }
}