package com.example.movieapp.ui.screens

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.NavigationUI
import com.example.movieapp.R
import com.example.movieapp.appComponent
import com.example.movieapp.data.model.entitymoviemodel.MovieEntity
import com.example.movieapp.databinding.FragmentHostBinding
import com.example.movieapp.ui.adapters.clicklisteners.MovieClickListener
import com.example.movieapp.viewModel.MovieViewModel
import com.example.movieapp.viewModel.ViewModelFactory
import javax.inject.Inject

class HostFragment : Fragment(R.layout.fragment_host), MovieClickListener {

    private var _binding: FragmentHostBinding? = null
    private val binding get() = _binding!!

    private lateinit var locationPermissionLauncher: ActivityResultLauncher<String>

    private val viewModel: MovieViewModel by activityViewModels {
        factory.create()
    }

    @Inject
    lateinit var factory: ViewModelFactory.Factory

    override fun onAttach(context: Context) {
        context.appComponent.inject(this)
        super.onAttach(context)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        locationPermissionLauncher =
            registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
                if (!isGranted) {
                    Toast.makeText(
                        requireContext(),
                        "Невозможно подключиться к интернету",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHostBinding.inflate(inflater, container, false)

        if (ContextCompat.checkSelfPermission(requireActivity(), Manifest.permission.INTERNET)
            != PackageManager.PERMISSION_GRANTED) {
            locationPermissionLauncher.launch(Manifest.permission.INTERNET)
        } else {
            Log.d("PERM_HOST_FRAGMENT","CHECK_PERM_SUC")
        }

        setupMovieClickListener()
        setupNavigation()

        return binding.root
    }


    private fun setupMovieClickListener() {
        if (viewModel.movieClickInterfaceImpl.value == null) viewModel.setClickInterface(this)
    }

    private fun setupNavigation() {
        val navHostFragment = childFragmentManager
            .findFragmentById(R.id.fragment_container) as NavHostFragment
        val navController = navHostFragment.navController

        NavigationUI.setupWithNavController(binding.bottomNavigationView, navController)
    }


    override fun onMovieDetails(movieId: Int) {
        val action = HostFragmentDirections.actionHostFragmentToMovieDetailFragment(movieId)
        findNavController().navigate(action)
    }

    override fun onLikeButtonClicked(movie: MovieEntity) {
        if (!movie.isFavourite) {
            viewModel.deleteMovieFromDB(movie.id!!)
        } else {
            viewModel.saveMovieToDB(movie)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}