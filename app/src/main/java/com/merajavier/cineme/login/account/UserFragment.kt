package com.merajavier.cineme.login.account

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.os.Bundle
import android.view.*
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadState
import androidx.paging.map
import com.google.android.material.snackbar.Snackbar
import com.merajavier.cineme.CinemaActivity
import com.merajavier.cineme.R
import com.merajavier.cineme.databinding.FragmentUserBinding
import com.merajavier.cineme.login.LoginViewModel
import com.merajavier.cineme.movies.MoviesFooterAdapter
import com.merajavier.cineme.movies.favorites.FavoriteMoviesAdapter
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import timber.log.Timber

@ExperimentalPagingApi
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
            accountViewModel.addMovieToFavorites(loginViewModel.userSession.sessionId,it.id,false)
        })

        binding.recycleViewMovies?.adapter = favoriteMoviesAdapter.withLoadStateFooter(MoviesFooterAdapter())

        val activity = requireActivity() as CinemaActivity
        activity.supportActionBar.let{
            if (it != null) {
                it.title = loginViewModel.userSession.username
            }
        }

        accountViewModel.fetchMovies(
            loginViewModel.userSession.sessionId,
            loginViewModel.userSession.accountId
        ).observe(viewLifecycleOwner, Observer {
            lifecycleScope.launch {
                it?.let{ movies ->
                    favoriteMoviesAdapter.submitData(movies)
                }
            }
        })

        favoriteMoviesAdapter.addLoadStateListener { loadState ->
            if (loadState.source.refresh is LoadState.NotLoading && loadState.append.endOfPaginationReached && favoriteMoviesAdapter.itemCount < 1) {
                binding.fragmentUserNoDataText.visibility = View.VISIBLE
            } else {
                binding.fragmentUserNoDataText.visibility = View.GONE
            }
        }

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
                        favoriteMoviesAdapter.refresh()
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

        loginViewModel.isLogged.observe(viewLifecycleOwner, Observer {
            if(it == false){
                lifecycleScope.launchWhenResumed {

                    val sharedPreferencesEditor = requireActivity().getSharedPreferences("LOGIN_PREFERENCES", MODE_PRIVATE)
                        .edit()

                    sharedPreferencesEditor.remove("USERNAME")
                    sharedPreferencesEditor.apply()

                    findNavController().navigate(UserFragmentDirections.actionUserFragmentToNavigationLogin())
                }
            }
        })

        setHasOptionsMenu(true)
        return binding.root
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        requireActivity().onBackPressedDispatcher.addCallback(
            this,
            object: OnBackPressedCallback(true){
            override fun handleOnBackPressed() {
                findNavController().navigate(UserFragmentDirections.actionUserFragmentToNavigationMovies())
            }
        })
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.fragment_user_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when(item.itemId){
            R.id.user_logout -> {
                loginViewModel.logout()
            }
        }
        return super.onOptionsItemSelected(item)
    }
}