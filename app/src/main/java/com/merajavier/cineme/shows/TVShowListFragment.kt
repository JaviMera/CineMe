package com.merajavier.cineme.shows

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.merajavier.cineme.R
import com.merajavier.cineme.databinding.FragmentTVShowListBinding

class TVShowListFragment : Fragment() {

    private lateinit var _binding: FragmentTVShowListBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentTVShowListBinding.inflate(inflater, container, false)

        return _binding.root
    }
}