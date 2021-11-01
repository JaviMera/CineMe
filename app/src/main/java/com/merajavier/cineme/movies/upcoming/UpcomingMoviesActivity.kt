package com.merajavier.cineme.movies.upcoming

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import android.widget.Toast.makeText
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.merajavier.cineme.R
import com.merajavier.cineme.cancelNotification
import com.merajavier.cineme.databinding.ActivityUpcomingMoviesBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

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

        val layoutManager = binding.recyclerUpcomingMovies.layoutManager as GridLayoutManager
        // Add a scroll listener to get more items if the user reaches the bottom of the list
        binding.recyclerUpcomingMovies.addOnScrollListener(object: RecyclerView.OnScrollListener(){
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if(dy > 0 && upcomingMoviesViewModel.loading.value == false){
                    if(layoutManager.findLastCompletelyVisibleItemPosition() == layoutManager.itemCount - 1){

                        if(upcomingMoviesViewModel.canFetchMovies()){
                            Snackbar.make(
                                binding.activityUpcomingMoviesConstraintLayout,
                                "You have seen all upcoming movies for today!",
                                Snackbar.LENGTH_LONG
                            )
                                .show()
                        }else{
                            upcomingMoviesViewModel.getUpcomingMovies()
                        }
                    }
                }
            }
        })

        upcomingMoviesViewModel.getUpcomingMovies()
        upcomingMoviesViewModel.upcomingMovies.observe(this, Observer {
            if(it != null){
                adapter.submitList(it)
            }
        })
    }

    override fun onDestroy() {
        super.onDestroy()
        upcomingMoviesViewModel.resetList()
    }
}

