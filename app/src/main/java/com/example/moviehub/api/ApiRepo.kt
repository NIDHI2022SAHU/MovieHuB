package com.example.moviehub.api

import com.example.moviehub.model.CreditList
import com.example.moviehub.model.Genre
import com.example.moviehub.model.MovieDetails
import com.example.moviehub.model.MovieImagesList
import com.example.moviehub.model.MovieResult
import com.example.moviehub.model.TvSeriesDetail
import com.example.moviehub.model.VideoList
import com.example.moviehub.utilities.Utility

class ApiRepo (private val apiServices: ApiServices) {

    private var utility: Utility = Utility()
    suspend fun getPopularMovies(): List<MovieResult> {
        val response = apiServices.getPopularMovie()
        if (response.isSuccessful) {
            utility.debug("Popular Movie${response.body()!!.results}")
            return response.body()?.results ?: emptyList()
        } else {
            throw Exception("Error loading popular movies")
        }
    }
    suspend fun getPopularTv(): List<TvSeriesDetail> {
        val response = apiServices.getPopularTvSeries()
        if (response.isSuccessful) {
            utility.debug("Popular Tv${response.body()!!.results}")
            return response.body()?.results ?: emptyList()
        } else {
            throw Exception("Error loading popular movies")
        }
    }
   suspend fun getTopRatedMovies(): List<MovieResult> {
        val allMovies = mutableListOf<MovieResult>()
        var currentPage = 1
        val totalPages = 2

        while (currentPage <= totalPages) {
            val response = apiServices.getTopRatedMovie(page = currentPage)
            if (response.isSuccessful) {
                val movieResponse = response.body()
                movieResponse?.let {
                    allMovies.addAll(it.results)
                }
            } else {
                throw Exception("Error loading Top Rated movies")
            }
            currentPage++
        }

        return allMovies
    }
    suspend fun getTopRatedTvSeries(): List<TvSeriesDetail> {
        val response = apiServices.getTopRatedTvSeries()
        if (response.isSuccessful) {
            utility.debug("Top Rated Tv${response.body()!!.results}")
            return response.body()?.results ?: emptyList()
        } else {
            throw Exception("Error loading Top Rated movies")
        }
    }
    suspend fun getNowPlayingMovie(): List<MovieResult> {
        val response = apiServices.getNowPlayingMovie()
        if (response.isSuccessful) {
            utility.debug("Now Playing Movie${response.body()!!.results}")
            return response.body()?.results ?: emptyList()
        } else {
            throw Exception("Error loading Trending movies")
        }
    }
    suspend fun getTrendingMovieByTime(timeWindow:String): List<MovieResult> {
        val response = apiServices.getTrendingMovieByTime(timeWindow)
        if (response.isSuccessful) {
            utility.debug("Trending Movie time${response.body()!!.results}")
            return response.body()?.results ?: emptyList()
        } else {
            throw Exception("Error loading Trending movies")
        }
    }
    suspend fun getUpcomingMovie(): List<MovieResult> {
        val response = apiServices.getUpcomingMovie()
        if (response.isSuccessful) {
            utility.debug("Upcoming Movie${response.body()!!.results}")
            return response.body()?.results ?: emptyList()
        } else {
            throw Exception("Error loading Upcoming movies")
        }
    }
    suspend fun discoverMovies(): List<MovieResult> {
        val response = apiServices.discoverMovie()
        if (response.isSuccessful) {
            utility.debug("Discover Movie ${response.body()!!.results}")
            return response.body()?.results ?: emptyList()
        } else {
            throw Exception("Error loading Discover movies")
        }
    }
    suspend fun discoverTv(): List<TvSeriesDetail> {
        val response = apiServices.discoverTv()
        if (response.isSuccessful) {
            utility.debug("discover Tv ${response.body()!!.results}")
            return response.body()?.results ?: emptyList()
        } else {
            throw Exception("Error loading Discover Tv")
        }
    }
    suspend fun getOnTheAirTvSeries(): List<TvSeriesDetail> {
        val response = apiServices.getOnTheAirTvSeries()
        if (response.isSuccessful) {
            utility.debug("On the Air Tv ${response.body()!!.results}")
            return response.body()?.results?: emptyList()
        } else {
            throw Exception("Error loading On the Air Tv shows ")
        }
    }
    suspend fun getAiringTodayTvSeries(): List<TvSeriesDetail> {
        val response = apiServices.getAiringTodayTvSeries()
        if (response.isSuccessful) {
            utility.debug("Airing today Tv ${response.body()!!.results}")
            return response.body()?.results?: emptyList()
        } else {
            throw Exception("Error loading Airing Today Tv show")
        }
    }
    suspend fun getCast(type: String, id:Int): CreditList? {
        val response = apiServices.getMovieCast(type,id)
        if (response.isSuccessful) {
            utility.debug("Get Cast $type ${response.body()!!}")
            return response.body()
        } else {
            throw Exception("Error loading Cast of  movies")
        }
    }
    suspend fun getSimilarMovies(id:Int): List<MovieResult> {
        val response = apiServices.getSimilarMovies(id)
        if (response.isSuccessful) {
            utility.debug("Similar movie${response.body()!!.results}")
            return response.body()?.results ?: emptyList()
        } else {
            throw Exception("Error loading Similar movies")
        }
    }
    suspend fun getSimilarTv(id:Int): List<TvSeriesDetail> {
        val response = apiServices.getSimilarTv(id)
        if (response.isSuccessful) {
            utility.debug("Similar Tv${response.body()!!.results}")
            return response.body()?.results ?: emptyList()
        } else {
            throw Exception("Error loading Similar Tv")
        }
    }
    suspend fun getRecommendationsMovie(id:Int): List<MovieResult> {
        val response = apiServices.getRecommendationsMovies(id)
        if (response.isSuccessful) {
            utility.debug("Recommandation Movie${response.body()!!.results}")
            return response.body()?.results ?: emptyList()
        } else {
            throw Exception("Error loading Recommandation movies")
        }
    }
    suspend fun getRecommendationsTV(tvId:Int): List<TvSeriesDetail> {
        val response = apiServices.getRecommendationsTv(tvId)
        if (response.isSuccessful) {
            utility.debug("Recommandation Tv ${response.body()!!.results}")
            return response.body()?.results ?: emptyList()
        } else {
            throw Exception("Error loading Recommandation movies")
        }
    }
    suspend fun getSearchResultMovie(query:String): List<MovieResult> {
        val response = apiServices.getSearchResultMovie(query)
        if (response.isSuccessful) {
            utility.debug("Search Result for Movie ${response.body()!!.results}")
            return response.body()?.results ?: emptyList()
        } else {
            throw Exception("Error loading Search Result movies")
        }
    }
    suspend fun getSearchResultTv(query:String): List<TvSeriesDetail> {
        val response = apiServices.getSearchResultTv(query)
        if (response.isSuccessful) {
            utility.debug("Search Result for Tv ${response.body()!!.results}")
            return response.body()?.results ?: emptyList()
        } else {
            throw Exception("Error loading Search Result tv")
        }
    }
    suspend fun getMovieDetailById(type: String,id:Int): MovieDetails {
        val response = apiServices.getMovieDetailById(type,id)
        if (response.isSuccessful) {
            utility.debug("Movie Detail By Id ${response.body()!!}")
            return response.body()!!
        } else {
            throw Exception("Error loading Search Result movies")
        }
    }

