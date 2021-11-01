package com.merajavier.cineme.cast

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.merajavier.cineme.common.ErrorResponse
import com.merajavier.cineme.common.TMDBApiResult
import com.merajavier.cineme.network.NetworkMovieActorRepository
import kotlinx.coroutines.launch
import timber.log.Timber

class CastListViewModel(
    private val networkMovieActorRepository: NetworkMovieActorRepository
) : ViewModel() {

    private var _actors = MutableLiveData<List<ActorDataItem>>()
    val actors: LiveData<List<ActorDataItem>>
    get() = _actors

    private var _crew = MutableLiveData<List<CrewDataItem>>()
    val crew: LiveData<List<CrewDataItem>>
    get() = _crew

    fun getMovieActors(movieId: Int){
        viewModelScope.launch {
            try{
                when(val response = networkMovieActorRepository.getActors(movieId)){
                    is TMDBApiResult.Success -> {
                        val actors = response.data as List<ActorDataItem>
                        _actors.postValue(actors)
                    }
                    is TMDBApiResult.Error -> {
                        Timber.i("Unable to get upcoming movies: ${response.message}")
                    }
                    is TMDBApiResult.Failure -> {
                        val errorResponse = response.data as ErrorResponse
                        Timber.i(errorResponse.statusMessage)
                    }
                }
            }catch(exception: Exception){
                Timber.i(exception.localizedMessage)
            }
        }
    }

    fun getDirectors(movieId: Int){
        viewModelScope.launch {
            try{
                when(val response = networkMovieActorRepository.getCrew(movieId)){
                    is TMDBApiResult.Success -> {
                        val crew = response.data as List<CrewDataItem>
                        _crew.postValue(crew)
                    }
                    is TMDBApiResult.Error -> {
                        Timber.i("Unable to get upcoming movies: ${response.message}")
                    }
                    is TMDBApiResult.Failure -> {
                        val errorResponse = response.data as ErrorResponse
                        Timber.i(errorResponse.statusMessage)
                    }
                }
            }catch(exception: Exception){
                Timber.i(exception.localizedMessage)
            }
        }
    }
}