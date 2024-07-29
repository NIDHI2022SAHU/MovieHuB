package com.example.moviehub.activities

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.SearchView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.example.moviehub.adapter.MovieAdapter
import com.example.moviehub.adapter.TvAdapter
import com.example.moviehub.api.ApiServices
import com.example.moviehub.api.RetrofitClient
import com.example.moviehub.databinding.ActivitySearchBinding
import com.example.moviehub.utilities.Utility
import com.example.moviehub.viewmodel.ApiViewModel
import com.example.moviehub.viewmodel.ViewModelFactory

class SearchActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySearchBinding
    private lateinit var movieAdapter: MovieAdapter
    private lateinit var tvAdapter: TvAdapter
    private lateinit var apiServices: ApiServices
    private lateinit var apiViewmodel: ApiViewModel
    private var utility: Utility = Utility()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initViews()
        initAdapters()
        initViewModel()
        setUpListener()
    }

    private fun setUpListener() {
        if(utility.checkNetworkConnection(this)){
        binding.searchView.setOnQueryTextListener(object  : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                binding.searchView.clearFocus()
                query?.let { fetchSearchResult(it) }
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                newText?.let { fetchSearchResult(it) }
                return false
            }
        })}
        observeSearchResults()
    }

    private fun initViews() {

        val rvMovies=binding.rvSearchResultMovie
        val rvTv=binding.rvSearchResultTv

        val moviesLayoutManager = GridLayoutManager(this, 2)
        rvMovies.layoutManager = moviesLayoutManager

        val tvLayoutManager = GridLayoutManager(this, 2)
        rvTv.layoutManager = tvLayoutManager

        movieAdapter = MovieAdapter(this, mutableListOf())
        binding.rvSearchResultMovie.adapter = movieAdapter

        tvAdapter = TvAdapter(this, mutableListOf())
        binding.rvSearchResultTv.adapter = tvAdapter
    }
    private fun initAdapters() {

        movieAdapter = MovieAdapter(this)
        binding.rvSearchResultMovie.adapter = movieAdapter

        tvAdapter = TvAdapter(this)
        binding.rvSearchResultTv.adapter = tvAdapter
    }
    private fun initViewModel(){
        val retrofit = RetrofitClient.getInstance().getClient()
        apiServices = retrofit.create(ApiServices::class.java)
        apiViewmodel = ViewModelProvider(this, ViewModelFactory(apiServices))[ApiViewModel::class.java]
    }

    private fun fetchSearchResult(query: String) {
        apiViewmodel.fetchSearchResultMovie(query)
        apiViewmodel.fetchSearchResultTv(query)
    }

    private fun observeSearchResults() {
        apiViewmodel.searchResultMovie.observe(this) { data ->
            data?.let {
                if (it.isNotEmpty()) {
                   dataFoundResult()
                    movieAdapter.updateMovies(it)
                    Log.d("SearchResult", "$it")
                } else {
                    noDataFoundResult()
                }
            }
        }
        apiViewmodel.searchResultTv.observe(this) { data ->
            data?.let {
                if (it.isNotEmpty()) {
                    dataFoundResult()
                    tvAdapter.updateTvShows(it)
                    Log.d("SearchResult", "$it")
                } else {
                   noDataFoundResult()
                }
            }
        }
    }

    private fun noDataFoundResult() {
        binding.imgSearch.visibility = View.GONE
        binding.searchResultLayout.visibility = View.GONE
        binding.noSearchLayout.visibility = View.VISIBLE
    }

    private fun dataFoundResult() {
        binding.imgSearch.visibility = View.GONE
        binding.searchResultLayout.visibility = View.VISIBLE
        binding.noSearchLayout.visibility = View.GONE
    }

    override fun onResume() {
        super.onResume()
        setUpListener()
    }
}
