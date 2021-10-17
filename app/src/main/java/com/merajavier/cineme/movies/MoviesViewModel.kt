package com.merajavier.cineme.movies

import android.app.Application
import android.graphics.Movie
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.merajavier.cineme.R
import com.merajavier.cineme.network.TMDBApi
import kotlinx.coroutines.launch
import retrofit2.Response
import retrofit2.awaitResponse
import java.lang.Exception

class MoviesViewModel(private val application: Application) : ViewModel() {

    private val _movie = MutableLiveData<MovieDataItem>()

    val movie: LiveData<MovieDataItem>
    get() = _movie

    init {
        viewModelScope.launch {
            try {
                getMovie(550)
            }catch(exception: Exception){
                Log.i("MoviesViewModel", exception.localizedMessage!!)
            }
        }
    }

    private suspend fun getMovie(movieId: Int) {
        val response = TMDBApi.tmdbService.getMovie(
            movieId,
            application.getString(R.string.tmdb_api_key))
            .awaitResponse()

        _movie.postValue(response.body())
    }
}