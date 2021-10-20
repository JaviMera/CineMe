package com.merajavier.cineme.adapters

import android.view.View
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.android.material.progressindicator.CircularProgressIndicator
import com.merajavier.cineme.BuildConfig
import com.merajavier.cineme.R

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

@BindingAdapter("showPoster")
fun bindPictureOfDay(imageView: ImageView, posterUrl: String?){
    posterUrl?.let{
        Glide.with(imageView.context)
            .load("${BuildConfig.API_POSTER_URL}${it}")
            .apply(
                RequestOptions()
                .placeholder(R.drawable.loading_image)
                .error(R.drawable.loading_image_error))
            .into(imageView)
    }
}
