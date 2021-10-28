package com.merajavier.cineme.movies

import android.util.Log
import androidx.annotation.MainThread
import androidx.lifecycle.*
import com.merajavier.cineme.network.NetworkMovieRepository
import com.merajavier.cineme.network.TMDBApiInterface
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
    private val networkMovieRepository: NetworkMovieRepository)
    : ViewModel() {

    private val _movies = SingleLiveData<List<MovieDataItem>>()
    val movies: LiveData<List<MovieDataItem>>
    get() = _movies

    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean>
    get() = _loading

    private val _selectedMovie = SingleLiveData<MovieDataItem>()
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

    fun resetList() {
        _movies.value = listOf()
    }
}