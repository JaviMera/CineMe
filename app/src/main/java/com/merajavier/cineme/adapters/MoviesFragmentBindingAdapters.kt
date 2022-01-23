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

@BindingAdapter("showAverageProgress")
fun bindAverageProgress(circularProgressIndicator: CircularProgressIndicator, average: Double){

    circularProgressIndicator.progress = average
        .toPercentAverage()
        .toInt()
}

@BindingAdapter("userScore")
fun setUserScore(userScoreView: UserScoreView, average: Double?){
    average?.let {
        userScoreView.userScoreProgress = it.toPercentAverage()
    }
}

@BindingAdapter("showVoteAverage")
fun bindAverageScore(textView: TextView, average: Double){
    val context = textView.context
    textView.text = context.getString(R.string.details_movie_user_average_value, average.toPercentAverage().toInt())
}

@BindingAdapter("showReleaseDate")
fun bindReleaseDate(textView: TextView, releaseDate: String?){
    releaseDate?.let{
        val movieDateFormat = it.toMovieDateFormat()
        textView.text = movieDateFormat
    }
}