package com.merajavier.cineme.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.merajavier.cineme.network.NetworkGuestSessionRepository
import com.merajavier.cineme.network.NetworkLoginRepositoryInterface
import kotlinx.coroutines.launch

class LoginViewModel(
    private val guestSessionRepository: NetworkLoginRepositoryInterface
) : ViewModel() {

    private var _isLogged = MutableLiveData<Boolean>()
    val isLogged: LiveData<Boolean>
    get() = _isLogged

    fun signInAsGuest() {
        viewModelScope.launch {
            _isLogged.postValue(guestSessionRepository.getGuestSession().success)
        }
    }
}