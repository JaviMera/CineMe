package com.merajavier.cineme.cast

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.navigation.fragment.navArgs
import com.merajavier.cineme.R
import com.merajavier.cineme.databinding.FragmentActorBinding
import org.koin.androidx.viewmodel.ext.android.viewModel
import timber.log.Timber

class ActorFragment : Fragment() {

    private lateinit var binding: FragmentActorBinding
    private lateinit var actorCreditsAdapter: ActorCreditsAdapter
    private val castListViewModel: CastListViewModel by viewModel()
    private val args: ActorFragmentArgs by navArgs()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentActorBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = this
        actorCreditsAdapter = ActorCreditsAdapter()
        binding.actorDetailsRecyclerFilmography.adapter = actorCreditsAdapter

        castListViewModel.getActorDetails(args.actorId)
        castListViewModel.actorDetails.observe(viewLifecycleOwner, Observer {
            binding.actorDetail = it
            actorCreditsAdapter.submitList(it.credits.cast.sortedBy { credit -> credit.order }.take(10))
        })

        return binding.root
    }
}