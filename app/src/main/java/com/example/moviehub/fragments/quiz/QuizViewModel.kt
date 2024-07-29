package com.example.moviehub.fragments.quiz

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.moviehub.api.ApiRepo
import com.example.moviehub.model.MovieResult
import com.example.moviehub.utilities.Utility
import kotlinx.coroutines.launch

class QuizViewModel(private val apiRepo: ApiRepo) : ViewModel() {

    private  var utility: Utility = Utility()

    private val _movieList = MutableLiveData<List<MovieResult>>()
     val movieList: LiveData<List<MovieResult>> = _movieList

    init {
        getMovieList()
    }
    fun getMovieList() {
        viewModelScope.launch {
            try {
                val movies = apiRepo.getTopRatedMovies()
                _movieList.value = movies
                utility.debug("The Quiz fetched Top Rated movies $movies")
            } catch (e: Exception) {
                utility.logError("Error fetching Top Rated movies", e)
            }
        }
    }
}