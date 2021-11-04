package com.merajavier.cineme.login.account

import androidx.lifecycle.*
import androidx.paging.ExperimentalPagingApi
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import com.merajavier.cineme.common.ErrorResponse
import com.merajavier.cineme.common.TMDBApiResult
import com.merajavier.cineme.data.local.LocalAccountRepositoryInterface
import com.merajavier.cineme.movies.MoviesPagerRepository
import com.merajavier.cineme.movies.SingleLiveData
import com.merajavier.cineme.movies.favorites.FavoriteMovieDataItem
import com.merajavier.cineme.movies.favorites.FavoriteMoviesResponse
import com.merajavier.cineme.movies.favorites.MarkFavoriteRequest
import com.merajavier.cineme.movies.favorites.MarkFavoriteResponse
import com.merajavier.cineme.network.repositories.NetworkAccountRepositoryInterface
import kotlinx.coroutines.launch
import timber.log.Timber
import java.lang.Exception

enum class MarkFavoriteStatus{
    DONE,
    FAILED
}

@ExperimentalPagingApi
class AccountViewModel(
    private val accountRepositoryInterface: NetworkAccountRepositoryInterface
) : ViewModel(){

    private val _movieUpdated = MutableLiveData<MarkFavoriteStatus>()
    val movieUpdated: LiveData<MarkFavoriteStatus>
    get() = _movieUpdated

    private val _isFavoriteMovie = SingleLiveData<Boolean>()
    val isFavoriteMovie: LiveData<Boolean>
    get() = _isFavoriteMovie

    fun fetchMovies(sessionId: String, accountId: Int) : LiveData<PagingData<FavoriteMovieDataItem>> {
        return MoviesPagerRepository()
            .favoriteMoviesPagingData(
                accountRepositoryInterface,
                sessionId,
                accountId
            )
            .map {
                it.map { movie -> movie }
            }
            .cachedIn(viewModelScope)
    }

    fun addMovieToFavorites(sessionId:String, movieId: Int, isFavorite: Boolean){
        viewModelScope.launch {
            try{
                when(val markFavoriteResult = accountRepositoryInterface.markFavorite(
                    sessionId,
                    MarkFavoriteRequest("movie", movieId, isFavorite)
                )){
                    is TMDBApiResult.Success ->{
                        val favoriteMoviesResponse = markFavoriteResult.data as MarkFavoriteResponse
                        if(favoriteMoviesResponse.success){
                            _movieUpdated.postValue(MarkFavoriteStatus.DONE)
                        }else{
                            _movieUpdated.postValue(MarkFavoriteStatus.FAILED)
                        }
                    }
                    is TMDBApiResult.Failure -> {

                        val failureResponse = markFavoriteResult.data as ErrorResponse
                        Timber.i(failureResponse.statusMessage)
                    }
                    is TMDBApiResult.Error -> {
                        Timber.i(markFavoriteResult.message)
                    }
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
                when(val favoriteMoviesResult = accountRepositoryInterface.getFavoriteMovies(accountId, sessionId, 1)){
                    is TMDBApiResult.Success -> {
                        val response = favoriteMoviesResult.data as FavoriteMoviesResponse
                        val movie = response.movies.find { m -> m.id == movieId }
                        _isFavoriteMovie.postValue(movie != null)
                    }
                    is TMDBApiResult.Failure -> {
                        val failureResponse = favoriteMoviesResult.data as ErrorResponse
                        Timber.i(failureResponse.statusMessage)
                    }
                    is TMDBApiResult.Error -> {
                        Timber.i(favoriteMoviesResult.message)
                    }
                }
            }catch(exception: Exception){
                Timber.i(exception.localizedMessage)
                _isFavoriteMovie.postValue(false)
            }
        }
    }
}