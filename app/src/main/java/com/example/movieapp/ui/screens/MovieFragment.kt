package com.example.movieapp.ui.screens

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.coroutineScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.example.movieapp.R
import com.example.movieapp.appComponent
import com.example.movieapp.databinding.FragmentMovieBinding
import com.example.movieapp.ui.widgets.ErrorFragment
import com.example.movieapp.ui.widgets.MovieListFragment
import com.example.movieapp.ui.widgets.ShimmerFragment
import com.example.movieapp.utils.ScreenStates
import com.example.movieapp.utils.SnackBarTypes
import com.example.movieapp.viewModel.MovieViewModel
import com.example.movieapp.viewModel.ViewModelFactory
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.launch
import javax.inject.Inject

class MovieFragment : Fragment(R.layout.fragment_movie) {

    private var _binding: FragmentMovieBinding? = null
    private val binding get() = _binding!!

    private val viewModel: MovieViewModel by activityViewModels {
        factory.create()
    }

    @Inject
    lateinit var factory: ViewModelFactory.Factory

    override fun onAttach(context: Context) {
        context.appComponent.inject(this)
        super.onAttach(context)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMovieBinding.inflate(inflater, container, false)

        observeViewModelFields()

        return binding.root
    }

    private fun observeViewModelFields() {
        viewLifecycleOwner.lifecycle.coroutineScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED){
                viewModel.screenState.collect { state ->
                    when (state) {
                        is ScreenStates.Ready -> {
                            showData()
                        }
                        is ScreenStates.ServerError -> {
                            showServerError()
                        }
                        is ScreenStates.ConnectionError -> {
                            showConnectionError()
                        }
                        is ScreenStates.Loading -> {
                            showLoading()
                        }
                    }
                }
            }
        }
    }

    private fun showLoading() {
        setFragment(ShimmerFragment())
        showSnackBar(SnackBarTypes.Loading)
    }

    private fun showData() {
        setFragment(MovieListFragment())
    }

    private fun showConnectionError() {
        showSnackBar(SnackBarTypes.ConnectionError)
        setFragment(ErrorFragment())
    }

    private fun showServerError() {
        showSnackBar(SnackBarTypes.ServerError)
        setFragment(ErrorFragment())
    }

    private fun showSnackBar(snackBarState: SnackBarTypes) {
        var text = ""
        var background = Color.LTGRAY
        var textColor = Color.BLACK

        when (snackBarState) {
            SnackBarTypes.ConnectionError -> {
                text = getString(R.string.snackbar_con_error_text)
                background = Color.parseColor("#F44336")
                textColor = Color.WHITE
            }
            SnackBarTypes.Loading -> {
                text = getString(R.string.snackbar_loading_text)
                background = Color.parseColor("#6534FF")
                textColor = Color.WHITE
            }
            SnackBarTypes.ServerError -> {
                text = getString(R.string.snackbar_server_error_text)
                background = Color.parseColor("#F44336")
                textColor = Color.WHITE
            }
        }
        val snackBar = Snackbar.make(binding.root, text, Snackbar.LENGTH_SHORT)
        snackBar.setBackgroundTint(background)
        snackBar.setTextColor(textColor)
        snackBar.show()
    }

    private fun setFragment(fragment: Fragment) {
        childFragmentManager.beginTransaction()
            .replace(R.id.fragment_container_movie, fragment)
            .commit()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}