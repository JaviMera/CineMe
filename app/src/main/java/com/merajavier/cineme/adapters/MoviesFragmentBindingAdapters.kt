package com.merajavier.cineme.adapters

import android.util.Log
import android.view.View
import androidx.databinding.BindingAdapter
import com.google.android.material.progressindicator.CircularProgressIndicator

@BindingAdapter("showLoading")
fun bindLoadingBar(circularProgressIndicator: CircularProgressIndicator, isLoading: Boolean){
    when(isLoading){
        true -> {
            circularProgressIndicator.visibility = View.VISIBLE
        }
        false -> {
            circularProgressIndicator.visibility = View.GONE
        }
    }
}
