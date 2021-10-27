package com.merajavier.cineme.movies

import androidx.lifecycle.*
import com.merajavier.cineme.genre.RateMovieRequest
import com.merajavier.cineme.network.NetworkGuestRepositoryInterface
import com.merajavier.cineme.network.NetworkMovieRepository
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

    private val _selectedMovie = SingleLiveData<MovieDataItem>()
    val movieSelected: LiveData<MovieDataItem>
    get() = _selectedMovie

    private val _ratedMovie = SingleLiveData<Boolean>()
    val ratedMovie: LiveData<Boolean>
    get() = _ratedMovie

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

    fun rateMovieAsGuest(movieId: Int, sessionId: String, rate: Double) {
        viewModelScope.launch {

            try {
                 val response = networkMovieRepository.rateMovieAsGuest(movieId, sessionId, RateMovieRequest(value=rate))

                Timber.i("Rating movie: ${movieId} with session ${sessionId} with rate ${rate}")
                response.let {
                    if(response.success){
                        Timber.i("Movie rated!")
                        _ratedMovie.postValue(response.success)
                    }else{
                        Timber.i("Movie rate failed. ${response.statusMessage}")
                        _ratedMovie.postValue(false)
                    }
                }

            }catch(exception: Exception){
                Timber.i("Problem rating movie: ${exception.localizedMessage}")
            }
        }
    }
}