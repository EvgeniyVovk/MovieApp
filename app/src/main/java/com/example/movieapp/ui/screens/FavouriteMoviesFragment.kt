package com.example.movieapp.ui.screens

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.coroutineScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.movieapp.R
import com.example.movieapp.appComponent
import com.example.movieapp.databinding.FragmentFavouriteMoviesBinding
import com.example.movieapp.ui.adapters.SearchMoviesAdapter
import com.example.movieapp.viewModel.MovieViewModel
import com.example.movieapp.viewModel.ViewModelFactory
import kotlinx.coroutines.launch
import javax.inject.Inject

class FavouriteMoviesFragment : Fragment(R.layout.fragment_favourite_movies) {

    private lateinit var adapter: SearchMoviesAdapter
    private var _binding: FragmentFavouriteMoviesBinding? = null
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
        _binding = FragmentFavouriteMoviesBinding.inflate(inflater, container, false)

        setVisibility(View.VISIBLE, View.INVISIBLE, View.INVISIBLE)
        setupRecyclerView()
        viewModel.loadFavouriteMovies()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeViewModelFields()
    }

    private fun observeViewModelFields() {
        viewLifecycleOwner.lifecycle.coroutineScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.favouriteMoviesList.collect { movies ->
                    if (movies == null){
                        setVisibility(View.VISIBLE, View.INVISIBLE, View.INVISIBLE)
                    } else if (movies.isEmpty()) {
                        setVisibility(View.INVISIBLE, View.INVISIBLE, View.VISIBLE)
                    } else {
                        adapter.movies = movies.toMutableList()
                        setVisibility(View.INVISIBLE, View.VISIBLE, View.INVISIBLE)
                    }
                }
            }
        }
    }

    private fun setVisibility(viewProgress: Int, viewRecycler: Int, viewNothing: Int) {
        binding.recyclerViewFavouriteMovies.visibility = viewRecycler
        binding.progressBarFavouriteMovies.visibility = viewProgress
        binding.nothingMoviesLayout.visibility = viewNothing
    }

    private fun setupRecyclerView(){
        adapter = SearchMoviesAdapter(viewModel.movieClickInterfaceImpl.value)
        val layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        binding.recyclerViewFavouriteMovies.layoutManager = layoutManager
        binding.recyclerViewFavouriteMovies.adapter = adapter
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}