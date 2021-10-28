package com.merajavier.cineme.login

import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import com.merajavier.cineme.CinemaActivity
import com.merajavier.cineme.R
import com.merajavier.cineme.databinding.FragmentUserBinding
import com.merajavier.cineme.login.account.AccountViewModel
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import timber.log.Timber

class UserFragment : Fragment() {
    private lateinit var binding: FragmentUserBinding
    private val loginViewModel: LoginViewModel by sharedViewModel()
    private val accountViewModel: AccountViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentUserBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = this

        val activity = requireActivity() as CinemaActivity
        activity.supportActionBar.let{
            if (it != null) {
                it.title = loginViewModel.userSession.username
            }
        }

        Timber.i(loginViewModel.userSession.toString())

        accountViewModel.getFavoriteMovies(
            loginViewModel.userSession.accountId,
            loginViewModel.userSession.sessionId
        )

        accountViewModel.favoriteMovies.observe(viewLifecycleOwner, Observer {
            it.let {

                Toast.makeText(requireContext(), it.size.toString(), Toast.LENGTH_SHORT)
                    .show()
            }
        })

        setHasOptionsMenu(true)
        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.fragment_user_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when(item.itemId){
            R.id.user_logout -> {
                lifecycleScope.launch {
//                    findNavController().navigate(UserFragmentDirections.actionUserFragmentToNavigationLogin())
                }
            }
        }
        return super.onOptionsItemSelected(item)
    }
}