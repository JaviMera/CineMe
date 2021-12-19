package com.merajavier.cineme.movies

import androidx.annotation.MainThread
import androidx.lifecycle.*
import androidx.paging.ExperimentalPagingApi
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import com.merajavier.cineme.common.ErrorResponse
import com.merajavier.cineme.common.TMDBApiResult
import com.merajavier.cineme.data.local.TMDBDatabase
import com.merajavier.cineme.movies.rate.AccountStateResponse
import com.merajavier.cineme.movies.rate.MovieRateDataItem
import com.merajavier.cineme.movies.rate.RateMovieRequest
import com.merajavier.cineme.movies.rate.RateMovieResponse
import com.merajavier.cineme.network.repositories.NetworkMoviesRepositoryInterface
import kotlinx.coroutines.launch
import timber.log.Timber
import java.lang.Exception
import java.util.concurrent.atomic.AtomicBoolean

class SingleLiveData<T> : MutableLiveData<T>() {
    private val pending = AtomicBoolean()
    /**
     * Adds the given observer to the observers list within the lifespan of the given
     * owner. The events are dispatched on the main thread. If LiveData already has data
     * set, it will be delivered to the observer.
     *
     * @param owner The LifecycleOwner which controls the observer
     * @param observer The observer that will receive the events
     * @see MutableLiveData.observe
     */
    @MainThread
    override fun observe(owner: LifecycleOwner, observer: Observer<in T>) {
        super.observe(owner, Observer { t ->
            if (pending.compareAndSet(true, false)) {
                observer.onChanged(t)
            }
        })
    }
    /**
     * Sets the value. If there are active observers, the value will be dispatched to them.
     *
     * @param value The new value
     * @see MutableLiveData.setValue
     */
    @MainThread
    override fun setValue(value: T?) {
        pending.set(true)
        super.setValue(value)
    }
}

@ExperimentalPagingApi
class MoviesViewModel(
    private val networkMovieRepository: NetworkMoviesRepositoryInterface,
    private val tmdbDatabase: TMDBDatabase)
    : ViewModel() {

    private val _selectedMovie = SingleLiveData<MovieDataItem>()
    val movieSelected: LiveData<MovieDataItem>
    get() = _selectedMovie

    private val _isMovieFavorite = MutableLiveData<AccountStateResponse>()
    val isMovieFavorite: LiveData<AccountStateResponse>
        get() = _isMovieFavorite

    private val _isMovieRated = MutableLiveData<Boolean>()
    val isMovieRated: LiveData<Boolean>
    get() = _isMovieRated

    fun fetchMovies() : LiveData<PagingData<MovieDataItem>>{
        return MoviesPagerRepository()
            .nowPlayingMoviesPagingData(networkMovieRepository)
            .map {
                it.map { movie -> movie }
            }
            .cachedIn(viewModelScope)
    }

    fun fetchLocalMovies() : LiveData<PagingData<MovieDataItem>> {
        return MoviesPagerRepository()
            .localMoviesPagingData(tmdbDatabase)
            .map { it.map{movie -> movie}
            }
            .cachedIn(viewModelScope)
    }

    fun getMovieDetails(movieId: Int){
        viewModelScope.launch {
            try{
                when(val response = networkMovieRepository.getDetails(movieId)){
                    is TMDBApiResult.Success -> {
                        val movie = response.data as MovieDataItem
                        _selectedMovie.postValue(movie)
                    }
                    is TMDBApiResult.Error -> {
                        Timber.i("Unable to get upcoming movies: ${response.message}")
                    }
                    is TMDBApiResult.Failure -> {
                        val upcomingMoviesfailure = response.data as ErrorResponse
                        Timber.i(upcomingMoviesfailure.statusMessage)
                    }
                }
            }catch(exception: Exception){
                Timber.i("Problem selecting movie: ${exception.localizedMessage}")
            }
        }
    }

    fun getMovieState(movieId: Int, sessionId: String) {
        viewModelScope.launch {
            try {
                when(val accountStateResult = networkMovieRepository.getAccountState(movieId, sessionId)){
                    is TMDBApiResult.Success ->{

                        val favoriteMoviesResponse = accountStateResult.data as AccountStateResponse
                        Timber.i("Favorite: ${favoriteMoviesResponse.favorite}")
                        _isMovieFavorite.postValue(favoriteMoviesResponse)
                    }
                    is TMDBApiResult.Failure -> {

                        val failureResponse = accountStateResult.data as ErrorResponse
                        Timber.i(failureResponse.statusMessage)
                    }
                    is TMDBApiResult.Error -> {
                        Timber.i(accountStateResult.message)
                    }
                }
            }catch(exception: Exception){
                Timber.i("There was a problem removing the movie from your favorite list.")
                _isMovieFavorite.postValue(AccountStateResponse(false, MovieRateDataItem(0.0)))
            }
        }
    }

    fun rate(movieId: Int, rating: Float, sessionId: String) {
        viewModelScope.launch {
            try {
                when(val rateStateResult = networkMovieRepository.rate(movieId, RateMovieRequest(rating), sessionId)){
                    is TMDBApiResult.Success ->{
                        val rateMovieResponse = rateStateResult.data as RateMovieResponse
                        _isMovieRated.postValue(rateMovieResponse.success)
                    }
                    is TMDBApiResult.Failure -> {

                        val failureResponse = rateStateResult.data as ErrorResponse
                        Timber.i(failureResponse.statusMessage)
                    }
                    is TMDBApiResult.Error -> {
                        Timber.i(rateStateResult.message)
                    }
                }
            }catch(exception: Exception){
                Timber.i("There was a problem rating the movie.")
                _isMovieRated.postValue(false)
            }
        }
    }
}