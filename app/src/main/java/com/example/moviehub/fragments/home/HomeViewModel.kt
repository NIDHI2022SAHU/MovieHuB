package com.example.moviehub.fragments.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.moviehub.api.ApiRepo
import com.example.moviehub.model.MovieResult
import com.example.moviehub.model.TvSeriesDetail
import com.example.moviehub.utilities.Utility
import kotlinx.coroutines.launch

class HomeViewModel(private val apiRepo: ApiRepo) : ViewModel() {
    private val utility: Utility = Utility()

    private val _trendingMovies = MutableLiveData<List<MovieResult>>()
    val trendingMovies: LiveData<List<MovieResult>> = _trendingMovies

    private val _upcomingMovies = MutableLiveData<List<MovieResult>>()
    val upcomingMovies: LiveData<List<MovieResult>> = _upcomingMovies

    private val _popularMovies = MutableLiveData<List<MovieResult>>()
    val popularMovies: LiveData<List<MovieResult>> = _popularMovies

    private val _popularTv = MutableLiveData<List<TvSeriesDetail>>()
    val popularTv: LiveData<List<TvSeriesDetail>> = _popularTv

    private val _topRatedTv = MutableLiveData<List<TvSeriesDetail>>()
    val topRatedTv: LiveData<List<TvSeriesDetail>> = _topRatedTv

    private val _nowPlayingMovies = MutableLiveData<List<MovieResult>>()
    val nowPlayingMovies: LiveData<List<MovieResult>> = _nowPlayingMovies

    private val _airingToday = MutableLiveData<List<TvSeriesDetail>>()
    val airingToday: LiveData<List<TvSeriesDetail>> = _airingToday

    private val _onAir = MutableLiveData<List<TvSeriesDetail>>()
    val onAir: MutableLiveData<List<TvSeriesDetail>> = _onAir

    init {
        fetchTopRatedTvSeries()
        fetchPopularTv()
        fetchPopularMovies()
        fetchUpcomingMovies()
        getOnTheAirTvSeries()
        fetchNowPlayingMovie()
        getAiringTodayTvSeries()
    }

    fun fetchTrendingMovies(sortBy: String) {
        viewModelScope.launch {
         try{
             val movies = apiRepo.getTrendingMovieByTime(sortBy)
             _trendingMovies.value = movies
            } catch (e: Exception) {
                utility.debug("Error fetching Trending movies: ${e.message}")
            }
        }
    }
     fun fetchTopRatedTvSeries() {
        viewModelScope.launch {
            try {
                val movies = apiRepo.getTopRatedTvSeries()
                _topRatedTv.value = movies
            } catch (e: Exception) {
                utility.debug("Error fetching Top Rated movies: ${e.message}")
            }
        }
    }
      fun fetchPopularMovies() {
        viewModelScope.launch {
            try {
                val movies = apiRepo.getPopularMovies()
                _popularMovies.value = movies
            } catch (e: Exception) {
                utility.debug("Error fetching popular movies: ${e.message}")
            }
        }
    }
     fun fetchPopularTv() {
        viewModelScope.launch {
            try {
                val tv = apiRepo.getPopularTv()
                _popularTv.value = tv
            } catch (e: Exception) {
                utility.debug("Error fetching popular tv: ${e.message}")
            }
        }
    }

     fun fetchUpcomingMovies() {
        viewModelScope.launch {
            try {
                val movies = apiRepo.getUpcomingMovie()
                _upcomingMovies.value = movies
            } catch (e: Exception) {
                utility.debug("Error fetching Upcoming movies: ${e.message}")
            }
        }
    }

     fun getOnTheAirTvSeries() {
        viewModelScope.launch {
            try {
                val tvShow = apiRepo.getOnTheAirTvSeries()
                _onAir.value = tvShow
            } catch (e: Exception) {
                utility.debug("Error fetching On Air Tv Show: ${e.message}")
            }
        }
    }
     fun fetchNowPlayingMovie() {
        viewModelScope.launch {
            try {
                val movies = apiRepo.getNowPlayingMovie()
                _nowPlayingMovies.value = movies
            } catch (e: Exception) {
                utility.debug("Error fetching Now playing movies: ${e.message}")
            }
        }
    }
     fun getAiringTodayTvSeries() {
        viewModelScope.launch {
            try {
                val tvShow = apiRepo.getAiringTodayTvSeries()
                _airingToday.value = tvShow
            } catch (e: Exception) {
                utility.debug("Error fetching Airing Today Tv shows: ${e.message}")
            }
        }
    }

}