package com.merajavier.cineme.cast

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.merajavier.cineme.network.NetworkMovieActorRepository
import kotlinx.coroutines.launch

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
            val response = networkMovieActorRepository.getActors(movieId)
            _actors.postValue(response)
        }
    }

    fun getDirectors(movieId: Int){
        viewModelScope.launch {
            val response = networkMovieActorRepository.getCrew(movieId)
            _crew.postValue(response)
        }
    }
}