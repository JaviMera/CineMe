package com.merajavier.cineme.movies

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.merajavier.cineme.network.TMDBApiInterface
import kotlinx.coroutines.launch
import retrofit2.awaitResponse
import java.lang.Exception

class MoviesViewModel(
    private val apiInterface: TMDBApiInterface)
    : ViewModel() {

    private val _movies = MutableLiveData<List<MovieDataItem>>()
    val movies: LiveData<List<MovieDataItem>>
    get() = _movies

    private val pageNumber = 1

    init {
        viewModelScope.launch {
            try {
                getNowPlayingMovies(pageNumber)
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