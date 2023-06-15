package com.example.movieapp.ui.widgets

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.coroutineScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.GridLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.movieapp.R
import com.example.movieapp.appComponent
import com.example.movieapp.databinding.FragmentMovieListBinding
import com.example.movieapp.ui.adapters.MoviesAdapter
import com.example.movieapp.viewModel.MovieViewModel
import com.example.movieapp.viewModel.ViewModelFactory
import kotlinx.coroutines.launch
import javax.inject.Inject

class MovieListFragment : Fragment(R.layout.fragment_movie_list), SwipeRefreshLayout.OnRefreshListener {

    private var _binding: FragmentMovieListBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: MoviesAdapter

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
        // Inflate the layout for this fragment
        _binding = FragmentMovieListBinding.inflate(inflater, container, false)

        setupRecyclerView()
        setupSwipeRefreshLayout()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewLifecycleOwner.lifecycle.coroutineScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED){
                viewModel.listToShow.collect { data ->
                    adapter.movies = data.toMutableList()
                }
            }
        }
    }

    private fun setupRecyclerView(){
        adapter = MoviesAdapter(viewModel.movieClickInterfaceImpl.value)
        val layoutManager = GridLayoutManager(context, 2)
        binding.recyclerViewMoviesList.layoutManager = layoutManager
        binding.recyclerViewMoviesList.adapter = adapter
    }

    private fun setupSwipeRefreshLayout() {
        binding.swipeRefreshLayout.setOnRefreshListener(this)
        binding.swipeRefreshLayout.setColorSchemeResources(R.color.refreshRingColor)
    }

    override fun onRefresh() {
        viewModel.updateData()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding.swipeRefreshLayout.isRefreshing = false
        _binding = null
    }
}