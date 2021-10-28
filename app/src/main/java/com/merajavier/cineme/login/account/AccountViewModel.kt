package com.merajavier.cineme.login.account

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.merajavier.cineme.movies.MovieDataItem
import com.merajavier.cineme.movies.SingleLiveData
import com.merajavier.cineme.movies.favorites.MarkFavoriteRequest
import com.merajavier.cineme.network.NetworkAccountRepositoryInterface
import kotlinx.coroutines.launch
import timber.log.Timber
import java.lang.Exception

enum class MarkFavoriteStatus{
    DONE,
    FAILED
}

class AccountViewModel(
    private val accountRepositoryInterface: NetworkAccountRepositoryInterface
) : ViewModel(){

    private val _favoriteMovies = MutableLiveData<List<MovieDataItem>>()
    val favoriteMovies: LiveData<List<MovieDataItem>>
    get() = _favoriteMovies

    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean>
    get() = _loading

    private val _movieUpdated = MutableLiveData<MarkFavoriteStatus>()
    val movieUpdated: LiveData<MarkFavoriteStatus>
    get() = _movieUpdated

    private val _isFavoriteMovie = SingleLiveData<Boolean>()
    val isFavoriteMovie: LiveData<Boolean>
    get() = _isFavoriteMovie

    fun getFavoriteMovies(accountId: Int, sessionId: String){
        viewModelScope.launch {

            try {
                _loading.postValue(true)
                val response = accountRepositoryInterface.getFavoriteMovies(accountId, sessionId)
                _favoriteMovies.postValue(response.movies)
                _loading.postValue(false)
            }catch(exception: Exception){
                Timber.i(exception.localizedMessage)
                _loading.postValue(false)
            }
        }
    }

    fun addMovieToFavorites(sessionId:String, request: MarkFavoriteRequest){
        viewModelScope.launch {
            try{

                val response = accountRepositoryInterface.markFavorite(
                    sessionId,
                    request
                )

                if(response.success){
                    _movieUpdated.postValue(MarkFavoriteStatus.DONE)
                }else{
                    _movieUpdated.postValue(MarkFavoriteStatus.FAILED)
                }

            }catch(exception: Exception){
                Timber.i("There was a problem removing the movie from your favorite list.")
                _movieUpdated.postValue(MarkFavoriteStatus.FAILED)
            }
        }
    }

    fun getFavoriteMovie(movieId:Int, accountId: Int, sessionId: String) {

        viewModelScope.launch {
            try {
                val response = accountRepositoryInterface.getFavoriteMovies(accountId, sessionId)
                val movie = response.movies.find { m -> m.id == movieId }

                _isFavoriteMovie.postValue(movie != null)
            }catch(exception: Exception){
                Timber.i(exception.localizedMessage)
                _isFavoriteMovie.postValue(false)
            }
        }
    }
}