package com.merajavier.cineme.details

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.merajavier.cineme.R
import com.merajavier.cineme.databinding.ActivityDetailsBinding
import timber.log.Timber

class DetailsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val movieId = intent.getIntExtra(SELECTED_MOVIE_ID, 1)
        Timber.i(if(movieId != 1) "Movie id: $movieId" else "unable to retrieve movie id")
    }

    companion object{
        const val SELECTED_MOVIE_ID = "SELECTED_MOVIE_ID"
    }
}