package com.merajavier.cineme

import android.net.ConnectivityManager
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.core.widget.doOnTextChanged
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.paging.ExperimentalPagingApi
import androidx.work.*
import com.merajavier.cineme.data.local.NowPlayingMoviesDao
import com.merajavier.cineme.databinding.ActivityCinemaBinding
import com.merajavier.cineme.login.LoginViewModel
import com.merajavier.cineme.movies.search.SearchMoviesFragment
import com.merajavier.cineme.movies.search.SearchMoviesViewModel
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import timber.log.Timber
import java.util.concurrent.TimeUnit

@ExperimentalPagingApi
class CinemaActivity : AppCompatActivity() {

    private lateinit var _binding: ActivityCinemaBinding
    private val loginViewModel: LoginViewModel by viewModel()
    private val searchViewModel: SearchMoviesViewModel by viewModel()

    private val navHostFragment: NavHostFragment by lazy {supportFragmentManager.findFragmentById(R.id.nav_host_fragment_activity_cinema) as NavHostFragment}
    val binding: ActivityCinemaBinding
    get() = _binding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        _binding = ActivityCinemaBinding.inflate(layoutInflater)
        setContentView(_binding.root)

        val navController = navHostFragment.navController
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_movies, R.id.navigation_search_movies, R.id.navigation_login, R.id.userFragment
            )
        )

        setSupportActionBar(binding.activityCinemaToolbar)
        setupActionBarWithNavController(navController, appBarConfiguration)
        _binding.navView.setupWithNavController(navController)

        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.UNMETERED)
            .build()

        val request = PeriodicWorkRequestBuilder<UpcomingMoviesWorker>(1, TimeUnit.DAYS)
            .setConstraints(constraints)
            .build()

        WorkManager
            .getInstance(applicationContext)
            .enqueueUniquePeriodicWork(
                UpcomingMoviesWorker.WORKER_NAME,
                ExistingPeriodicWorkPolicy.KEEP,
                request
            )

        val nowPlayingMoviesRequest = PeriodicWorkRequestBuilder<NowPlayingMoviesWorker>(1, TimeUnit.DAYS)
            .setConstraints(constraints)
            .build()

        WorkManager
            .getInstance(applicationContext)
            .enqueueUniquePeriodicWork(
                NowPlayingMoviesWorker.WORKER_NAME,
                ExistingPeriodicWorkPolicy.KEEP,
                nowPlayingMoviesRequest
            )

        navController.addOnDestinationChangedListener { controller, destination, arguments ->
            if(destination.id == R.id.navigation_search_movies){
                binding.fragmentSearchCardView.visibility = View.VISIBLE
            }else{
                binding.fragmentSearchCardView.visibility = View.GONE
            }
        }

        binding.fragmentSearchEditText.doOnTextChanged { text, start, before, count ->
            searchViewModel.queryTitle(text.toString())
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = navHostFragment.navController
        return navController.navigateUp() || super.onSupportNavigateUp()
    }

    // Disable base back press functionality
    override fun onBackPressed() {
    }

    override fun onPause() {
        super.onPause()
        loginViewModel.saveLogin()
    }

    override fun onResume() {
        super.onResume()
        loginViewModel.restoreLogin()
    }
}