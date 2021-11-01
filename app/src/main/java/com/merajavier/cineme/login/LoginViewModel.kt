package com.merajavier.cineme.login

import android.content.SharedPreferences
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.merajavier.cineme.common.ErrorResponse
import com.merajavier.cineme.common.TMDBApiResult
import com.merajavier.cineme.data.local.LocalAccountRepositoryInterface
import com.merajavier.cineme.data.local.UserSessionEntity
import com.merajavier.cineme.login.account.AccountDetailsResponse
import com.merajavier.cineme.login.authentication.*
import com.merajavier.cineme.movies.SingleLiveData
import com.merajavier.cineme.network.NetworkAccountRepositoryInterface
import com.merajavier.cineme.network.NetworkAuthenticationRepositoryInterface
import kotlinx.coroutines.launch
import timber.log.Timber
import java.lang.Exception

data class UserSession(
    val sessionId: String = "",
    val accountId: Int = 0,
    val username: String = ""
)

class LoginViewModel(
    private val accountRepository: NetworkAccountRepositoryInterface,
    private val authenticationRepository: NetworkAuthenticationRepositoryInterface,
    private val localAccountRepositoryInterface: LocalAccountRepositoryInterface,
    private val loginSharedPreferences: SharedPreferences
) : ViewModel() {

    private var _isLogged = MutableLiveData<Boolean>()
    val isLogged: LiveData<Boolean>
    get() = _isLogged

    private var _userSession = UserSession()
    val userSession: UserSession
    get() = _userSession

    private var _snackbarMessage = SingleLiveData<String>()
    val snackbarMessage: LiveData<String>
    get() = _snackbarMessage

    fun signInAsUser(username: String, password: String) {
        viewModelScope.launch {
            when(val tokenResult = authenticationRepository.createToken()){
                is TMDBApiResult.Success -> {
                    val tokenResponse = tokenResult.data as CreateTokenResponse

                    if(tokenResponse.success){
                        when(val authenticationResult = authenticationRepository.validateToken(
                            ValidateTokenWithLoginRequest(username, password, tokenResponse.requestToken)
                        )){
                            is TMDBApiResult.Success -> {
                                val authenticationResponse = authenticationResult.data as ValidateTokenWithLoginResponse

                                if(authenticationResponse.success){
                                    when(val sessionResult = authenticationRepository.createSession(
                                        CreateSessionRequest(authenticationResponse.requestToken)
                                    )){
                                        is TMDBApiResult.Success -> {
                                            val sessionResponse = sessionResult.data as CreateSessionResponse
                                            if(sessionResponse.success){
                                                when(val accountResult = accountRepository.getAccountDetails(sessionResponse.sessionId)){
                                                    is TMDBApiResult.Success -> {
                                                        val accountResponse = accountResult.data as AccountDetailsResponse
                                                        localAccountRepositoryInterface.createSession(
                                                            UserSessionEntity(accountResponse.username, sessionResponse.sessionId, accountResponse.id)
                                                        )

                                                        _userSession = UserSession(sessionResponse.sessionId, accountResponse.id, accountResponse.username)
                                                        _isLogged.postValue(true)
                                                    }
                                                    is TMDBApiResult.Failure ->{

                                                        val failureResponse = accountResult.data as ErrorResponse
                                                        Timber.i(failureResponse.statusMessage)
                                                        _snackbarMessage.postValue("Unable to login. Try again")
                                                    }
                                                    is TMDBApiResult.Error -> {
                                                        Timber.i(accountResult.message)
                                                        _snackbarMessage.postValue("Unable to login. Try again")
                                                    }
                                                }
                                            }
                                        }
                                        is TMDBApiResult.Failure ->{

                                            val failureResponse = authenticationResult.data as ErrorResponse
                                            Timber.i(failureResponse.statusMessage)
                                            _snackbarMessage.postValue("Unable to login. Try again")
                                        }
                                        is TMDBApiResult.Error -> {
                                            Timber.i(sessionResult.message)
                                            _snackbarMessage.postValue("Unable to login. Try again")
                                        }
                                    }
                                }
                            }
                            is TMDBApiResult.Failure ->{
                                val failureResponse = authenticationResult.data as ErrorResponse
                                Timber.i(failureResponse.statusMessage)
                                _snackbarMessage.postValue(failureResponse.statusMessage)
                            }
                            is TMDBApiResult.Error -> {
                                Timber.i(authenticationResult.message)
                                _snackbarMessage.postValue("Unable to login. Try again")
                            }
                        }
                    }
                }
                is TMDBApiResult.Failure ->{
                    val failureResponse = tokenResult.data as ErrorResponse
                    Timber.i(failureResponse.statusMessage)
                    _snackbarMessage.postValue("Unable to login. Try again")
                }
                is TMDBApiResult.Error -> {
                    Timber.i(tokenResult.message)
                    _snackbarMessage.postValue("Unable to login. Try again")
                }
            }
        }
    }

    fun logout() {
        viewModelScope.launch {
            try{
                when(val deleteSessionResult = authenticationRepository.deleteSession(DeleteSessionRequest(userSession.sessionId))){
                    is TMDBApiResult.Success -> {
                        val deleteSessionResponse = deleteSessionResult.data as DeleteSessionResponse
                        if(deleteSessionResponse.success){

                            localAccountRepositoryInterface.deleteSession(
                                UserSessionEntity(
                                userSession.username, userSession.sessionId, userSession.accountId)
                            )

                            loginSharedPreferences
                                .edit()
                                .remove(LOGIN_USERNAME_KEY)
                                .apply()

                            _isLogged.postValue(false)
                        }
                    }
                    is TMDBApiResult.Failure ->{

                    }
                    is TMDBApiResult.Error -> {

                    }
                }
            }catch(exception: Exception){
                Timber.i("Unable to log out: ${exception.localizedMessage}")
            }
        }
    }

    fun restoreLogin() {

        viewModelScope.launch {
            val username = loginSharedPreferences.getString(LOGIN_USERNAME_KEY, "")

            if(username.isNullOrEmpty()){
                _isLogged.postValue(false)
            }else{

                localAccountRepositoryInterface.getSession(username)?.let { credentials ->

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

    fun saveLogin() {

        if(!loginSharedPreferences.contains(LOGIN_USERNAME_KEY)){

            val editor = loginSharedPreferences
                .edit()

            editor.putString(LOGIN_USERNAME_KEY, userSession.username)
            editor.apply()
        }
    }

    companion object {
        private const val LOGIN_USERNAME_KEY = "username"
    }
}