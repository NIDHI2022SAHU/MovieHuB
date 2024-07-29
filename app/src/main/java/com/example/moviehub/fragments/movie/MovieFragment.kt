package com.example.moviehub.fragments.movie

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import com.example.moviehub.R
import com.example.moviehub.adapter.MovieAdapter
import com.example.moviehub.api.ApiServices
import com.example.moviehub.api.RetrofitClient
import com.example.moviehub.databinding.FragmentMovieBinding
import com.example.moviehub.model.Genre
import com.example.moviehub.utilities.Utility
import com.example.moviehub.viewmodel.ViewModelFactory
import kotlinx.coroutines.launch

class MovieFragment : Fragment() {

    private var _binding: FragmentMovieBinding? = null
    private val binding get() = _binding!!

    private lateinit var expMovieAdapter: MovieAdapter
    private lateinit var apiServices: ApiServices
    private lateinit var movieViewModel: MovieViewModel
    private val utility: Utility = Utility()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMovieBinding.inflate(inflater, container, false)
        val root: View = binding.root

        initApiServices()
        initViewModel()
        initAdapter()
        observeMovies()
        initSortingSpinner()
        initGenresSpinner()
        setUpListeners()

        return root
    }

    private fun initApiServices() {
        val retrofit = RetrofitClient.getInstance().getClient()
        apiServices = retrofit.create(ApiServices::class.java)
    }

    private fun initViewModel() {
        movieViewModel =
            ViewModelProvider(this, ViewModelFactory(apiServices))[MovieViewModel::class.java]
    }

    private fun initAdapter(){
        val rvExpMovie = binding.rvExpMovies
        val layoutManager = GridLayoutManager(requireContext(), 2)
        rvExpMovie.layoutManager = layoutManager

        expMovieAdapter = MovieAdapter(requireContext())
        rvExpMovie.adapter = expMovieAdapter
    }
    private fun observeMovies() {
        movieViewModel.discoverMovie.observe(viewLifecycleOwner) { movies ->
            expMovieAdapter.updateMovies(movies)
        }
    }
    private fun initGenresSpinner() {
        movieViewModel.genresMovie.observe(viewLifecycleOwner) { genres ->
            val genresList = genres.map { Genre(it.id, it.name) }
            val genresNames = genresList.map { it.name }.toTypedArray()

            val genresAdapter = ArrayAdapter(
                requireContext(),
                android.R.layout.simple_spinner_item,
                genresNames
            )
            genresAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            binding.spinnerGenres.adapter = genresAdapter

            binding.spinnerGenres.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                    val selectedGenre = genresList[position]
                    movieViewModel.getMovieGenresById(selectedGenre.id)
                    utility.debug("Spinner Item Select ${genresList[position]} ${selectedGenre.id}")
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {
                    movieViewModel.discoverMovies()
                }
            }
        }
    }
    private fun initSortingSpinner() {
        val sortAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item,
            resources.getStringArray(R.array.sort_options)
        )
        sortAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerSort.adapter = sortAdapter

        binding.spinnerSort.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                when (position) {
                    1 -> {
                        movieViewModel.sortByTitleAscending()
                    }
                    2 -> {
                        movieViewModel.sortByReleaseDateAscending()
                    }
                    3 -> {
                        movieViewModel.sortByReleaseDateDescending()
                    }
                    4 -> {
                        movieViewModel.sortByPopularityAscending()
                    }
                    5 -> {
                        movieViewModel.sortByPopularityDescending()
                    }
                    6 -> {
                        movieViewModel.sortByRatingAscending()
                    }
                    7 -> {
                        movieViewModel.sortByRatingDescending()
                    }
                }
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {

            }
        }
    }
    private fun setUpListeners() {
        utility.checkNetworkConnection(requireContext())

        binding.swipeRefreshLayout.setOnRefreshListener {
            if(utility.checkNetworkConnection(requireContext())) {
                movieViewModel.discoverMovies()
                movieViewModel.getMovieGenres()
                lifecycleScope.launch {
                    observeMovies()
                }
                Toast.makeText(requireContext(), "Refresh Done !!", Toast.LENGTH_SHORT).show()
            }
            binding.swipeRefreshLayout.isRefreshing = false
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}