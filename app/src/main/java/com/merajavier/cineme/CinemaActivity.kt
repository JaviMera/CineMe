package com.merajavier.cineme

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.widget.doOnTextChanged
import androidx.lifecycle.Observer
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.paging.ExperimentalPagingApi
import androidx.work.*
import com.google.android.material.snackbar.Snackbar
import com.merajavier.cineme.databinding.ActivityCinemaBinding
import com.merajavier.cineme.login.LoginViewModel
import com.merajavier.cineme.movies.search.SearchMoviesViewModel
import com.merajavier.cineme.network.ConnectivityLiveData
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.concurrent.TimeUnit

@ExperimentalPagingApi
class CinemaActivity : AppCompatActivity() {

    private lateinit var _binding: ActivityCinemaBinding
    private val loginViewModel: LoginViewModel by viewModel()
    private val searchViewModel: SearchMoviesViewModel by viewModel()
    private val messageViewModel: MessageViewModel by viewModel()

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

        binding.cancelSearchIcon.setOnClickListener {
            binding.fragmentSearchEditText.setText("")
        }

        val connectivityLiveData = ConnectivityLiveData(this.application)
        connectivityLiveData.observe(this, Observer {
            it?.let { isConnected ->
                binding.fragmentSearchEditText.isEnabled = isConnected

                if(!isConnected){
                    showSnackBar(getString(R.string.connection_error))
                }
            }
        })

        messageViewModel.snackBarMessage.observe(this, Observer {
            showSnackBar(it)
        })

        loginViewModel.snackMessage.observe(this, Observer {
            showSnackBar(it)
        })
    }

    private fun showSnackBar(message: String){
        Snackbar
            .make(binding.navView,
                message,
                Snackbar.LENGTH_LONG)
            .setAnchorView(binding.navView)
            .setTextColor(ContextCompat.getColor(this, R.color.white))
            .setBackgroundTint(ContextCompat.getColor(this, R.color.colorPrimary))
            .show()
    }

    fun isDarkModeOn(): Boolean {
        val currentNightMode = resources.configuration.uiMode and android.content.res.Configuration.UI_MODE_NIGHT_MASK
        return currentNightMode == android.content.res.Configuration.UI_MODE_NIGHT_YES
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = navHostFragment.navController
        return navController.navigateUp() || super.onSupportNavigateUp()
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