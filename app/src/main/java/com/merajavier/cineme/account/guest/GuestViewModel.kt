package com.merajavier.cineme.account.guest

import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.merajavier.cineme.movies.MovieDataItem
import com.merajavier.cineme.movies.SingleLiveData
import com.merajavier.cineme.network.NetworkGuestRepositoryInterface
import com.merajavier.cineme.network.NetworkMovieRepositoryInterface
import kotlinx.coroutines.launch
import timber.log.Timber

class GuestViewModel(
    private val repository: NetworkGuestRepositoryInterface
) : ViewModel() {

    private val _rate = MutableLiveData<Double>()
    val rate: LiveData<Double>
    get() = _rate

    fun getRatedMovie(movieId: Int, sessionId: String) {
        viewModelScope.launch {
            try {
                val response = repository
                    .getRatedMovies(sessionId)

                val movie = response.ratedMovies.find { movie -> movie.id == movieId }
                if (movie != null) {
                    Timber.i("Got Rate")
                    _rate.postValue(movie.rating)
                }
            }catch (exception: Exception){
                Timber.i("Unable to get guest rated movie: ${exception.localizedMessage}")
            }
        }
    }
}