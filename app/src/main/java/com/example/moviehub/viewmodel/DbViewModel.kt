package com.example.moviehub.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.moviehub.db.entity.Users
import com.example.moviehub.model.MovieDetails
import com.example.moviehub.model.TvSeriesDetail
import com.example.moviehub.repo.DbRepository
import com.example.moviehub.utilities.Utility
import kotlinx.coroutines.launch


class DbViewModel(private val dbRepository: DbRepository) : ViewModel() {

    private var utility: Utility = Utility()

    private val _isMovieInWatchlist = MutableLiveData<Boolean>()
    val isMovieInWatchlist: LiveData<Boolean> = _isMovieInWatchlist

    private val _isTvInWatchlist = MutableLiveData<Boolean>()
    val isTvInWatchlist: LiveData<Boolean> = _isTvInWatchlist


    private val _watchlist = MutableLiveData<List<MovieDetails>>()
    val watchlist: LiveData<List<MovieDetails>> = _watchlist

    private val _watchlistTv = MutableLiveData<List<TvSeriesDetail>>()
    val watchlistTv: LiveData<List<TvSeriesDetail>> = _watchlistTv

    init {
        loadMovieWatchlist()
        loadTvWatchlist()
    }

    // Function for Login to user
    fun login(email: String, password: String, callback: (Boolean) -> Unit) {
        viewModelScope.launch {
            try {
                val isDataAvailable = dbRepository.checkLoginData(email, password)
                callback(isDataAvailable)
            } catch (e: Exception) {
                utility.logError("Error logging in", e)
            }
        }
    }

    // Function to insert user
    fun insertUser(users: Users) {
        viewModelScope.launch {
            try {
                val id = dbRepository.insertUser(users)
                utility.debug("Data $users is Inserted on $id")
            } catch (e: Exception) {
                utility.logError("Error inserting user", e)
            }

        }
    }
    // Function to update the User Password
    fun updateUserPassword(email: String, newPassword: String, callback: (Int) -> Unit) {
        viewModelScope.launch {
            try {
                val id = dbRepository.updateUserByEmail(email, newPassword)
                utility.debug("Update done in db $id")
                callback(id)
            } catch (e: Exception) {
                utility.logError("Error updating user password", e)
            }
        }
    }

    // Function to Add movie to Watchlist
    fun addToWatchlistMovie(movieDetails:MovieDetails) {
        viewModelScope.launch {
            try {
                val id = dbRepository.addToWatchlistMovie(movieDetails)
                utility.debug("Data $watchlist is Inserted on $id ")
            } catch (e: Exception) {
                utility.logError("Error inserting user", e)
            }
        }
    }

    // Function to Add tv series to Watchlist
    fun addToWatchlistTv(tvSeriesDetail: TvSeriesDetail) {
        viewModelScope.launch {
            try {
                val id = dbRepository.addToWatchlistTv(tvSeriesDetail)
                utility.debug("Data $watchlist Tv is Inserted on $id ")
            } catch (e: Exception) {
                utility.logError("Error inserting user", e)
            }
        }
    }

    // Function to remove movie to Watchlist
    fun removeMovieFromWatchlist(movieId:Int) {
        viewModelScope.launch {
            try {
                val id = dbRepository.removeMovieFromWatchlist(movieId)
                utility.debug("Data $watchlist is Deleted on $id ")
            } catch (e: Exception) {
                utility.logError("Error deleted movie", e)
            }
        }
    }
    // Function to remove tv series to Watchlist
    fun removeTvFromWatchlist(tvId:Int) {
        viewModelScope.launch {
            try {
                val id = dbRepository.removeTvFromWatchlist(tvId)
                utility.debug("Data $watchlist is Deleted on $id ")
            } catch (e: Exception) {
                utility.logError("Error deleted tv", e)
            }
        }
    }

    // Function to check movie in Watchlist
    fun checkIfMovieInWatchlist(movieId: Int) {
        viewModelScope.launch {
            try {
                val isExist = dbRepository.isMovieInWatchlist(movieId)
                _isMovieInWatchlist.value = isExist
            } catch (e: Exception) {
                utility.logError("Error checking if movie is in watchlist", e)
            }
        }
    }
    // Function to check tv in Watchlist
    fun checkIfTvInWatchlist(tvId: Int) {
        viewModelScope.launch {
            try {
                val isExist = dbRepository.isTvInWatchlist(tvId)
                _isTvInWatchlist.value = isExist
            } catch (e: Exception) {
                utility.logError("Error checking if tv is in watchlist", e)
            }
        }
    }
    // Load the Movie Watchlist
     fun loadMovieWatchlist() {
        viewModelScope.launch {
            try {
                val data = dbRepository.getAllMovieWatchlist()
                _watchlist.value = data
            } catch (e: Exception) {
                utility.logError("Error getting watchlist", e)
            }
        }
    }
    // Load the tv Watchlist
    fun loadTvWatchlist() {
        viewModelScope.launch {
            try {
                val data = dbRepository.getAllTvWatchlist()
                utility.debug("the tv list is $data")
                _watchlistTv.value = data
            } catch (e: Exception) {
                utility.logError("Error getting watchlist", e)
            }
        }
    }

}