    suspend fun getMovieGenres():List<Genre> {
        val response = apiServices.getMovieGenres()
        if (response.isSuccessful) {
            utility.debug("Movie Genres${response.body()!!}")
            return response.body()!!.genres
        } else {
            throw Exception("Error loading Search Result movies")
        }
    }
    suspend fun getTvGenres():List<Genre> {
        val response = apiServices.getTvGenres()
        if (response.isSuccessful) {
            utility.debug("Tv Genres${response.body()!!}")
            return response.body()!!.genres
        } else {
            throw Exception("Error loading Search Result Tv genres")
        }
    }
    suspend fun getMovieGenresById(id:Int):List<MovieResult> {
        val response = apiServices.getMovieGenresById(id)
        if (response.isSuccessful) {
            utility.debug("Movie Genres By Id ${response.body()!!}")
            return response.body()!!.results
        } else {
            throw Exception("Error Fetch Genres Data")
        }
    }
    suspend fun getTvGenresById(id:Int):List<TvSeriesDetail> {
        val response = apiServices.getTvGenresById(id)
        if (response.isSuccessful) {
            utility.debug("Tv Genres By Id ${response.body()!!}")
            return response.body()!!.results
        } else {
            throw Exception("Error Fetch Genres Data")
        }
    }
    suspend fun getMovieImages(type: String,id:Int): MovieImagesList {
        val response = apiServices.getMovieImages(type,id)
        if (response.isSuccessful) {
            utility.debug("Get Movie Image ${response.body()!!}")
            return response.body()!!
        } else {
            throw Exception("Error loading Search Result movies")
        }
    }
    suspend fun getMovieVideos(type:String,id:Int): VideoList {
        val response = apiServices.getMovieVideos(type,id)
        if (response.isSuccessful) {
            utility.debug("Movie Video${response.body()!!}")
            return response.body()!!
        } else {
            throw Exception("Error loading Search Result movies")
        }
    }

}