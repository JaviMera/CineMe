package com.merajavier.cineme.movies

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.merajavier.cineme.network.NetworkMovieRepository
import com.merajavier.cineme.network.TMDBApiInterface
import kotlinx.coroutines.launch
import timber.log.Timber
import java.lang.Exception

class MovieListViewModel(
    private val networkMovieRepository: NetworkMovieRepository)
    : ViewModel() {

    private val _movies = MutableLiveData<List<MovieDataItem>>()
    val movies: LiveData<List<MovieDataItem>>
    get() = _movies

    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean>
    get() = _loading

    private val _selectedMovie = MutableLiveData<MovieDataItem>()
    val movieSelected: LiveData<MovieDataItem>
    get() = _selectedMovie

    fun getNowPlayingMovies(pageNumber: Int){
        viewModelScope.launch {
            try {
                _loading.postValue(true)
                val movies = networkMovieRepository.getAll(pageNumber)
                if(_movies.value?.any() == true){
                    _movies.postValue(_movies.value?.plus(movies))
                }else{
                    _movies.postValue(movies)
                }
                _loading.postValue(false)

            }catch(exception: Exception){
                Timber.i(exception.localizedMessage)
            }
        }
    }

    fun getMovieDetails(movieId: Int){
        viewModelScope.launch {
            try{
                val movie = networkMovieRepository.getDetails(movieId)
                _selectedMovie.postValue(movie)
            }catch(exception: Exception){
                Timber.i("Problem selecting movie: ${exception.localizedMessage}")
            }
        }
    }
}