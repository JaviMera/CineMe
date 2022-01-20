package com.merajavier.cineme.details

import android.animation.ValueAnimator
import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.animation.Animation
import android.view.animation.Transformation
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import com.google.android.material.progressindicator.CircularProgressIndicator
import com.merajavier.cineme.R

class UserScoreView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : ConstraintLayout(context, attrs) {

    private var userScorePercentage: TextView
    private var progressBar: CircularProgressIndicator

    init{
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        inflater.inflate(R.layout.movie_user_score, this)

        userScorePercentage = findViewById(R.id.details_movie_user_score_percentage)
        progressBar = findViewById(R.id.details_movie_user_score_progress)
    }

    var userScoreProgress: Double = 0.0
        set(value){
        field = value
        val progressBarAnimator = ProgressBarAnimation(
            progressBar,
            0.0,
            field
        )
        progressBarAnimator.duration = 2000
        progressBar.startAnimation(progressBarAnimator)

        val progressPercentAnimator = ValueAnimator.ofInt(0, field.toInt())
        progressPercentAnimator.duration = 2000
        progressPercentAnimator.addUpdateListener { animation ->
            userScorePercentage.text = animation.animatedValue.toString()
        }
        progressPercentAnimator.start()
    }

    class ProgressBarAnimation(
        private val progressBar: CircularProgressIndicator,
        private val from: Double,
        private val to: Double
    ) : Animation() {

        override fun applyTransformation(interpolatedTime: Float, t: Transformation?) {
            super.applyTransformation(interpolatedTime, t)
            val value = from + (to - from) * interpolatedTime
            progressBar.progress = value.toInt()
        }
    }
}

