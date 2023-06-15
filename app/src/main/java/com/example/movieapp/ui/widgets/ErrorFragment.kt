package com.example.movieapp.ui.widgets

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.example.movieapp.R
import com.example.movieapp.appComponent
import com.example.movieapp.databinding.FragmentErrorBinding
import com.example.movieapp.databinding.FragmentMovieBinding
import com.example.movieapp.viewModel.MovieViewModel
import com.example.movieapp.viewModel.ViewModelFactory
import javax.inject.Inject

class ErrorFragment : Fragment(R.layout.fragment_error) {

    private var _binding: FragmentErrorBinding? = null
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
        _binding = FragmentErrorBinding.inflate(inflater, container, false)

        setupTryAgainButton()

        return binding.root
    }

    private fun setupTryAgainButton() {
        binding.tryAgainButtonErrorLayout.setOnClickListener {
            viewModel.updateData()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}