package com.merajavier.cineme.details

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import com.merajavier.cineme.R

class UserVotesView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : ConstraintLayout(context, attrs) {

    private var userVotesTextView: TextView

    init {
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        inflater.inflate(R.layout.movie_user_votes, this)
        userVotesTextView = findViewById(R.id.details_movie_user_votes)
    }

    var userVotes: Int = 0
    set(value){
        field = value
        userVotesTextView.text = field.toString()
    }
}