package com.merajavier.cineme

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ActivityViewModel() : ViewModel() {

    private val _snackBarMessage = MutableLiveData<String>()
    val snackBarMessage: LiveData<String>
    get() = _snackBarMessage

    private val _appBarTitle = MutableLiveData<String>()
    val appBarTitle: LiveData<String>
    get() = _appBarTitle

    fun setMessage(message: String){
        _snackBarMessage.value = message
    }

    fun setAppBarTitle(title: String){
        _appBarTitle.value = title
    }
}