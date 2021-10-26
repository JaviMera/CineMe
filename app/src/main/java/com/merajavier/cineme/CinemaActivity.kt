package com.merajavier.cineme

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.navigation.NavigationBarView
import com.merajavier.cineme.databinding.ActivityCinemaBinding
import com.merajavier.cineme.login.LoginFragmentDirections
import com.merajavier.cineme.login.LoginViewModel
import kotlinx.coroutines.launch
import timber.log.Timber
import org.koin.androidx.viewmodel.ext.android.viewModel

class CinemaActivity : AppCompatActivity() {

    private lateinit var _binding: ActivityCinemaBinding
    private val _loginViewModel: LoginViewModel by viewModel()

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
    }

    override fun onSupportNavigateUp(): Boolean {
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment_activity_cinema) as NavHostFragment
        val navController = navHostFragment.navController
        return navController.navigateUp() || super.onSupportNavigateUp()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        if(_binding.navView.selectedItemId == R.id.navigation_login && _loginViewModel.isLogged.value == true){
            val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment_activity_cinema) as NavHostFragment
            val navController = navHostFragment.navController
            lifecycleScope.launch {
                navController.navigate(LoginFragmentDirections.actionNavigationLoginToNavigationMovies())
            }
        }
    }
}