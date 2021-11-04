package com.merajavier.cineme.network

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.annotation.RequiresPermission
import androidx.lifecycle.LiveData
import androidx.paging.ExperimentalPagingApi
import timber.log.Timber


@ExperimentalPagingApi
class ConnectivityLiveData private constructor(private val connectivityManager: ConnectivityManager) :
    LiveData<Boolean>() {

    @RequiresPermission(android.Manifest.permission.ACCESS_NETWORK_STATE)
    constructor(application: Application) : this(
        application.getSystemService(Context.CONNECTIVITY_SERVICE)
                as ConnectivityManager
    )

    private val networkCallback = object : ConnectivityManager.NetworkCallback() {

        override fun onAvailable(network: Network) {
            Timber.i("Network available")
            postValue(true)
        }

        override fun onLost(network: Network) {
            Timber.i("Network not available")
            postValue(false)
        }
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onActive() {
        super.onActive()

        //check availability
        val networkAvailability =
            connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
        networkAvailability?.let {
            if (
                networkAvailability.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) &&
                networkAvailability.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED)
            ) postValue(true) else postValue(false)
        } ?: run {
            postValue(false)
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) connectivityManager.registerDefaultNetworkCallback(
            networkCallback
        )
        else {
            val builder = NetworkRequest.Builder()
            connectivityManager.registerNetworkCallback(builder.build(), networkCallback)
        }
    }

    override fun onInactive() {
        super.onInactive()
        connectivityManager.unregisterNetworkCallback(networkCallback)
    }
}