package com.merajavier.cineme.login

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.merajavier.cineme.R
import com.merajavier.cineme.databinding.FragmentUserBinding
import com.merajavier.cineme.network.NetworkLoginRepository
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject

class UserFragment : Fragment() {
    private lateinit var binding: FragmentUserBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentUserBinding.inflate(inflater, container, false)

        // Inflate the layout for this fragment
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
                    findNavController().navigate(UserFragmentDirections.actionUserFragmentToNavigationLogin())
                }
            }
        }
        return super.onOptionsItemSelected(item)
    }
}