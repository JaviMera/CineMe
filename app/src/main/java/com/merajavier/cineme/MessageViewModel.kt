package com.merajavier.cineme

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MessageViewModel() : ViewModel() {

    private val _snackBarMessage = MutableLiveData<String>()
    val snackBarMessage: LiveData<String>
    get() = _snackBarMessage

    fun setMessage(message: String){
        _snackBarMessage.value = message
    }
}