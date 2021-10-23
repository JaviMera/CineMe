package com.merajavier.cineme.cast

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.merajavier.cineme.network.NetworkMovieActorRepository
import kotlinx.coroutines.launch

class ActorListViewModel(
    private val networkMovieActorRepository: NetworkMovieActorRepository
) : ViewModel() {

    private var _actors = MutableLiveData<List<ActorDataItem>>()
    val actors: LiveData<List<ActorDataItem>>
    get() = _actors

    fun getMovieActors(movieId: Int){
        viewModelScope.launch {
            val response = networkMovieActorRepository.getAll(movieId)
            _actors.postValue(response)
        }
    }
}