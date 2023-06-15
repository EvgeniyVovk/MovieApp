package com.example.movieapp.ui.screens

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.coroutineScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.movieapp.R
import com.example.movieapp.appComponent
import com.example.movieapp.databinding.FragmentMovieBinding
import com.example.movieapp.databinding.FragmentSearchBinding
import com.example.movieapp.ui.adapters.ActorMoviesAdapter
import com.example.movieapp.ui.adapters.MoviesAdapter
import com.example.movieapp.ui.adapters.SearchMoviesAdapter
import com.example.movieapp.utils.ScreenStates
import com.example.movieapp.viewModel.MovieViewModel
import com.example.movieapp.viewModel.SearchViewModel
import com.example.movieapp.viewModel.ViewModelFactory
import kotlinx.coroutines.launch
import javax.inject.Inject

class SearchFragment : Fragment(R.layout.fragment_search) {

    private lateinit var adapter: SearchMoviesAdapter
    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!

    private var searchText: String? = null

    private val viewModelSearch: SearchViewModel by viewModels {
        factory.create()
    }

    private val viewModelMovie: MovieViewModel by activityViewModels {
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
        _binding = FragmentSearchBinding.inflate(inflater, container, false)

        setupSearchView()
        observeViewModelFields()
        setupRecyclerView()
        setupTryAgainButton()

        return binding.root
    }

    private fun observeViewModelFields() {
        viewLifecycleOwner.lifecycle.coroutineScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED){
                viewModelSearch.screenState.collect { state ->
                    when (state) {
                        is ScreenStates.Loading -> {
                            setVisibility(View.VISIBLE, View.INVISIBLE, View.INVISIBLE, View.INVISIBLE)
                        }
                        is ScreenStates.ServerError, is ScreenStates.ConnectionError -> {
                            setVisibility(View.INVISIBLE, View.VISIBLE, View.INVISIBLE, View.INVISIBLE)
                        }
                        is ScreenStates.Ready -> {
                            setVisibility(View.INVISIBLE, View.INVISIBLE, View.VISIBLE, View.INVISIBLE)
                        }
                        else -> {
                            setVisibility(View.INVISIBLE, View.INVISIBLE, View.INVISIBLE, View.INVISIBLE)
                        }
                    }
                }
            }
        }
        viewLifecycleOwner.lifecycle.coroutineScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModelSearch.listToShow.collect { movies ->
                    if (movies.isEmpty()){
                        setVisibility(View.INVISIBLE, View.INVISIBLE, View.INVISIBLE, View.VISIBLE)
                    } else {
                        adapter.movies = movies.toMutableList()
                    }
                }
            }
        }
    }

    private fun setupTryAgainButton() {
        binding.tryAgainButtonMoviesSearch.setOnClickListener {
            viewModelSearch.searchMovies(searchText.orEmpty())
        }
    }

    private fun setupRecyclerView(){
        adapter = SearchMoviesAdapter(viewModelMovie.movieClickInterfaceImpl.value)
        val layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        binding.recyclerViewMoviesSearch.layoutManager = layoutManager
        binding.recyclerViewMoviesSearch.adapter = adapter
    }

    private fun setVisibility(viewProgress: Int, viewError: Int, viewLayout: Int, viewNothingLayout: Int) {
        binding.recyclerViewMoviesSearch.visibility = viewLayout
        binding.progressBarMoviesSearch.visibility = viewProgress
        binding.errorMoviesSearchLayout.visibility = viewError
        binding.nothingSearchLayout.visibility = viewNothingLayout
    }

    private fun setupSearchView() {
        binding.searchViewMovie.setOnQueryTextListener(object: SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                binding.searchViewMovie.clearFocus()
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                searchText = newText
                viewModelSearch.searchMovies(newText.orEmpty())
                return true
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}