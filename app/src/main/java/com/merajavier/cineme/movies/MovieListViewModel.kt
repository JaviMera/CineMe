package com.merajavier.cineme.movies

import androidx.annotation.MainThread
import androidx.lifecycle.*
import com.merajavier.cineme.common.ErrorResponse
import com.merajavier.cineme.common.TMDBApiResult
import com.merajavier.cineme.movies.upcoming.UpcomingMovieDataItem
import com.merajavier.cineme.movies.upcoming.UpcomingMovieResponse
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

class MovieListViewModel(
    private val networkMovieRepository: NetworkMoviesRepositoryInterface)
    : ViewModel() {

    private val _nowPlayingMovies = SingleLiveData<List<UpcomingMovieDataItem>>()
    val movies: LiveData<List<UpcomingMovieDataItem>>
    get() = _nowPlayingMovies

    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean>
    get() = _loading

    private val _selectedMovie = SingleLiveData<UpcomingMovieDataItem>()
    val movieSelected: LiveData<UpcomingMovieDataItem>
    get() = _selectedMovie

    private var _pageNumber = 0

    private var _maxPages = 1

    fun getNowPlayingMovies(){
        viewModelScope.launch {

            try {
                _loading.postValue(true)
                _pageNumber = _pageNumber.inc()
                when(val response = networkMovieRepository.getNowPlaying(_pageNumber)) {
                    is TMDBApiResult.Success -> {
                        val upcomingMovies = response.data as UpcomingMovieResponse
                        if(_nowPlayingMovies.value?.any() == true){

                            _nowPlayingMovies.postValue(_nowPlayingMovies.value?.plus(upcomingMovies.movies))
                        }else{
                            _nowPlayingMovies.postValue(upcomingMovies.movies)
                        }

                        _maxPages = upcomingMovies.totalPages
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
                Timber.i(exception.localizedMessage)
            }finally {
                _loading.postValue(false)
            }
        }
    }

    fun getMovieDetails(movieId: Int){
        viewModelScope.launch {
            try{
                when(val response = networkMovieRepository.getDetails(movieId)){
                    is TMDBApiResult.Success -> {
                        val movie = response.data as UpcomingMovieDataItem
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

    fun resetList() {
        _nowPlayingMovies.value = listOf()
        _pageNumber = 0
        _maxPages = 0
    }

    fun canFetchMovies(): Boolean {
        return _pageNumber >= _maxPages
    }
}