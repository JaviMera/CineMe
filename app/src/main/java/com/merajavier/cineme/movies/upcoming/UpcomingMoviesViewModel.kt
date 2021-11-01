package com.merajavier.cineme.movies.upcoming

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.work.ListenableWorker
import com.merajavier.cineme.common.ErrorResponse
import com.merajavier.cineme.common.TMDBApiResult
import com.merajavier.cineme.movies.MovieDataItem
import com.merajavier.cineme.network.NetworkMovieRepository
import com.merajavier.cineme.sendNotification
import kotlinx.coroutines.launch
import timber.log.Timber
import java.lang.Exception

class UpcomingMoviesViewModel(
    private val networkMovieRepository: NetworkMovieRepository
) : ViewModel() {

    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean>
        get() = _loading

    private val _upcomingMovies = MutableLiveData<List<UpcomingMovieDataItem>>()
    val upcomingMovies: LiveData<List<UpcomingMovieDataItem>>
    get() = _upcomingMovies

    private var _pageNumber = 0

    private var _maxPages = 1
    val maxPages: Int
    get() = _maxPages

    fun getUpcomingMovies(){
        viewModelScope.launch {
            try{
                _loading.postValue(true)
                _pageNumber = _pageNumber.inc()
                when(val response = networkMovieRepository.getUpcoming(_pageNumber)) {
                    is TMDBApiResult.Success -> {
                        val upcomingMovies = response.data as UpcomingMovieResponse
                        if(_upcomingMovies.value?.any() == true){

                            _upcomingMovies.postValue(_upcomingMovies.value?.plus(upcomingMovies.movies))
                        }else{
                            _upcomingMovies.postValue(upcomingMovies.movies)
                        }

                        _maxPages = upcomingMovies.totalPages
                    }
                    is TMDBApiResult.Error -> {
                        Timber.i("Unable to get upcoming movies: ${response.message}")
                    }
                    is TMDBApiResult.Failure -> {
                        val upcomingMoviesfailure = response.data as ErrorResponse
                        Timber.i(upcomingMoviesfailure.statusMessage)
                    }
                }
            }catch(exception: Exception){
                Timber.i(exception.localizedMessage)
            }finally {
                _loading.postValue(false)
            }
        }
    }

    fun canFetchMovies() : Boolean {
        return _pageNumber >= _maxPages
    }

    fun resetList() {
        _upcomingMovies.value = listOf()
        _pageNumber = 0
    }
}