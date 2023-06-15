package com.example.movieapp.ui.screens

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.example.movieapp.R
import com.example.movieapp.appComponent
import com.example.movieapp.data.model.cinemamodel.Cinema
import com.example.movieapp.databinding.FragmentCinemaTabsBinding
import com.example.movieapp.ui.adapters.clicklisteners.CinemaClickListener
import com.example.movieapp.ui.widgets.CinemaMapFragment
import com.example.movieapp.ui.widgets.FavouriteCinemasFragment
import com.example.movieapp.ui.widgets.NearestCinemasFragment
import com.example.movieapp.utils.Constants.tabNames
import com.example.movieapp.viewModel.CinemaViewModel
import com.example.movieapp.viewModel.ViewModelFactory
import com.google.android.material.tabs.TabLayout
import javax.inject.Inject

class CinemaTabsFragment : Fragment(R.layout.fragment_cinema_tabs), CinemaClickListener {

    private var _binding: FragmentCinemaTabsBinding? = null
    private val binding get() = _binding!!

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
        _binding = FragmentCinemaTabsBinding.inflate(inflater, container, false)

        setupTabs()
        setupCinemaClickListener()

        return binding.root
    }

    private fun setupTabs(){
        setFragment(CinemaMapFragment())
        val tabs = binding.tabLayout
        tabNames.forEach { tabName ->
            tabs.newTab().let {
                it.text = tabName
                tabs.addTab(it)
            }
        }
        tabs.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                viewModel.saveTabIndex(tab?.position ?: 0)
                tab?.position?.let {
                    when (it) {
                        0 -> setFragment(CinemaMapFragment())
                        1 -> setFragment(NearestCinemasFragment())
                        else -> setFragment(FavouriteCinemasFragment())
                    }
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {}
        })
    }

    private fun setFragment(fragment: Fragment) {
        childFragmentManager.beginTransaction()
            .replace(R.id.cinemas_fragment_container, fragment)
            .commit()
    }

    override fun onResume() {
        super.onResume()
        binding.tabLayout.getTabAt(viewModel.selectedPositionTabIndex.value)?.select()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setupCinemaClickListener() {
        if (viewModel.cinemaClickInterfaceImpl.value == null) viewModel.setCinemaClickInterface(this)
    }

    override fun onLikeButtonClicked(cinema: Cinema) {
        if (!cinema.isFavourite) {
            viewModel.deleteCinemaFromDB (cinema.description!!)
        } else {
            viewModel.saveFavouriteCinemaToDB(cinema)
        }
    }
}