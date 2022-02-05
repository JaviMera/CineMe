package com.merajavier.cineme.adapters

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.android.material.progressindicator.CircularProgressIndicator
import com.merajavier.cineme.BuildConfig
import com.merajavier.cineme.R
import com.merajavier.cineme.common.toMovieDateFormat
import com.merajavier.cineme.common.toPercentAverage
import com.merajavier.cineme.details.UserScoreView
import com.merajavier.cineme.details.UserVotesView
import com.merajavier.cineme.movies.MovieDataItem
import timber.log.Timber
import java.text.SimpleDateFormat
import java.util.*

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

    if(posterUrl == null){
        imageView.setImageResource(R.drawable.loading_image_error)
    }else{

        Glide.with(imageView.context)
            .load("${BuildConfig.API_POSTER_URL}${posterUrl}")
            .apply(
                RequestOptions()
                .placeholder(R.drawable.loading_image)
                .error(R.drawable.loading_image_error))
            .into(imageView)
    }
}

@BindingAdapter("showMovieDetails")
fun bindMovieDetails(textView: TextView, movie: MovieDataItem){

    val context = textView.context
    val year = movie.releaseDate.split("-").firstOrNull()
    val certification = movie.releaseDates.results
        .firstOrNull { result -> result.country == "US" }
        ?.releaseDates
        ?.firstOrNull{releaseDate -> releaseDate.certification.isNotEmpty()}
        ?.certification

    val hours = movie.runtime / 60
    val minutes = movie.runtime % 60

    textView.text =  context.getString(R.string.movie_details, year, certification, hours,minutes)
}

@BindingAdapter("showReleaseDate")
fun bindReleaseDate(textView: TextView, releaseDate: String?){
    releaseDate?.let{
        val movieDateFormat = it.toMovieDateFormat()
        textView.text = movieDateFormat
    }
}