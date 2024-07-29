package com.example.moviehub.fragments.tv

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.moviehub.api.ApiRepo
import com.example.moviehub.model.Genre
import com.example.moviehub.model.TvSeriesDetail
import com.example.moviehub.utilities.Utility
import kotlinx.coroutines.launch

class TvViewModel(private val apiRepo: ApiRepo) : ViewModel() {

    private  var utility: Utility = Utility()

    private val _discoverTv = MutableLiveData<List<TvSeriesDetail>>()
    val discoverTv: LiveData<List<TvSeriesDetail>> = _discoverTv

    private val _genresTv = MutableLiveData<List<Genre>>()
    val genresTv: LiveData<List<Genre>> = _genresTv

    init {
        discoverTv()
        getTvGenres()
    }

     fun discoverTv() {
        viewModelScope.launch {
            try {
                val tvShows = apiRepo.discoverTv()
                _discoverTv.value = tvShows
            } catch (e: Exception) {
                utility.logError("Error fetching discover TV shows", e)
            }
        }
    }

    fun getTvGenres() {
        viewModelScope.launch {
            try {
                val genres = apiRepo.getTvGenres()
                _genresTv.value = genres
            } catch (e: Exception) {
                utility.logError("Error fetching TV genres", e)
            }
        }
    }

    fun getTvGenresById(id: Int) {
        viewModelScope.launch {
            try {
                val genres = apiRepo.getTvGenresById(id)
                _discoverTv.value = genres
            } catch (e: Exception) {
                utility.logError("Error fetching TV genres by Id", e)
            }
        }
    }

    private fun sortTvSeriesBy(comparator: Comparator<TvSeriesDetail>) {
        viewModelScope.launch {
            try {
                val sortedTvShows = discoverTv.value?.sortedWith(comparator)
                _discoverTv.value = sortedTvShows!!
            } catch (e: Exception) {
                utility.logError("Error sorting TV shows", e)
            }
        }
    }

    fun sortByTitleAscending() {
        sortTvSeriesBy(compareBy { it.name })
    }

    fun sortByReleaseDateAscending() {
        sortTvSeriesBy(compareBy { it.firstAirDate })
    }

    fun sortByReleaseDateDescending() {
        sortTvSeriesBy(compareByDescending { it.firstAirDate })
    }

    fun sortByPopularityAscending() {
        sortTvSeriesBy(compareBy { it.popularity })
    }

    fun sortByPopularityDescending() {
        sortTvSeriesBy(compareByDescending { it.popularity })
    }

    fun sortByRatingAscending() {
        sortTvSeriesBy(compareBy { it.voteAverage })
    }

    fun sortByRatingDescending() {
        sortTvSeriesBy(compareByDescending { it.voteAverage })
    }
}
