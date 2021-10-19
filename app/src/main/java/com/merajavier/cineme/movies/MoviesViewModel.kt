package com.merajavier.cineme.movies

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.merajavier.cineme.network.TMDBApiInterface
import kotlinx.coroutines.launch
import java.lang.Exception

class MoviesViewModel(
    private val apiInterface: TMDBApiInterface)
    : ViewModel() {

    private val _movies = MutableLiveData<List<MovieDataItem>>()
    val movies: LiveData<List<MovieDataItem>>
    get() = _movies

    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean>
    get() = _loading

    private val _pageNumber = 1

    init {
        viewModelScope.launch {
            try {
                _loading.postValue(true)
                getNowPlayingMovies(_pageNumber)
                _loading.postValue(false)

            }catch(exception: Exception){
                Log.i("MoviesViewModel", exception.localizedMessage!!)
            }
        }
    }

    private suspend fun getNowPlayingMovies(pageNumber: Int){
        val response = apiInterface.getNowPlayingMovies(pageNumber)
        _movies.postValue(response.movies)
    }
}