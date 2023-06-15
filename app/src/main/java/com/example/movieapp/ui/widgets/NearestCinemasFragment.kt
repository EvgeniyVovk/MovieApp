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
import com.example.movieapp.databinding.FragmentActorDetailBinding
import com.example.movieapp.databinding.FragmentCinemaMapBinding
import com.example.movieapp.databinding.FragmentNearestCinemasBinding
import com.example.movieapp.ui.adapters.ActorMoviesAdapter
import com.example.movieapp.ui.adapters.CinemaAdapter
import com.example.movieapp.utils.ScreenStates
import com.example.movieapp.viewModel.CinemaViewModel
import com.example.movieapp.viewModel.ViewModelFactory
import com.yandex.mapkit.MapKitFactory
import kotlinx.coroutines.launch
import javax.inject.Inject

class NearestCinemasFragment : Fragment(R.layout.fragment_nearest_cinemas) {

    private var _binding: FragmentNearestCinemasBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: CinemaAdapter

    private val viewModel: CinemaViewModel by activityViewModels {
        factory.create()
    }

    @Inject
    lateinit var factory: ViewModelFactory.Factory

    override fun onAttach(context: Context) {
        MapKitFactory.initialize(context)
        context.appComponent.inject(this)
        super.onAttach(context)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentNearestCinemasBinding.inflate(inflater, container, false)

        setupRecyclerView()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeViewModelFields()
    }

    private fun setupRecyclerView() {
        adapter = CinemaAdapter(viewModel.cinemaClickInterfaceImpl.value)
        val layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        binding.recyclerViewCinemasList.layoutManager = layoutManager
        binding.recyclerViewCinemasList.adapter = adapter
    }

    private fun observeViewModelFields() {
        viewLifecycleOwner.lifecycle.coroutineScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED){
                viewModel.cinemasToShow.collect { cinemas ->
                    adapter.cinemas = cinemas.toMutableList()
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}