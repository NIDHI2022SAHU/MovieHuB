package com.example.moviehub.fragments.tv

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import com.example.moviehub.R
import com.example.moviehub.adapter.TvAdapter
import com.example.moviehub.api.ApiServices
import com.example.moviehub.api.RetrofitClient
import com.example.moviehub.databinding.FragmentTvBinding
import com.example.moviehub.model.Genre
import com.example.moviehub.utilities.Utility
import com.example.moviehub.viewmodel.ViewModelFactory
import kotlinx.coroutines.launch

class TvFragment : Fragment() {
    private var _binding: FragmentTvBinding? = null
    private lateinit var expTvAdapter: TvAdapter
     private lateinit var apiServices: ApiServices
    private lateinit var tvViewModel:TvViewModel
    private val utility: Utility = Utility()

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
            _binding = FragmentTvBinding.inflate(inflater, container, false)
            val root: View = binding.root

            initApiServices()
            initViewModel()
            initAdapter()
            observeTv()
            initSortingSpinner()
            initGenresSpinner()
            setUpListeners()
            utility.checkNetworkConnection(requireContext())

        return root
    }

    private fun initViewModel() {
        tvViewModel =
            ViewModelProvider(this,ViewModelFactory(apiServices))[TvViewModel::class.java]

    }
    private fun initApiServices() {
        val retrofit = RetrofitClient.getInstance().getClient()
        apiServices = retrofit.create(ApiServices::class.java)
    }
    private fun initAdapter() {
        val rvExpTv = binding.rvExpTv
        val layoutManager = GridLayoutManager(requireContext(), 2)
        rvExpTv.layoutManager = layoutManager

        expTvAdapter = TvAdapter(requireContext())
        rvExpTv.adapter = expTvAdapter
    }

    private fun observeTv() {
        tvViewModel.discoverTv.observe (viewLifecycleOwner) { tv ->
            expTvAdapter.updateTvShows(tv)
        }
    }

    private fun initGenresSpinner() {
        tvViewModel.genresTv.observe(viewLifecycleOwner) { genres ->
            val genresList = genres.map { Genre(it.id, it.name) }
            val genresNames = genresList.map { it.name }.toTypedArray()

            val genresAdapter = ArrayAdapter(
                requireContext(),
                android.R.layout.simple_spinner_item,
                genresNames
            )
            genresAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            binding.spinnerGenresTv.adapter = genresAdapter

            binding.spinnerGenresTv.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                    val selectedGenre = genresList[position]
                    tvViewModel.getTvGenresById(selectedGenre.id)
                    utility.debug("Spinner Item Select ${genresList[position]} ${selectedGenre.id}")
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {
                    tvViewModel.discoverTv()
                }
            }
        }
    }
    private fun initSortingSpinner() {
        val sortAdapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_item,
            resources.getStringArray(R.array.sort_options)
        )
        sortAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerSortTv.adapter = sortAdapter

        binding.spinnerSortTv.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                when (position) {
                    1 -> {
                        tvViewModel.sortByTitleAscending()
                    }
                    2 -> {
                        tvViewModel.sortByReleaseDateAscending()
                    }
                    3 -> {
                        tvViewModel.sortByReleaseDateDescending()
                    }
                    4 -> {
                        tvViewModel.sortByPopularityAscending()
                    }
                    5 -> {
                        tvViewModel.sortByPopularityDescending()
                    }
                    6 -> {
                        tvViewModel.sortByRatingAscending()
                    }
                    7 -> {
                        tvViewModel.sortByRatingDescending()
                    }
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                // Handle default selection (if needed)
            }
        }
    }
    private fun setUpListeners() {
        binding.swipeRefreshLayout.setOnRefreshListener {
            if(utility.checkNetworkConnection(requireContext())) {
                tvViewModel.discoverTv()
                tvViewModel.getTvGenres()
                lifecycleScope.launch {
                    observeTv()
                }
                utility.showToast(requireContext(),"Refresh Done !!")
            }
                binding.swipeRefreshLayout.isRefreshing = false
        }
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}