package com.example.movieapp.ui.screens

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
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.movieapp.R
import com.example.movieapp.appComponent
import com.example.movieapp.data.model.actormodel.Actor
import com.example.movieapp.databinding.FragmentActorDetailBinding
import com.example.movieapp.ui.adapters.ActorMoviesAdapter
import com.example.movieapp.ui.adapters.clicklisteners.ActorMoviesClickListener
import com.example.movieapp.utils.ScreenStates
import com.example.movieapp.viewModel.MovieViewModel
import com.example.movieapp.viewModel.ViewModelFactory
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.time.format.DateTimeFormatter
import java.util.*
import javax.inject.Inject

class ActorDetailFragment : Fragment(R.layout.fragment_actor_detail), ActorMoviesClickListener {

    private lateinit var adapter: ActorMoviesAdapter
    private var actorId: Int = -1
    private var _binding: FragmentActorDetailBinding? = null
    private val binding get() = _binding!!

    private var actor: Actor? = null

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
        actorId = ActorDetailFragmentArgs.fromBundle(requireArguments()).actorId

        viewModel.getActorById(actorId)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentActorDetailBinding.inflate(inflater, container, false)

        setupActorMovieClickListener()
        setupRecyclerView()
        setupTryAgainButton()
        setupBackButton()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (actor != null) {
            setupUI(actor)
            setVisibility(View.INVISIBLE, View.INVISIBLE, View.VISIBLE)
        } else {
            observeViewModelFields()
        }
    }

    private fun observeViewModelFields() {
        viewLifecycleOwner.lifecycle.coroutineScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED){
                viewModel.actorToShow.collect { _actor ->
                    setupUI(_actor)
                    actor = _actor
                }
            }
        }
        viewLifecycleOwner.lifecycle.coroutineScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.detailActorScreenState.collect { state ->
                    when (state) {
                        is ScreenStates.Ready -> {
                            setVisibility(View.INVISIBLE, View.INVISIBLE, View.VISIBLE)
                        }
                        is ScreenStates.Loading -> {
                            setVisibility(View.VISIBLE, View.INVISIBLE, View.INVISIBLE)
                        }
                        is ScreenStates.ConnectionError, is ScreenStates.ServerError -> {
                            setVisibility(View.INVISIBLE, View.VISIBLE, View.INVISIBLE)
                        }
                    }
                }
            }
        }
    }

    private fun setupBackButton(){
        binding.actorDetailBackButton.setOnClickListener {
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }
    }

    private fun setVisibility(viewProgress: Int, viewError: Int, viewLayout: Int) {
        binding.actorDetailLayout.visibility = viewLayout
        binding.progressBarActorDetail.visibility = viewProgress
        binding.errorActorDetailLayout.visibility = viewError
    }

    private fun setupRecyclerView() {
        adapter = ActorMoviesAdapter(viewModel.actorMovieClickInterfaceImpl.value)
        val layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        binding.recyclerViewActorMovies.layoutManager = layoutManager
        binding.recyclerViewActorMovies.adapter = adapter
    }

    private fun setupUI(actor: Actor?) {
        actor?.let { _actor ->
            adapter.movies = _actor.movies ?: listOf()
            binding.textViewActorName.text = _actor.name ?: _actor.enName
            binding.textViewGenreOfActor.text = _actor.profession?.joinToString { it.value ?: "" }
            binding.textViewActorBirthday.text = convertDate(_actor.birthday)?.let {
                "Дата рождения %s".format(it)
            }
            binding.textViewActorBirthPlace.text = _actor.birthPlace?.joinToString { it.value ?: "" }
            _actor.death?.let {
                binding.textViewActorDeath.text = convertDate(_actor.death)?.let {
                    "Дата смерти %s".format(it)
                }
            } ?: {
                binding.textViewActorDeath.visibility = View.GONE
            }
            _actor.deathPlace?.let {deathPlace ->
                binding.textViewActorDeathPlace.text = deathPlace.joinToString { it.value ?: "" }
            } ?: {
                binding.textViewActorDeathPlace.visibility = View.GONE
            }
            _actor.photo?.let {
                if (it.isNotBlank()) {
                    Glide.with(binding.actorImageDetail.context)
                        .load(it)
                        .placeholder(R.drawable.actor_placeholder_image)
                        .error(R.drawable.actor_placeholder_image)
                        .into(binding.actorImageDetail)
                } else {
                    binding.actorImageDetail.setImageResource(R.drawable.actor_placeholder_image)
                }
            }
        }
    }

    private fun setupTryAgainButton() {
        binding.tryAgainButtonActorDetail.setOnClickListener {
            viewModel.getActorById(actorId)
        }
    }

    private fun setupActorMovieClickListener() {
        viewModel.setActorMoviesClickInterface(this)
    }

    private fun convertDate(inputDate: String?): String? {
        val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
        val outputFormat = SimpleDateFormat("dd MM yyyy", Locale("ru"))
        val date = inputDate?.let { inputFormat.parse(it) }
        return date?.let { outputFormat.format(it) }
    }

    override fun onMovieDetails(movieId: Int) {
        val action = ActorDetailFragmentDirections.actionActorDetailFragmentToMovieDetailFragment(movieId)
        findNavController().navigate(action)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}