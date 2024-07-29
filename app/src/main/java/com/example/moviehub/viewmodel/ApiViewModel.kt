package com.example.moviehub.viewmodel

import androidx.lifecycle.*
import com.example.moviehub.api.ApiRepo
import com.example.moviehub.model.*
import com.example.moviehub.utilities.Utility
import kotlinx.coroutines.launch

class ApiViewModel(private val apiRepo: ApiRepo) : ViewModel() {

    private  var utility: Utility = Utility()

    private val _similarMovies = MutableLiveData<List<MovieResult>>()
    val similarMovies: LiveData<List<MovieResult>> = _similarMovies

    private val _similarTvMovies = MutableLiveData<List<TvSeriesDetail>>()
    val similarTvMovies: LiveData<List<TvSeriesDetail>> = _similarTvMovies

    private val _topCast = MutableLiveData<List<Cast>>()
    val topCast: LiveData<List<Cast>> = _topCast

    private val _topCrew = MutableLiveData<List<Crew>>()
    val topCrew: LiveData<List<Crew>> = _topCrew

    private val _imageList = MutableLiveData<List<Poster>>()
    val imageList: LiveData<List<Poster>> = _imageList

    private val _videoList = MutableLiveData<List<VideoResult>>()
    val videoList: LiveData<List<VideoResult>> = _videoList

    private val _recommendationMovies = MutableLiveData<List<MovieResult>>()
    val recommendationMovies: LiveData<List<MovieResult>> = _recommendationMovies

    private val _recommendationTv = MutableLiveData<List<TvSeriesDetail>>()
    val recommendationTv: LiveData<List<TvSeriesDetail>> = _recommendationTv

    private val _searchResultMovie = MutableLiveData<List<MovieResult>>()
    val searchResultMovie: LiveData<List<MovieResult>> = _searchResultMovie

    private val _searchResultTv = MutableLiveData<List<TvSeriesDetail>>()
    val searchResultTv: LiveData<List<TvSeriesDetail>> = _searchResultTv

    // Function to fetch similar movies
    fun fetchSimilarMovies(id: Int) {
        viewModelScope.launch {
            try {
                val similarMovie = apiRepo.getSimilarMovies(id)
                _similarMovies.value = similarMovie
            } catch (e: Exception) {
                utility.logError("Error fetching similar movies", e)
            }
        }
    }

    // Function to fetch similar TV series
    fun fetchSimilarTv(id: Int) {
        viewModelScope.launch {
            try {
                val similarTv = apiRepo.getSimilarTv(id)
                _similarTvMovies.value = similarTv
            } catch (e: Exception) {
                utility.logError("Error fetching similar TV series", e)
            }
        }
    }

    // Function to fetch top cast and crew
    fun fetchTopCast(type: String, id: Int) {
        viewModelScope.launch {
            try {
                val credit = apiRepo.getCast(type, id)
                _topCast.value = credit?.cast
                _topCrew.value = credit?.crew
            } catch (e: Exception) {
                utility.logError("Error fetching top cast and crew", e)
            }
        }
    }

    // Function to fetch movie images
    fun getMovieImages(type: String, id: Int) {
        viewModelScope.launch {
            try {
                val posters = apiRepo.getMovieImages(type, id).posters
                _imageList.value = posters
            } catch (e: Exception) {
                utility.logError("Error fetching movie images", e)
            }
        }
    }

    // Function to fetch movie videos
    fun getMovieVideos(type: String, id: Int) {
        viewModelScope.launch {
            try {
                val videos = apiRepo.getMovieVideos(type, id).results
                _videoList.value = videos
            } catch (e: Exception) {
                utility.logError("Error fetching movie videos", e)
            }
        }
    }

    // Function to fetch recommendation movies
    fun fetchRecommendationMovies(id: Int) {
        viewModelScope.launch {
            try {
                val movies = apiRepo.getRecommendationsMovie(id)
                _recommendationMovies.value = movies
            } catch (e: Exception) {
                utility.logError("Error fetching recommendation movies", e)
            }
        }
    }

    // Function to fetch recommendation TV series
    fun fetchRecommendationTv(tvId: Int) {
        viewModelScope.launch {
            try {
                val tvSeries = apiRepo.getRecommendationsTV(tvId)
                _recommendationTv.value = tvSeries
            } catch (e: Exception) {
                utility.logError("Error fetching recommendation TV series", e)
            }
        }
    }

    // Function to fetch search results movie
    fun fetchSearchResultMovie(query: String) {
        viewModelScope.launch {
            try {
                val results = apiRepo.getSearchResultMovie(query)
                _searchResultMovie.value = results
            } catch (e: Exception) {
                utility.logError("Error fetching search results", e)
            }
        }
    }
    // Function to fetch search results tv
    fun fetchSearchResultTv(query: String) {
        viewModelScope.launch {
            try {
                val results = apiRepo.getSearchResultTv(query)
                _searchResultTv.value = results
            } catch (e: Exception) {
                utility.logError("Error fetching search results", e)
            }
        }
    }

    // Function to get movie details by ID
    fun getMovieDetailById(type: String, id: Int, callback: (MovieDetails?) -> Unit) {
        viewModelScope.launch {
            try {
                val movie = apiRepo.getMovieDetailById(type, id)
                callback(movie)
            } catch (e: Exception) {
                utility.logError("Error fetching movie details", e)
                callback(null)
            }
        }
    }

}
