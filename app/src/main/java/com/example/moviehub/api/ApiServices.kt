package com.example.moviehub.api

import com.example.moviehub.model.TvSeriesList
import com.example.moviehub.model.CreditList
import com.example.moviehub.model.MovieDetails
import com.example.moviehub.model.GenreList
import com.example.moviehub.model.MovieImagesList
import com.example.moviehub.model.MovieList
import com.example.moviehub.model.VideoList
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiServices {
    @GET("trending/movie/{time_window}")
    suspend fun getTrendingMovieByTime(@Path("time_window") timeWindow:String): Response<MovieList>
    @GET("movie/now_playing")
    suspend fun getNowPlayingMovie(): Response<MovieList>
    @GET("movie/popular")
    suspend fun getPopularMovie(): Response<MovieList>
    @GET("tv/popular")
    suspend fun getPopularTvSeries() : Response<TvSeriesList>
    @GET("movie/top_rated")
    suspend fun getTopRatedMovie(@Query("page") page: Int): Response<MovieList>

    @GET("tv/top_rated")
    suspend fun getTopRatedTvSeries(): Response<TvSeriesList>
    @GET("movie/upcoming")
    suspend fun getUpcomingMovie(): Response<MovieList>
    @GET("movie/{movie_id}/similar")
    suspend fun getSimilarMovies(@Path("movie_id") id:Int): Response<MovieList>

    @GET("tv/{tv_id}/similar")
    suspend fun getSimilarTv(@Path("tv_id") id:Int): Response<TvSeriesList>

    @GET("movie/{movie_id}/recommendations")
    suspend fun getRecommendationsMovies(@Path("movie_id") id:Int): Response<MovieList>
    @GET("tv/{tv_id}/recommendations")
    suspend fun getRecommendationsTv(@Path("tv_id") tvId:Int): Response<TvSeriesList>

    @GET("tv/airing_today")
    suspend fun getAiringTodayTvSeries() : Response<TvSeriesList>
    @GET("tv/on_the_air")
    suspend fun getOnTheAirTvSeries() : Response<TvSeriesList>

    @GET("discover/movie")
    suspend fun discoverMovie(): Response<MovieList>

    @GET("genre/movie/list")
    suspend fun getMovieGenres(): Response<GenreList>
    @GET("genre/tv/list")
    suspend fun getTvGenres() : Response<GenreList>
    @GET("discover/movie")
    suspend fun getMovieGenresById(  @Query("with_genres") genreId: Int ):Response<MovieList>
    @GET("discover/tv")
    suspend fun getTvGenresById(  @Query("with_genres") genreId: Int ):Response<TvSeriesList>
    @GET("discover/tv")
    suspend fun discoverTv(): Response<TvSeriesList>
    @GET("{type}/{movie_id}")
    suspend fun getMovieDetailById(@Path("type")type: String,@Path("movie_id") id: Int):Response<MovieDetails>

    @GET("{type}/{movie_id}/credits")
    suspend fun getMovieCast(@Path("type")type:String,@Path("movie_id") id: Int): Response<CreditList>

   @GET("{type}/{movie_id}/images")
   suspend fun getMovieImages(@Path("type")type: String,@Path("movie_id") id :Int):Response<MovieImagesList>

    @GET("{type}/{movie_id}/videos")
    suspend fun getMovieVideos(@Path("type")type: String,@Path("movie_id") id :Int):Response<VideoList>
    @GET("search/movie")
    suspend fun getSearchResultMovie(@Query("query")query: String): Response<MovieList>

    @GET("search/tv")
    suspend fun getSearchResultTv(@Query("query")query: String): Response<TvSeriesList>

}

