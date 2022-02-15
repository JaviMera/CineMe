package com.merajavier.cineme.adapters

import android.text.SpannableStringBuilder
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.core.text.bold
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.android.material.progressindicator.CircularProgressIndicator
import com.merajavier.cineme.BuildConfig
import com.merajavier.cineme.R
import com.merajavier.cineme.cast.ActorProfileImagesResponse
import com.merajavier.cineme.common.toActorYears
import com.merajavier.cineme.common.toDateFormat
import com.merajavier.cineme.common.toYear
import com.merajavier.cineme.movies.MovieDataItem

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
        val movieDateFormat = it.toDateFormat()
        textView.text = movieDateFormat
    }
}

@BindingAdapter("showActorImageProfile")
fun bindActorImageProfile(imageView: ImageView, imagesResponse: ActorProfileImagesResponse?){

    if(imagesResponse?.profiles == null || !imagesResponse.profiles.any() ){
        imageView.setImageResource(R.drawable.loading_image_error)
    }else{

        val imagePath = imagesResponse.profiles.maxByOrNull { image -> image.VoteCount }

        if(imagePath == null){
            imageView.setImageResource(R.drawable.loading_image_error)
        }else{
            Glide.with(imageView.context)
                .load("${BuildConfig.API_POSTER_URL}${imagePath.filePath}")
                .apply(
                    RequestOptions()
                        .placeholder(R.drawable.loading_image)
                        .error(R.drawable.loading_image_error))
                .into(imageView)
        }
    }
}

@BindingAdapter("showBirthdate")
fun bindBirthdate(textView: TextView, birthdate: String?){
    birthdate?.let{
        textView.text = getActorDate(
            textView.context.getString(R.string.actor_details_birthdate_title),
            it
        )
    }
}

@BindingAdapter("actorCreditYear", "actorCreditCharacter")
fun bindActorCreditDescription(textView: TextView, actorCreditYear: String?, actorCreditCharacter: String?){

    if(actorCreditYear == null && actorCreditCharacter == null)
        return

    textView.text = textView.context.getString(
        R.string.actor_credits_description,
        actorCreditYear?.toYear(),
        actorCreditCharacter
    )
}

@BindingAdapter("birthday", "deathday")
fun bindDeathDate(textView: TextView, birthday: String?, deathday: String?){

    if(!birthday.isNullOrEmpty() && !deathday.isNullOrEmpty()){
        deathday.let{
            textView.text = getActorDate(
                textView.context.getString(R.string.actor_details_deathdate_title),
                it
            )
                .append(birthday.toActorYears())
        }
    }
}

private fun getActorDate(title: String, date: String): SpannableStringBuilder {

    return SpannableStringBuilder()
        .bold { append(title) }
        .append(" ${date.toDateFormat()}")
}