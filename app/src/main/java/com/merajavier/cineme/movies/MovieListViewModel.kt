package com.merajavier.cineme.movies

import androidx.annotation.MainThread
import androidx.lifecycle.*
import androidx.paging.ExperimentalPagingApi
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import com.merajavier.cineme.common.ErrorResponse
import com.merajavier.cineme.common.TMDBApiResult
import com.merajavier.cineme.network.NetworkMoviesRepositoryInterface
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
class MovieListViewModel(
    private val networkMovieRepository: NetworkMoviesRepositoryInterface)
    : ViewModel() {

    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean>
    get() = _loading

    private val _selectedMovie = SingleLiveData<MovieDataItem>()
    val movieSelected: LiveData<MovieDataItem>
    get() = _selectedMovie

    fun fetchMovies() : LiveData<PagingData<MovieDataItem>>{
        return MoviesPagerRepository()
            .nowPlayingMoviesPagingData(networkMovieRepository)
            .map {
                it.map { movie -> movie }
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
}