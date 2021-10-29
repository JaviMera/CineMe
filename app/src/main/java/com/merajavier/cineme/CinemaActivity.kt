package com.merajavier.cineme

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.merajavier.cineme.databinding.ActivityCinemaBinding
import com.merajavier.cineme.login.LoginViewModel
import timber.log.Timber
import org.koin.androidx.viewmodel.ext.android.viewModel

class CinemaActivity : AppCompatActivity() {

    private lateinit var _binding: ActivityCinemaBinding
    private val loginViewModel: LoginViewModel by viewModel()

    val binding: ActivityCinemaBinding
    get() = _binding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val preferences = getSharedPreferences("LOGIN_PREFERENCES", MODE_PRIVATE)
        Timber.i("Username restored: ${preferences.getString("USERNAME", "")}")

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
        val preferences = getSharedPreferences("LOGIN_PREFERENCES", MODE_PRIVATE)

        if(!preferences.contains("USERNAME")){
            val editor = preferences.edit()
            editor.putString("USERNAME", loginViewModel.userSession.username)
            editor.apply()
        }
    }

    override fun onResume() {
        super.onResume()

        val preferences = getSharedPreferences("LOGIN_PREFERENCES", MODE_PRIVATE)
        if(preferences.contains("USERNAME")){
            loginViewModel.restoreLogin(preferences.getString("USERNAME", ""))
        }
    }
}