package com.example.movieapp.ui.screens

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.coroutineScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.movieapp.R
import com.example.movieapp.appComponent
import com.example.movieapp.data.model.entitymoviemodel.MovieEntity
import com.example.movieapp.data.model.entitymoviemodel.PersonsEntity
import com.example.movieapp.databinding.FragmentMovieDetailBinding
import com.example.movieapp.ui.adapters.clicklisteners.ActorClickListener
import com.example.movieapp.ui.adapters.ActorsAdapter
import com.example.movieapp.utils.Constants.IS_FIRST_TIME
import com.example.movieapp.utils.ScreenStates
import com.example.movieapp.viewModel.MovieViewModel
import com.example.movieapp.viewModel.ViewModelFactory
import kotlinx.coroutines.launch
import javax.inject.Inject

class MovieDetailFragment : Fragment(R.layout.fragment_movie_detail), ActorClickListener {

    private lateinit var adapter: ActorsAdapter
    private var movieId: Int = -1
    private var _binding: FragmentMovieDetailBinding? = null
    private val binding get() = _binding!!

    private var movie: MovieEntity? = null

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
        movieId = MovieDetailFragmentArgs.fromBundle(requireArguments()).movieId
        viewModel.getMovieById(movieId)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentMovieDetailBinding.inflate(inflater, container, false)

        setupActorClickListener()
        setupRecyclerView()
        setupTryAgainButton()
        setupBackButton()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (movie != null) {
            setupUI(movie)
            setVisibility(View.INVISIBLE, View.INVISIBLE, View.VISIBLE)
        } else {
            observeViewModelFields()
        }
    }

    private fun observeViewModelFields() {
        viewLifecycleOwner.lifecycle.coroutineScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED){
                viewModel.movieToShowDetail.collect { _movie ->
                    setupUI(_movie)
                    movie = _movie
                }
            }
        }
        viewLifecycleOwner.lifecycle.coroutineScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED){
                viewModel.detailMovieScreenState.collect { state ->
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

    private fun setVisibility(viewProgress: Int, viewError: Int, viewLayout: Int) {
        binding.movieDetailLayout.visibility = viewLayout
        binding.progressBarMovieDetail.visibility = viewProgress
        binding.errorMovieDetailLayout.visibility = viewError
    }

    private fun setupUI(movie: MovieEntity?) {
        setupFavouriteButton(movie)
        movie?.let { _movie ->
            adapter.actors = getActors(_movie.persons)
            if (movie.isFavourite) {
                binding.imageViewLikeButtonMovieDetail.setImageResource(R.drawable.image_like_filled)
            } else {
                binding.imageViewLikeButtonMovieDetail.setImageResource(R.drawable.image_like)
            }
            binding.textViewGenreOfMovie.text = _movie.genres.joinToString { it.name ?: "" }
            binding.filmRatingViewDetail.text = _movie.ageRating?.let { "%d+".format(it) } ?: "NR"
            binding.textViewMovieName.text = _movie.name ?: _movie.alternativeName
            binding.textViewMovieReview.text = _movie.description
            binding.textViewMovieKpRating.text = _movie.rating?.kp?.let {
                "Рейтинг кинопоиска: %.2f".format(it) } ?: ""
            binding.ratingBarMovieDetail.rating = _movie.rating?.kp?.toFloat()?.div(2) ?: 0f
            _movie.poster?.url?.let {
                if (it.isNotBlank()) {
                    Glide.with(binding.movieImageDetail.context)
                        .load(it)
                        .placeholder(R.drawable.img_placeholder)
                        .error(R.drawable.img_placeholder)
                        .into(binding.movieImageDetail)
                } else {
                    binding.movieImageDetail.setImageResource(R.drawable.img_placeholder)
                }
            }
        }
    }

    private fun getActors(persons: List<PersonsEntity>): List<PersonsEntity> {
        return persons.filter { it.profession.equals("актеры") }
    }

    private fun setupFavouriteButton(movie: MovieEntity?) {
        binding.imageViewLikeButtonMovieDetail.setOnClickListener {
            movie?.let {
                if (it.isFavourite) {
                    binding.imageViewLikeButtonMovieDetail.setImageResource(R.drawable.image_like)
                    viewModel.deleteMovieFromDB(it.id!!)
                } else {
                    binding.imageViewLikeButtonMovieDetail.setImageResource(R.drawable.image_like_filled)
                    it.isFavourite = !it.isFavourite
                    viewModel.saveMovieToDB(it)
                }
            }
        }
    }

    private fun setupRecyclerView() {
        adapter = ActorsAdapter(viewModel.actorClickInterfaceImpl.value)
        val layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        binding.recyclerViewActors.layoutManager = layoutManager
        binding.recyclerViewActors.adapter = adapter
    }

    private fun setupActorClickListener() {
        viewModel.setActorClickInterface(this)
    }

    private fun setupTryAgainButton() {
        binding.tryAgainButtonMovieDetail.setOnClickListener {
            viewModel.getActorById(movieId)
        }
    }

    private fun setupBackButton(){
        binding.movieDetailBackButton.setOnClickListener {
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }
    }

    override fun onActorDetails(actorId: Int) {
        val action = MovieDetailFragmentDirections.actionMovieDetailFragmentToActorDetailFragment(actorId)
        findNavController().navigate(action)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}