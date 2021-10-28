package com.merajavier.cineme.login.account

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import com.google.android.material.snackbar.Snackbar
import com.merajavier.cineme.CinemaActivity
import com.merajavier.cineme.R
import com.merajavier.cineme.databinding.FragmentUserBinding
import com.merajavier.cineme.login.LoginViewModel
import com.merajavier.cineme.movies.favorites.FavoriteMoviesAdapter
import com.merajavier.cineme.movies.favorites.MarkFavoriteRequest
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import timber.log.Timber

class UserFragment : Fragment() {
    private lateinit var binding: FragmentUserBinding
    private val loginViewModel: LoginViewModel by sharedViewModel()
    private val accountViewModel: AccountViewModel by viewModel()
    private lateinit var favoriteMoviesAdapter: FavoriteMoviesAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentUserBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = this

        favoriteMoviesAdapter = FavoriteMoviesAdapter(FavoriteMoviesAdapter.OnFavoriteRemoveClickListener {
            accountViewModel.addMovieToFavorites(
                loginViewModel.userSession.sessionId,
                MarkFavoriteRequest("movie", it, false)
            )
        })

        binding.recycleViewMovies.adapter = favoriteMoviesAdapter

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
                favoriteMoviesAdapter.submitList(it)
            }
        })

        accountViewModel.movieUpdated.observe(viewLifecycleOwner, Observer {

            if(it == null){
                Snackbar.make(
                    binding.favoriteMoviesConstraintLayout,
                    "Unable to remove movie",
                    Snackbar.LENGTH_SHORT
                )
                    .show()
            }else{
                when(it){
                    MarkFavoriteStatus.DONE -> {

                        accountViewModel.getFavoriteMovies(
                            loginViewModel.userSession.accountId,
                            loginViewModel.userSession.sessionId
                        )

                        Snackbar.make(
                            binding.favoriteMoviesConstraintLayout,
                            "Favorites Updated!",
                            Snackbar.LENGTH_SHORT
                        )
                            .show()
                    }
                    MarkFavoriteStatus.FAILED -> {
                        Snackbar.make(
                            binding.favoriteMoviesConstraintLayout,
                            "Unable to remove movie",
                            Snackbar.LENGTH_SHORT
                        )
                            .show()
                    }
                }
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