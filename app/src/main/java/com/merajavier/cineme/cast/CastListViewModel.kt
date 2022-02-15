package com.merajavier.cineme.cast

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.merajavier.cineme.common.ErrorResponse
import com.merajavier.cineme.common.TMDBApiResult
import com.merajavier.cineme.network.repositories.NetworkMovieActorRepository
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

    private var _actorDetails = MutableLiveData<ActorDetailDataItem>()
    val actorDetails: LiveData<ActorDetailDataItem>
    get() = _actorDetails

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

    fun getActorDetails(actorId: Int){
        viewModelScope.launch {
            try{
                when(val response = networkMovieActorRepository.getActorDetails(actorId, "images,credits")){
                    is TMDBApiResult.Success -> {
                        val actorDetails = response.data as ActorDetailDataItem
                        _actorDetails.postValue(actorDetails)
                    }
                    is TMDBApiResult.Error -> {
                        Timber.i("Unable to get actor details: ${response.message}")
                    }
                    is TMDBApiResult.Failure -> {
                        val errorResponse = response.data as ErrorResponse
                        Timber.i(errorResponse.statusMessage)
                    }
                }
            }
            catch (exception: Exception){
                Timber.i(exception.localizedMessage)
            }
        }
    }
}