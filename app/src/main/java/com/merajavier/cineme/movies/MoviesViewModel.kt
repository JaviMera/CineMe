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

    init {
        viewModelScope.launch {
            try {
                getMovie(551)
            }catch(exception: Exception){
                Log.i("MoviesViewModel", exception.localizedMessage!!)
            }
        }
    }

    private suspend fun getMovie(movieId: Int) {
        val response = apiInterface.getMovie(movieId)
        _movies.postValue(listOf(response))
    }
}