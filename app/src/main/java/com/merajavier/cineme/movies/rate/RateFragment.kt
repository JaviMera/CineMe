package com.merajavier.cineme.movies.rate

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.navArgs
import com.merajavier.cineme.R
import com.merajavier.cineme.databinding.FragmentRateBinding

class RateFragment : Fragment() {

    private lateinit var binding: FragmentRateBinding

    private val args: RateFragmentArgs by navArgs()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentRateBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = this
        binding.movie = args.movie
        
        return binding.root
    }
}