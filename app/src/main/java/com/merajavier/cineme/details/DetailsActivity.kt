package com.merajavier.cineme.details

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.android.material.appbar.AppBarLayout
import com.merajavier.cineme.databinding.ActivityDetailsBinding
import com.merajavier.cineme.movies.MovieDataItem
import com.merajavier.cineme.movies.MovieListViewModel
import org.koin.android.compat.SharedViewModelCompat.sharedViewModel
import timber.log.Timber

class DetailsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        intent?.getParcelableExtra<MovieDataItem>(SELECTED_MOVIE_ID).let {

            if(it != null){
                binding.movie = it
            }
        }

        val listener = AppBarLayout.OnOffsetChangedListener{unsued, verticalOffset ->
            val seekPosition = -verticalOffset / binding.appbarLayout.totalScrollRange.toFloat()
            binding.motionLayout.progress = seekPosition
        }

        binding.appbarLayout.addOnOffsetChangedListener(listener)
    }

    companion object{
        const val SELECTED_MOVIE_ID = "SELECTED_MOVIE_ID"
    }
}