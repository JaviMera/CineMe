package com.merajavier.cineme.movies.upcoming

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.paging.ExperimentalPagingApi
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.merajavier.cineme.R
import com.merajavier.cineme.cancelNotification
import com.merajavier.cineme.databinding.ActivityUpcomingMoviesBinding
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

@ExperimentalPagingApi
class UpcomingMoviesActivity : AppCompatActivity() {

    private lateinit var binding: ActivityUpcomingMoviesBinding
    private lateinit var adapter: UpcomingMoviesAdapter
    private val upcomingMoviesViewModel: UpcomingMoviesViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUpcomingMoviesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.lifecycleOwner = this
        adapter = UpcomingMoviesAdapter()
        binding.recyclerUpcomingMovies.adapter = adapter
        val notificationId = intent.getIntExtra(getString(R.string.notification_id_key), -1)
        if(notificationId != -1){
            cancelNotification(this, notificationId)
        }

        upcomingMoviesViewModel.fetchMovies().observe(this, Observer {
            lifecycleScope.launch {
                adapter.submitData(it)
            }
        })
    }
}

