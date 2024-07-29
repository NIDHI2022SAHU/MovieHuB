package com.example.moviehub.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.moviehub.api.ApiRepo
import com.example.moviehub.api.ApiServices
import com.example.moviehub.fragments.home.HomeViewModel
import com.example.moviehub.fragments.movie.MovieViewModel
import com.example.moviehub.fragments.quiz.QuizViewModel
import com.example.moviehub.fragments.tv.TvViewModel


@Suppress("UNCHECKED_CAST")
class ViewModelFactory(private val apiServices: ApiServices) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        val apiRepo = ApiRepo(apiServices)
        when (modelClass) {
            HomeViewModel::class.java -> {
                return HomeViewModel(apiRepo) as T
            }
            MovieViewModel::class.java -> {
                return MovieViewModel(apiRepo) as T
            }
            TvViewModel::class.java -> {
                return TvViewModel(apiRepo) as T
            }
            QuizViewModel::class.java -> {
                return QuizViewModel(apiRepo) as T
            }
            ApiViewModel::class.java ->{
                return ApiViewModel(apiRepo) as T
            }
            else -> throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}