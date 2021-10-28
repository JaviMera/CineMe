package com.merajavier.cineme.login.account

import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.merajavier.cineme.movies.MovieDataItem
import com.merajavier.cineme.network.NetworkAccountRepositoryInterface
import kotlinx.coroutines.launch
import timber.log.Timber
import java.lang.Exception

class AccountViewModel(
    private val accountRepositoryInterface: NetworkAccountRepositoryInterface
) : ViewModel(){

    private val _favoriteMovies = MutableLiveData<List<MovieDataItem>>()
    val favoriteMovies: LiveData<List<MovieDataItem>>
    get() = _favoriteMovies

    fun getFavoriteMovies(accountId: Int, sessionId: String){
        viewModelScope.launch {

            try {
                val response = accountRepositoryInterface.getFavoriteMovies(accountId, sessionId)
                _favoriteMovies.postValue(response.movies)
            }catch(exception: Exception){
                Timber.i(exception.localizedMessage)
            }
        }
    }
}