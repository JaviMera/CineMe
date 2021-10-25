package com.merajavier.cineme.login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.Observer
import com.merajavier.cineme.CinemaActivity
import com.merajavier.cineme.R
import com.merajavier.cineme.databinding.ActivityLoginBinding
import org.koin.androidx.viewmodel.ext.android.viewModel
class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private val loginViewModel: LoginViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.lifecycleOwner = this

        binding.loginSignInGuest.setOnClickListener{
            loginViewModel.signInAsGuest()
        }

        loginViewModel.isLogged.observe(this, Observer {
            it.let {
                val intent = Intent(this, CinemaActivity::class.java)
                startActivity(intent)
            }
        })
    }
}