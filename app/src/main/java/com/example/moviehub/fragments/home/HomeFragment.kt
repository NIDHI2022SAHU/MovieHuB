package com.example.moviehub.fragments.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.moviehub.R
import com.example.moviehub.adapter.MovieAdapter
import com.example.moviehub.adapter.TvAdapter
import com.example.moviehub.api.ApiServices
import com.example.moviehub.api.RetrofitClient
import com.example.moviehub.databinding.FragmentHomeBinding
import com.example.moviehub.utilities.Utility
import com.example.moviehub.viewmodel.ViewModelFactory
import kotlinx.coroutines.launch

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private lateinit var trendingAdapter: MovieAdapter
    private lateinit var popularMovieAdapter: MovieAdapter
    private lateinit var popularTvAdapter:TvAdapter
    private lateinit var topRatedAdapter: TvAdapter
    private lateinit var upcomingAdapter: MovieAdapter
    private lateinit var nowPlayingAdapter: MovieAdapter
    private lateinit var airingTodayAdapter: TvAdapter
    private lateinit var onAirAdapter: TvAdapter
    private lateinit var apiServices: ApiServices
    private lateinit var homeViewModel: HomeViewModel
    private val utility: Utility = Utility()
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        initApiServices()
        initViewModel()
        initAdapters()
        initRecyclers()
        initSpinners()
        setUpListeners()
        utility.checkNetworkConnection(requireContext())
        return root
    }

     private fun initApiServices() {
        val retrofit = RetrofitClient.getInstance().getClient()
        apiServices = retrofit.create(ApiServices::class.java)
    }

    private fun initViewModel() {
        homeViewModel =
            ViewModelProvider(this, ViewModelFactory(apiServices))[HomeViewModel::class.java]
    }

    private fun initAdapters() {
        trendingAdapter = MovieAdapter(requireContext())
        popularMovieAdapter = MovieAdapter(requireContext())
        popularTvAdapter = TvAdapter(requireContext())
        topRatedAdapter = TvAdapter(requireContext())
        upcomingAdapter = MovieAdapter(requireContext())
        nowPlayingAdapter = MovieAdapter(requireContext())
        airingTodayAdapter = TvAdapter(requireContext())
        onAirAdapter = TvAdapter(requireContext())

    }
    private fun initRecyclers() {
        val rvTrending = binding.rvTrending
        val rvPopularMovie = binding.rvPopularMovie
        val rvPopularTv = binding.rvPopularTv
        val rvTopRated = binding.rvTopRated
        val rvUpcoming = binding.rvUpcoming
        val rvNowPlaying = binding.rvNowPlaying
        val rvAiringToday = binding.rvAiringToday
        val rvOnTheAir = binding.rvOnTheAir

        rvTrending.layoutManager =
            LinearLayoutManager(requireContext(), GridLayoutManager.HORIZONTAL, false)
        rvPopularMovie.layoutManager =
            LinearLayoutManager(requireContext(), GridLayoutManager.HORIZONTAL, false)
        rvTopRated.layoutManager =
            LinearLayoutManager(requireContext(), GridLayoutManager.HORIZONTAL, false)
        rvUpcoming.layoutManager =
            LinearLayoutManager(requireContext(), GridLayoutManager.HORIZONTAL, false)
        rvNowPlaying.layoutManager =
            LinearLayoutManager(requireContext(), GridLayoutManager.HORIZONTAL, false)
        rvAiringToday.layoutManager =
            LinearLayoutManager(requireContext(), GridLayoutManager.HORIZONTAL, false)
        rvPopularTv.layoutManager =
            LinearLayoutManager(requireContext(), GridLayoutManager.HORIZONTAL, false)
        rvOnTheAir.layoutManager =
            LinearLayoutManager(requireContext(), GridLayoutManager.HORIZONTAL, false)

        rvTrending.adapter = trendingAdapter
        rvPopularMovie.adapter = popularMovieAdapter
        rvPopularTv.adapter = popularTvAdapter
        rvTopRated.adapter = topRatedAdapter
        rvUpcoming.adapter = upcomingAdapter
        rvNowPlaying.adapter = nowPlayingAdapter
        rvAiringToday.adapter = airingTodayAdapter
        rvOnTheAir.adapter = onAirAdapter

        observeViewModel()

    }

    private fun initSpinner(spinner: Spinner, arrayResId: Int, viewModelMethod: (String) -> Unit) {
        ArrayAdapter.createFromResource(
            requireContext(),
            arrayResId,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinner.adapter = adapter
        }

        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                val selectedItem = parent?.getItemAtPosition(position).toString()
                viewModelMethod.invoke(selectedItem)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                val default = parent?.getItemAtPosition(0).toString()
                viewModelMethod.invoke(default)
            }
        }
    }

    private fun initSpinners() {
        initSpinner(
            binding.spinnerSort,
            R.array.trending_options
        ) { homeViewModel.fetchTrendingMovies(it) }
    }

    private fun observeViewModel() {

        homeViewModel.popularMovies.observe(viewLifecycleOwner) { movies ->
            popularMovieAdapter.updateMovies(movies)
        }
        homeViewModel.popularTv.observe(viewLifecycleOwner){ tv->
            popularTvAdapter.updateTvShows(tv)
        }

        homeViewModel.topRatedTv.observe(viewLifecycleOwner) { tv ->
            topRatedAdapter.updateTvShows(tv)
        }

        homeViewModel.upcomingMovies.observe(viewLifecycleOwner) { movies ->
            upcomingAdapter.updateMovies(movies)
        }

        homeViewModel.nowPlayingMovies.observe(viewLifecycleOwner) { movies ->
            nowPlayingAdapter.updateMovies(movies)
        }

        homeViewModel.airingToday.observe(viewLifecycleOwner) { tv ->
            airingTodayAdapter.updateTvShows(tv)
        }

        homeViewModel.onAir.observe(viewLifecycleOwner) { tv ->
            onAirAdapter.updateTvShows(tv)
        }
        homeViewModel.trendingMovies.observe(viewLifecycleOwner) { movies ->
            trendingAdapter.updateMovies(movies)
        }
    }
    private fun setUpListeners() {
        binding.swipeRefreshLayout.setOnRefreshListener {
            if(utility.checkNetworkConnection(requireContext())){
            homeViewModel.fetchTrendingMovies("day")
            homeViewModel.fetchTopRatedTvSeries()
            homeViewModel.fetchPopularTv()
            homeViewModel.fetchPopularMovies()
            homeViewModel.fetchUpcomingMovies()
            homeViewModel.getOnTheAirTvSeries()
            homeViewModel.fetchNowPlayingMovie()
            homeViewModel.getAiringTodayTvSeries()
            lifecycleScope.launch {
                observeViewModel()
            }
            Toast.makeText(requireContext(), "Refresh Done !!", Toast.LENGTH_SHORT).show()
            }
            binding.swipeRefreshLayout.isRefreshing = false
        }
    }

     override fun onResume() {
         super.onResume()
         observeViewModel()
     }
}