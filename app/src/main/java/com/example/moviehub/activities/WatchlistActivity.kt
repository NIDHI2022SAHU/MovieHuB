package com.example.moviehub.activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import com.example.moviehub.adapter.MovieAdapter
import com.example.moviehub.adapter.TvAdapter
import com.example.moviehub.databinding.ActivityWatchlistBinding
import com.example.moviehub.model.MovieDetails
import com.example.moviehub.model.MovieResult
import kotlinx.coroutines.launch

class WatchlistActivity : BaseActivity() {
    lateinit var binding: ActivityWatchlistBinding
    private lateinit var movieAdapter: MovieAdapter
    private lateinit var tvAdapter: TvAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWatchlistBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initAdapter()
        setupButtons()
        observeWatchlist()
        checkDataAvailability()
    }
    private fun initAdapter() {
        movieAdapter = MovieAdapter(this)
        binding.rvWatchlistMovies.adapter = movieAdapter
        tvAdapter =TvAdapter(this)
        binding.rvWatchlistTv.adapter = tvAdapter
    }
    private fun setupButtons() {
        binding.buttonMovieShare.setOnClickListener {shareMovies(binding.rvWatchlistMovies)}
        binding.buttonTvShare.setOnClickListener {shareMovies(binding.rvWatchlistTv) }
    }
    private fun movieDetailsToMovieResult(movieDetails: MovieDetails): MovieResult {
        val genreIds = movieDetails.genres.map { it.id }
        return MovieResult(
            adult = movieDetails.adult,
            backdropPath = movieDetails.backdropPath,
            genreIds = genreIds,
            id = movieDetails.id,
            originalLanguage = movieDetails.originalLanguage,
            originalTitle = movieDetails.originalTitle,
            overview = movieDetails.overview,
            popularity = movieDetails.popularity,
            posterPath = movieDetails.posterPath,
            releaseDate = movieDetails.releaseDate,
            title = movieDetails.title,
            video = movieDetails.video,
            voteAverage = movieDetails.voteAverage,
            voteCount = movieDetails.voteCount
        )
    }
    private fun observeWatchlist() {

        dbViewModel.watchlist.observe(this) { watchlist ->
            if(watchlist!=null && watchlist.isNotEmpty()){
                movieAdapter.updateMovies(watchlist.map(this::movieDetailsToMovieResult))
                binding.noDataLayout.visibility=View.GONE
            }else{
                checkDataAvailability()
               binding.watchlistMovieLayout.visibility= View.GONE
                binding.rvWatchlistMovies.visibility=View.GONE
            }
        }
        dbViewModel.watchlistTv.observe(this) { watchlist ->
            if(watchlist!=null && watchlist.isNotEmpty()){
                tvAdapter.updateTvShows(watchlist)
                binding.noDataLayout.visibility=View.GONE
            }
            else{
                checkDataAvailability()
                binding.watchlistTvLayout.visibility= View.GONE
                binding.rvWatchlistTv.visibility=View.GONE
            }
        }
        }

    private fun checkDataAvailability() {
        if(binding.watchlistMovieLayout.visibility==View.GONE && binding.watchlistTvLayout.visibility==View.GONE){
            binding.noDataLayout.visibility=View.VISIBLE
    }
}
    private fun shareMovies(recyclerView: RecyclerView) {
        val movieList = getMovieListFromRecyclerView(recyclerView)

        val shareIntent = Intent(Intent.ACTION_SEND)
        shareIntent.type = "text/plain"

        shareIntent.putExtra(Intent.EXTRA_TEXT, getMovieListAsText(movieList))

        startActivity(Intent.createChooser(shareIntent, "Share via"))
    }
    private fun getMovieListFromRecyclerView(recyclerView: RecyclerView): List<MovieResult> {
        val adapter = recyclerView.adapter as MovieAdapter
        return adapter.movies
    }

    private fun getMovieListAsText(movieList: List<MovieResult>): String {
        val text = StringBuilder()

        for (movie in movieList) {
            text.append("Movie: ").append(movie.title).append(", Rating: ").append(movie.voteAverage).append("\n")
        }

        return text.toString()
    }

    override fun onResume() {
        super.onResume()
        dbViewModel.loadMovieWatchlist()
        dbViewModel.loadTvWatchlist()
        lifecycleScope.launch {
            observeWatchlist()
        }
    }

}
