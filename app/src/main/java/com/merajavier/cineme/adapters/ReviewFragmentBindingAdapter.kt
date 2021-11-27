package com.merajavier.cineme.adapters

import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.merajavier.cineme.R
import com.merajavier.cineme.common.toMovieDateFormat
import com.merajavier.cineme.movies.reviews.AuthorDetails
import timber.log.Timber
import java.text.SimpleDateFormat
import java.util.*

@BindingAdapter("reviewAuthorName")
fun bindReviewAuthorName(textView: TextView, authorDetails: AuthorDetails?){

    authorDetails?.let {
        textView.text = textView.context.getString(R.string.movie_review_author_name, it.username)
    }
}

@BindingAdapter("showReviewDate")
fun bindReviewDate(textView: TextView, releaseDate: String?){

    releaseDate?.let{
        if(it.isNotEmpty()){
            val datePart = it.split("T")

            if(datePart.isNotEmpty() && datePart.any()){
                textView.text = textView.context.getString(R.string.movie_review_date, datePart[0].toMovieDateFormat())
            }
        }else{
            Timber.i("Empty date: $it")
        }
    }
}