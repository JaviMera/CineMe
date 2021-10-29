package com.merajavier.cineme.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.merajavier.cineme.data.local.LocalAccountRepositoryInterface
import com.merajavier.cineme.data.local.UserSessionEntity
import com.merajavier.cineme.login.authentication.CreateSessionRequest
import com.merajavier.cineme.login.authentication.DeleteSessionRequest
import com.merajavier.cineme.login.authentication.ValidateTokenWithLoginRequest
import com.merajavier.cineme.movies.SingleLiveData
import com.merajavier.cineme.network.NetworkAccountRepositoryInterface
import com.merajavier.cineme.network.NetworkAuthenticationRepositoryInterface
import com.merajavier.cineme.network.NetworkLoginRepositoryInterface
import kotlinx.coroutines.launch
import timber.log.Timber
import java.lang.Exception

data class UserSession(
    val sessionId: String = "",
    val accountId: Int = 0,
    val username: String = ""
)

class LoginViewModel(
    private val guestSessionRepository: NetworkLoginRepositoryInterface,
    private val accountRepository: NetworkAccountRepositoryInterface,
    private val authenticationRepository: NetworkAuthenticationRepositoryInterface,
    private val localAccountRepositoryInterface: LocalAccountRepositoryInterface
) : ViewModel() {

    private var _isLogged = MutableLiveData<Boolean>()
    val isLogged: LiveData<Boolean>
    get() = _isLogged

    private var _userSession = UserSession()
    val userSession: UserSession
    get() = _userSession

    fun signInAsGuest() {
        viewModelScope.launch {
            val response  = guestSessionRepository.getGuestSession()
            _isLogged.postValue(response.success)
        }
    }

    fun signInAsUser(username: String, password: String) {
        viewModelScope.launch {
            val tokenResponse = authenticationRepository.createToken()

            try{
                if(tokenResponse.success){
                    val validateResponse = authenticationRepository.validateToken(
                        ValidateTokenWithLoginRequest(username, password, tokenResponse.requestToken)
                    )

                    if(validateResponse.success){
                        val sessionResponse = authenticationRepository.createSession(
                            CreateSessionRequest(tokenResponse.requestToken)
                        )

                        if(sessionResponse.success){
                            val accountResponse = accountRepository.getAccountDetails(sessionResponse.sessionId)

                            localAccountRepositoryInterface.createSession(
                                UserSessionEntity(accountResponse.username, sessionResponse.sessionId, accountResponse.id)
                            )
                            _userSession = UserSession(sessionResponse.sessionId, accountResponse.id, accountResponse.username)
                            _isLogged.postValue(true)
                        }else{
                            Timber.i("Unable to create a session")
                        }
                    }else{
                        Timber.i("Unable to validate token")
                    }
                }else{
                    Timber.i("Unable to create a token")
                }
            }catch (exception: Exception){
                Timber.i("Unable to authenticate: ${exception.localizedMessage}")
            }
        }
    }

    fun logout() {
        viewModelScope.launch {
            try{
                val response = authenticationRepository.deleteSession(DeleteSessionRequest(userSession.sessionId))
                if(response.success){
                    localAccountRepositoryInterface.deleteSession(
                        UserSessionEntity(
                        userSession.username, userSession.sessionId, userSession.accountId)
                    )

                    _isLogged.postValue(false)
                }
            }catch(exception: Exception){
                Timber.i("Unable to log out: ${exception.localizedMessage}")
            }
        }
    }

    fun restoreLogin(username: String?) {

        viewModelScope.launch {
            username?.let {
                val credentials = localAccountRepositoryInterface.getSession(username)

                _userSession = UserSession(
                    accountId = credentials.accountId,
                    username = credentials.userName,
                    sessionId = credentials.sessionId
                )

                _isLogged.postValue(true)
            }
        }
    }
}