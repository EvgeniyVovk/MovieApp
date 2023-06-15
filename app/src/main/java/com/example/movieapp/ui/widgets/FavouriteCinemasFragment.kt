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
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.movieapp.R
import com.example.movieapp.appComponent
import com.example.movieapp.databinding.FragmentFavouriteCinemasBinding
import com.example.movieapp.databinding.FragmentNearestCinemasBinding
import com.example.movieapp.ui.adapters.CinemaAdapter
import com.example.movieapp.viewModel.CinemaViewModel
import com.example.movieapp.viewModel.ViewModelFactory
import com.yandex.mapkit.MapKitFactory
import kotlinx.coroutines.launch
import javax.inject.Inject

class FavouriteCinemasFragment : Fragment(R.layout.fragment_favourite_cinemas) {

    private var _binding: FragmentFavouriteCinemasBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: CinemaAdapter

    private val viewModel: CinemaViewModel by activityViewModels {
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
        _binding = FragmentFavouriteCinemasBinding.inflate(inflater, container, false)

        setVisibility(View.VISIBLE, View.INVISIBLE, View.INVISIBLE)
        setupRecyclerView()
        viewModel.loadFavouriteCinemas()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeViewModelFields()
    }

    private fun setupRecyclerView() {
        adapter = CinemaAdapter(viewModel.cinemaClickInterfaceImpl.value)
        val layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        binding.recyclerViewFavouriteCinemasList.layoutManager = layoutManager
        binding.recyclerViewFavouriteCinemasList.adapter = adapter
    }

    private fun observeViewModelFields() {
        viewLifecycleOwner.lifecycle.coroutineScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED){
                viewModel.favouriteCinemasToShow.collect { cinemas ->
                    if (cinemas == null){
                        setVisibility(View.VISIBLE, View.INVISIBLE, View.INVISIBLE)
                    } else if (cinemas.isEmpty()) {
                        setVisibility(View.INVISIBLE, View.INVISIBLE, View.VISIBLE)
                    } else {
                        adapter.cinemas = cinemas.toMutableList()
                        setVisibility(View.INVISIBLE, View.VISIBLE, View.INVISIBLE)
                    }
                }
            }
        }
    }

    private fun setVisibility(viewProgress: Int, viewRecycler: Int, viewNothing: Int) {
        binding.recyclerViewFavouriteCinemasList.visibility = viewRecycler
        binding.progressBarFavouriteCinemas.visibility = viewProgress
        binding.nothingCinemasLayout.visibility = viewNothing
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}