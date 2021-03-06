package com.merajavier.cineme.adapters

import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.merajavier.cineme.R
import com.merajavier.cineme.common.toDateFormat
import com.merajavier.cineme.movies.reviews.AuthorDetails
import timber.log.Timber

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
                textView.text = textView.context.getString(R.string.movie_review_date, datePart[0].toDateFormat())
            }
        }else{
            Timber.i("Empty date: $it")
        }
    }
}

@BindingAdapter("showReviewScore")
fun bindReviewScore(textView: TextView, reviewScore: Double?){

    reviewScore?.let { score ->
        if(reviewScore.compareTo(0.0) != 0){
            textView.text = score.toString()
        }
    }
}