package com.merajavier.cineme

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.work.*
import com.merajavier.cineme.common.ErrorResponse
import com.merajavier.cineme.common.TMDBApiResult
import com.merajavier.cineme.databinding.ActivityCinemaBinding
import com.merajavier.cineme.login.LoginViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import timber.log.Timber
import java.util.concurrent.TimeUnit

class CinemaActivity : AppCompatActivity() {

    private lateinit var _binding: ActivityCinemaBinding
    private val loginViewModel: LoginViewModel by viewModel()

    val binding: ActivityCinemaBinding
    get() = _binding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        _binding = ActivityCinemaBinding.inflate(layoutInflater)
        setContentView(_binding.root)

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment_activity_cinema) as NavHostFragment
        val navController = navHostFragment.navController
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_movies, R.id.navigation_tv_shows, R.id.navigation_login, R.id.userFragment
            )
        )

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

        WorkManager.getInstance(this)
            .getWorkInfoByIdLiveData(request.id)
            .observe(this, Observer {
                when (it.state) {
                    WorkInfo.State.SUCCEEDED -> {
                        Timber.i("Worker succeeded")
                    }
                    WorkInfo.State.FAILED -> {
                        Timber.i("Worker cancelled")
                    }
                    WorkInfo.State.RUNNING -> {
                        Timber.i("Worker running")
                    }
                }
            })
    }

    override fun onSupportNavigateUp(): Boolean {
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment_activity_cinema) as NavHostFragment
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