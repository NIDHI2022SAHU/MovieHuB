package com.example.moviehub.activities

import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import android.widget.ScrollView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.moviehub.R
import com.example.moviehub.adapter.ImagesAdapter
import com.example.moviehub.adapter.MovieAdapter
import com.example.moviehub.adapter.TopCastAdapter
import com.example.moviehub.adapter.TopCrewAdapter
import com.example.moviehub.adapter.TvAdapter
import com.example.moviehub.adapter.VideosAdapter
import com.example.moviehub.api.ApiServices
import com.example.moviehub.api.RetrofitClient
import com.example.moviehub.databinding.ActivityDetailBinding
import com.example.moviehub.model.MovieDetails
import com.example.moviehub.model.TvSeriesDetail
import com.example.moviehub.utilities.Constants
import com.example.moviehub.utilities.Utility
import com.example.moviehub.viewmodel.ApiViewModel
import com.example.moviehub.viewmodel.ViewModelFactory

class DetailActivity : BaseActivity() {
    private lateinit var binding: ActivityDetailBinding
    private lateinit var topCastAdapter: TopCastAdapter
    private lateinit var topCrewAdapter: TopCrewAdapter
    private lateinit var similarAdapter: MovieAdapter
    private lateinit var similarTVAdapter: TvAdapter
    private  lateinit var recommendationTvAdapter: TvAdapter
    private lateinit var imagesAdapter: ImagesAdapter
    private lateinit var videosAdapter: VideosAdapter
    private lateinit var recommendationAdapter: MovieAdapter
    private lateinit var apiServices: ApiServices
    private lateinit var apiViewmodel: ApiViewModel
    private lateinit var watchlist:MovieDetails
    private lateinit var watchlistTv:MovieDetails
    private var utility: Utility = Utility()
    private val views: View by lazy { findViewById(android.R.id.content) }
    private var id =0
    private var mediaType="movie"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initViews()
        initAdapters()
        initViewModel()
        setUpListeners()
        checkExistenceBeforeClick()

    }

    private fun initViews() {
        val rvTopCast = binding.rvTopCast
        val rvTopCrew =binding.rvTopCrew
        val rvSimilar = binding.rvSimilar
        val rvSimilarTv=binding.rvSimilarTv
        val rvImages=binding.rvImages
        val rvVideos =binding.rvVidoes
        val rvRecommendationMovie = binding.rvRecommendationsMovie
        val rvRecommendationTv=binding.rvRecommendationsTv

        val topCastLayoutManager = LinearLayoutManager(this,  LinearLayoutManager.HORIZONTAL, false)
        rvTopCast.layoutManager = topCastLayoutManager

        val topCrewLayoutManager = LinearLayoutManager(this,  LinearLayoutManager.HORIZONTAL, false)
        rvTopCrew.layoutManager = topCrewLayoutManager

        val imagesLayoutManager = LinearLayoutManager(this,  LinearLayoutManager.HORIZONTAL, false)
        rvImages.layoutManager = imagesLayoutManager

        val videosLayoutManager = LinearLayoutManager(this,  LinearLayoutManager.HORIZONTAL, false)
        rvVideos.layoutManager = videosLayoutManager

        val similarLayoutManager = LinearLayoutManager(this,  LinearLayoutManager.HORIZONTAL, false)
        rvSimilar.layoutManager = similarLayoutManager

        val similarTvLayoutManager = LinearLayoutManager(this,  LinearLayoutManager.HORIZONTAL, false)
        rvSimilarTv.layoutManager = similarTvLayoutManager

        val recommendationMovieLayoutManager = LinearLayoutManager(this,  LinearLayoutManager.HORIZONTAL, false)
        rvRecommendationMovie.layoutManager = recommendationMovieLayoutManager

        val recommendationTVLayoutManager = LinearLayoutManager(this,  LinearLayoutManager.HORIZONTAL, false)
        rvRecommendationTv.layoutManager = recommendationTVLayoutManager

    }

    private fun initAdapters() {
        topCastAdapter = TopCastAdapter(this)
        binding.rvTopCast.adapter = topCastAdapter

        topCrewAdapter = TopCrewAdapter(this)
        binding.rvTopCrew.adapter = topCrewAdapter

        similarTVAdapter=TvAdapter(this)
        binding.rvSimilarTv.adapter = similarTVAdapter

        imagesAdapter = ImagesAdapter(this)
        binding.rvImages.adapter = imagesAdapter

        videosAdapter = VideosAdapter(this)
        binding.rvVidoes.adapter = videosAdapter

        recommendationAdapter = MovieAdapter(this)
        binding.rvRecommendationsMovie.adapter = recommendationAdapter

        recommendationTvAdapter=TvAdapter(this)
        binding.rvRecommendationsTv.adapter=recommendationTvAdapter

        similarAdapter = MovieAdapter(this)
        binding.rvSimilar.adapter = similarAdapter

        similarAdapter = MovieAdapter(this)
        binding.rvSimilar.adapter = similarAdapter

    }

    private fun initViewModel() {
            val retrofit = RetrofitClient.getInstance().getClient()
            apiServices = retrofit.create(ApiServices::class.java)
            apiViewmodel = ViewModelProvider(this, ViewModelFactory(apiServices))[ApiViewModel::class.java]

            id= intent.getIntExtra("id", 0)
            mediaType= intent.getStringExtra("type").toString()

            utility.debug(" The media type is: $mediaType and id is:  $id")
            updateValues(mediaType,id)

            observeData()
    }

    private fun observeData() {

        fetchData()

        dbViewModel.checkIfMovieInWatchlist(id)
        dbViewModel.checkIfTvInWatchlist(id)

        apiViewmodel.similarMovies.observe(this) { movies ->
            similarAdapter.updateMovies(movies)
        }
        apiViewmodel.similarTvMovies.observe(this){ tv->
            similarTVAdapter.updateTvShows(tv)
        }
        apiViewmodel.topCast.observe(this) { casts ->
            topCastAdapter.updateCasts(casts)
        }
        apiViewmodel.topCrew.observe(this) { crew ->
            topCrewAdapter.updateCrew(crew)
        }
        apiViewmodel.imageList.observe(this) { images ->
            imagesAdapter.updatePosters(images)
        }
        apiViewmodel.videoList.observe(this) { videos ->
            videosAdapter.setVideos(videos)
        }
        apiViewmodel.recommendationMovies.observe(this) { movies ->
            recommendationAdapter.updateMovies(movies)
        }
        apiViewmodel.recommendationTv.observe(this) { tv ->
            recommendationTvAdapter.updateTvShows(tv)
        }
    }

    private fun fetchData() {

        if(mediaType == "movie"){
            apiViewmodel.fetchSimilarMovies(id)
            apiViewmodel.fetchRecommendationMovies(id)
        }
        else{
            binding.rvSimilar.visibility= View.GONE
            binding.rvRecommendationsMovie.visibility=View.GONE
            apiViewmodel.fetchSimilarTv(id)
            apiViewmodel.fetchRecommendationTv(id)
        }

        apiViewmodel.fetchTopCast(mediaType,id)
        apiViewmodel.getMovieImages(mediaType,id)
        apiViewmodel.getMovieVideos(mediaType,id)
    }

    private fun updateValues(type: String, id: Int) {

        binding.apply {
            title.text = intent.getStringExtra("title").toString()
            subtitle.text = intent.getStringExtra("subtitle").toString()

            apiViewmodel.getMovieDetailById(type, id) { movie ->
                runOnUiThread {
                    if (movie != null) {
                        watchlist=movie
                        watchlistTv=movie
                        ratingText.text = movie.voteAverage.toString()
                        overviewText.text = movie.overview
                        status.text = movie.status
                        releaseDate.text = movie.releaseDate
                        runtime.text = movie.runtime.toString()
                        spLanguageText.text = movie.spokenLanguages.map { it.name }.toString()

                        Glide.with(this@DetailActivity)
                            .load(Constants.POSTER_BASE_URL + movie.posterPath)
                            .into(poster)

                        val genreNames = movie.genres.joinToString(" | ") { it.name }
                        genresText.text = genreNames
                    } else {
                        utility.showToast(this@DetailActivity, "Network Issue Occurs!!")
                    }
                }
            }
        }
    }

    private fun setUpListeners() {
        binding.playButton.setOnClickListener {
            playButtonClicked()
        }
        binding.buttonWatchlist.setOnClickListener {
            checkExistAfterClick()
        }
        utility.checkNetworkConnection(this)
    }
    private fun movieDetailsToTvSeriesDetail(movieDetails: MovieDetails): TvSeriesDetail {
        val genreIds = movieDetails.genres.map { it.id }
        return TvSeriesDetail(
            adult =movieDetails.adult,
            backdropPath = movieDetails.backdropPath,
            firstAirDate=movieDetails.releaseDate,
            genreIds = genreIds,
            id = movieDetails.id,
            name=intent.getStringExtra("title").toString(),
            originalLanguage = movieDetails.originalLanguage,
            originCountry=movieDetails.originCountry,
            originalName=intent.getStringExtra("subtitle").toString(),
            overview = movieDetails.overview,
            popularity = movieDetails.popularity,
            posterPath = movieDetails.posterPath,
            voteAverage = movieDetails.voteAverage,
            voteCount = movieDetails.voteCount
        )
    }
    private fun addToWatchlist() {
          if(mediaType=="movie"){
            dbViewModel.addToWatchlistMovie(watchlist)}
        else{
              dbViewModel.addToWatchlistTv(movieDetailsToTvSeriesDetail(watchlistTv))
          }

    }
    private fun removeFromWatchlist() {
            if(mediaType=="movie"){
                dbViewModel.removeMovieFromWatchlist(id)
            }else{
                dbViewModel.removeTvFromWatchlist(id)
            }
    }
    fun playButtonClicked() {
        val scrollView = findViewById<ScrollView>(R.id.detail)
        val videoSection = findViewById<LinearLayout>(R.id.video_section)
        scrollView.smoothScrollTo(0, videoSection.top)
    }
    private fun checkExistAfterClick() {
        observeExistenceData { isExist ->
            if (isExist) {
                removeFromWatchlist()
                binding.buttonWatchlist.setImageResource(R.drawable.ic_bookmark)
                utility.showSnackBar(views,"Removed From Watchlist")
            } else {
                addToWatchlist()
                binding.buttonWatchlist.setImageResource(R.drawable.ic_bookmark_r)
                utility.showSnackBar(views,"Added From Watchlist")
            }
        }
    }
    private fun observeExistenceData(callback: (Boolean) -> Unit) {
        if(mediaType=="movie"){
            dbViewModel.checkIfMovieInWatchlist(id)
            dbViewModel.isMovieInWatchlist.observe(this) { value ->
                callback(value)
        } }else{
            dbViewModel.checkIfTvInWatchlist(id)
            dbViewModel.isTvInWatchlist.observe(this) { value ->
                callback(value)
            }
        }
    }
    private fun checkExistenceBeforeClick() {
        observeExistenceData { isExist ->
            if (isExist) {
                binding.buttonWatchlist.setImageResource(R.drawable.ic_bookmark_r)
            } else {
                binding.buttonWatchlist.setImageResource(R.drawable.ic_bookmark)
            }
        }
    }
}

