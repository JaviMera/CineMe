package com.merajavier.cineme.login

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.merajavier.cineme.R
import com.merajavier.cineme.databinding.FragmentLoginBinding
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class LoginFragment : Fragment() {

    private lateinit var binding: FragmentLoginBinding
    private val _loginViewModel: LoginViewModel by sharedViewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLoginBinding.inflate(inflater, container,false)
        binding.lifecycleOwner = this

        binding.loginSignInGuest.setOnClickListener{
            _loginViewModel.signInAsGuest()
        }

        _loginViewModel.isLogged.observe(requireActivity(), Observer {
            it.let {
                lifecycleScope.launch {
                    findNavController().navigate(LoginFragmentDirections.actionNavigationLoginToUserFragment())
                }
            }
        })
        return binding.root
    }
}