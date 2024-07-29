package com.example.moviehub.fragments.movie

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.moviehub.api.ApiRepo
import com.example.moviehub.model.Genre
import com.example.moviehub.model.MovieResult
import com.example.moviehub.utilities.Utility
import kotlinx.coroutines.launch

class MovieViewModel(private val apiRepo: ApiRepo) : ViewModel() {

    private val _discoverMovie = MutableLiveData<List<MovieResult>>()
    val discoverMovie: LiveData<List<MovieResult>> = _discoverMovie

    private val _genresMovie = MutableLiveData<List<Genre>>()
    val genresMovie: LiveData<List<Genre>> = _genresMovie

    private val utility: Utility = Utility()

    init {
        discoverMovies()
        getMovieGenres()
    }

    fun discoverMovies() {
        viewModelScope.launch {
            try {
                val movies = apiRepo.discoverMovies()
                _discoverMovie.value = movies
            } catch (e: Exception) {
                utility.logError("Error fetching Discover movies", e)
            }
        }
    }

    fun getMovieGenres() {
        viewModelScope.launch {
            try {
                val genres = apiRepo.getMovieGenres()
                utility.debug("Genres Detail Movie $genres")
                _genresMovie.value = genres
            } catch (e: Exception) {
                utility.logError("Error fetching Genres movies", e)
            }
        }
    }

    fun getMovieGenresById(id: Int) {
        viewModelScope.launch {
            try {
                val genres = apiRepo.getMovieGenresById(id)
                utility.debug("Genres BY id $genres")
                _discoverMovie.value = genres
            } catch (e: Exception) {
                utility.logError("Error fetching Genres By Id movies", e)
            }
        }
    }

    private fun sortMoviesBy(comparator: Comparator<MovieResult>) {
        viewModelScope.launch {
            try {
                val sortedMovies = discoverMovie.value?.sortedWith(comparator)
                _discoverMovie.value = sortedMovies!!
            } catch (e: Exception) {
                utility.logError("Error sorting movies", e)
            }
        }
    }

    fun sortByTitleAscending() {
        sortMoviesBy(compareBy { it.title })
    }

    fun sortByReleaseDateAscending() {
        sortMoviesBy(compareBy { it.releaseDate })
    }

    fun sortByReleaseDateDescending() {
        sortMoviesBy(compareByDescending { it.releaseDate })
    }

    fun sortByPopularityAscending() {
        sortMoviesBy(compareBy { it.popularity })
    }

    fun sortByPopularityDescending() {
        sortMoviesBy(compareByDescending { it.popularity })
    }

    fun sortByRatingAscending() {
        sortMoviesBy(compareBy { it.voteAverage })
    }

    fun sortByRatingDescending() {
        sortMoviesBy(compareByDescending { it.voteAverage })
    }
}
