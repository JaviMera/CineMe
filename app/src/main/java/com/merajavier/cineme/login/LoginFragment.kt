package com.merajavier.cineme.login

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.merajavier.cineme.BuildConfig
import com.merajavier.cineme.databinding.FragmentLoginBinding
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class LoginFragment : Fragment() {

    private lateinit var binding: FragmentLoginBinding
    private val loginViewModel: LoginViewModel by sharedViewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLoginBinding.inflate(inflater, container,false)
        binding.lifecycleOwner = this

        if(loginViewModel.isLogged.value == true){
            lifecycleScope.launch {
                findNavController().navigate(LoginFragmentDirections.actionNavigationLoginToUserFragment())
            }
        }else{

            binding.loginSignInGuest.setOnClickListener{
                loginViewModel.signInAsGuest()
            }

            loginViewModel.isLogged.observe(requireActivity(), Observer {

                if(it == true){
                    lifecycleScope.launchWhenResumed {
                        findNavController().navigate(LoginFragmentDirections.actionNavigationLoginToUserFragment())
                    }
                }
            })

            binding.loginSignIn.setOnClickListener {
                loginViewModel.signInAsUser(binding.loginUsername.text.toString(), binding.loginPassword.text.toString())
            }

            binding.loginUsername.setText(BuildConfig.username)
            binding.loginPassword.setText(BuildConfig.password)
        }

        return binding.root
    }
}