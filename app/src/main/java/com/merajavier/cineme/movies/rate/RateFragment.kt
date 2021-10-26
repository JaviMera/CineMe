package com.merajavier.cineme.movies.rate

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.merajavier.cineme.R
import com.merajavier.cineme.databinding.FragmentRateBinding

class RateFragment : Fragment() {

    private lateinit var binding: FragmentRateBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentRateBinding.inflate(inflater, container, false)

        return binding.root
    }
}