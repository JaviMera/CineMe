package com.merajavier.cineme.details

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.paging.ExperimentalPagingApi
import com.merajavier.cineme.MessageViewModel
import com.merajavier.cineme.R
import com.merajavier.cineme.cast.ActorsRecyclerAdapter
import com.merajavier.cineme.cast.CastListViewModel
import com.merajavier.cineme.databinding.FragmentMovieDetailsBinding
import com.merajavier.cineme.genre.GenresRecyclerAdapter
import com.merajavier.cineme.login.LoginViewModel
import com.merajavier.cineme.login.account.AccountViewModel
import com.merajavier.cineme.login.account.MarkFavoriteStatus
import com.merajavier.cineme.movies.MovieListViewModel
import com.merajavier.cineme.movies.reviews.MovieReviewsViewModel
import com.merajavier.cineme.movies.reviews.ReviewsListAdapter
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import timber.log.Timber

interface AddToFavoriteListener{
    fun onAddToFavoriteClick()
}

@ExperimentalPagingApi
class DetailsFragment : Fragment(), AddToFavoriteListener {

    private lateinit var binding: FragmentMovieDetailsBinding
    private lateinit var genresAdapter: GenresRecyclerAdapter
    private lateinit var actorsAdapter: ActorsRecyclerAdapter
    private lateinit var reviewsAdapter: ReviewsListAdapter

    private val castListViewModel: CastListViewModel by viewModel()
    private val accountViewModel: AccountViewModel by viewModel()
    private val moviesViewModel: MovieListViewModel by viewModel()
    private val reviewsViewModel: MovieReviewsViewModel by viewModel()

    private val loginViewModel: LoginViewModel by sharedViewModel()
    private val messageViewModel: MessageViewModel by sharedViewModel()

    private val args: DetailsFragmentArgs by navArgs()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentMovieDetailsBinding.inflate(inflater, container, false)

        binding.lifecycleOwner = this
        genresAdapter = GenresRecyclerAdapter()
        actorsAdapter = ActorsRecyclerAdapter(ActorsRecyclerAdapter.OnActorClickListener{
            findNavController().navigate(DetailsFragmentDirections.actionDetailsFragmentToActorFragment(it))
        })
        reviewsAdapter = ReviewsListAdapter()

        binding.movieGenres.adapter = genresAdapter
        binding.detailsMovieActors.adapter = actorsAdapter
        binding.detailsMovieReviews?.adapter = reviewsAdapter

        binding.movie = args.movie
        genresAdapter.submitList(args.movie.genres)
        castListViewModel.getMovieActors(args.movie.id)
        castListViewModel.getDirectors(args.movie.id)

        castListViewModel.actors.observe(viewLifecycleOwner, Observer {
            it.let {
                actorsAdapter.submitList(it)
            }
        })

        castListViewModel.crew.observe(viewLifecycleOwner, Observer {
            it.let { crew ->
                val directors = crew
                    .filter { member -> member.job == "Director" }
                    .map { member -> member.name}
                    .joinToString()

                binding.detailsMovieDirector.text = directors

                val writers = crew
                    .filter { member -> member.department == "Writing" }
                    .joinToString { member -> "${member.name} (${member.job})" }

                binding.detailsMovieWriters.text = writers
            }
        })

        binding.movieAddToFavorite?.setListener {

            if(loginViewModel.isLogged.value == true){
                val isFavorite = !(moviesViewModel.isMovieFavorite.value)!!

                accountViewModel.addMovieToFavorites(
                    loginViewModel.userSession.sessionId,
                    args.movie.id,
                    isFavorite)

                messageViewModel.setMessage(
                    when(isFavorite){
                        true -> getString(R.string.added_to_favorites_text)
                        false -> getString(R.string.removed_from_favorites_text)
                    })
            }else{
                messageViewModel.setMessage(getString(R.string.sign_in_error_message))
            }
        }

        accountViewModel.movieUpdated.observe(viewLifecycleOwner, Observer {
            if(it.equals(MarkFavoriteStatus.DONE)){
                moviesViewModel.isMovieFavorite(args.movie.id, loginViewModel.userSession.sessionId)
            }
        })

        if(loginViewModel.isLogged.value == true){
            moviesViewModel.isMovieFavorite(args.movie.id, loginViewModel.userSession.sessionId)
            moviesViewModel.isMovieFavorite.observe(viewLifecycleOwner, Observer {

                if(it == null){
                    binding.movieAddToFavorite?.iconResource = R.drawable.movie_favorite_not_selected
                }else{
                    displayFavoriteIcon(it)
                }
            })
        }

        binding.movieOverview.setOnClickListener {
            findNavController().navigate(DetailsFragmentDirections.actionDetailsFragmentToOverviewFragment(args.movie.overview))
        }

        reviewsViewModel.getTopMovieReviews(args.movie.id)
        reviewsViewModel.topMovieReviews.observe(viewLifecycleOwner, Observer {
            reviewsAdapter.submitList(it)
        })

        binding.detailsMovieSeeAllReviews?.setOnClickListener {
            findNavController().navigate(DetailsFragmentDirections.actionDetailsFragmentToReviewsFragment(args.movie.id))
        }

        return binding.root
    }

    private fun displayFavoriteIcon(isFavorite: Boolean){

        binding.movieAddToFavorite?.iconResource =
        if(isFavorite){
            R.drawable.movie_favorite_selected
        }else{
            R.drawable.movie_favorite_not_selected_dark
        }
    }

    override fun onAddToFavoriteClick() {
        Toast.makeText(context, "sup", Toast.LENGTH_SHORT).show()
    }
}