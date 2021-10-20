package com.merajavier.cineme.movies

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.merajavier.cineme.network.NetworkMovieRepository
import com.merajavier.cineme.network.TMDBApiInterface
import kotlinx.coroutines.launch
import java.lang.Exception

class MoviesViewModel(
    private val networkMovieRepository: NetworkMovieRepository)
    : ViewModel() {

    private val _movies = MutableLiveData<List<MovieDataItem>>()
    val movies: LiveData<List<MovieDataItem>>
    get() = _movies

    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean>
    get() = _loading

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
                Log.i("MoviesViewModel", exception.localizedMessage!!)
            }
        }
    }
}