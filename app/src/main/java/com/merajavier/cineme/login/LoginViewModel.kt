package com.merajavier.cineme.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.merajavier.cineme.movies.SingleLiveData
import com.merajavier.cineme.network.NetworkLoginRepositoryInterface
import kotlinx.coroutines.launch

class LoginViewModel(
    private val guestSessionRepository: NetworkLoginRepositoryInterface
) : ViewModel() {

    private var _isLogged = SingleLiveData<Boolean>()
    val isLogged: LiveData<Boolean>
    get() = _isLogged

    private var _sessionId = MutableLiveData<String>()
    val sessionId: LiveData<String>
    get() = _sessionId

    fun signInAsGuest() {
        viewModelScope.launch {
            val response  = guestSessionRepository.getGuestSession()
            _isLogged.postValue(response.success)
            _sessionId.postValue(response.sessionId)
        }
    }
}