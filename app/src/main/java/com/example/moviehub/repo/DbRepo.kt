package com.example.moviehub.repo

import com.example.moviehub.db.dao.UserDao
import com.example.moviehub.db.entity.Users
import com.example.moviehub.model.MovieDetails
import com.example.moviehub.model.TvSeriesDetail
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext


class DbRepository(private val userDao: UserDao) {
    suspend fun insertUser(users: Users):Long {
        return withContext(Dispatchers.IO) {
            userDao.insertUser(users)
        }
    }

    suspend fun checkLoginData(email: String, password: String): Boolean {
        return withContext(Dispatchers.IO) {
            userDao.checkLoginData(email, password)
        }
    }

    suspend fun updateUserByEmail(email: String, newPassword: String):Int {
        return withContext(Dispatchers.IO) {
            userDao.updateUserByEmail(email, newPassword)
        }
    }

    suspend fun addToWatchlistMovie(movieDetails: MovieDetails) {
        return withContext(Dispatchers.IO) {
            userDao.addToWatchlistMovie(movieDetails)
        }
    }
    suspend fun addToWatchlistTv(tvSeriesDetail: TvSeriesDetail) {
        return withContext(Dispatchers.IO) {
            userDao.addToWatchlistTv(tvSeriesDetail)
        }
    }
    suspend fun removeMovieFromWatchlist(movieId: Int) {
        return withContext(Dispatchers.IO) {
            userDao.removeMovieFromWatchlist(movieId)
        }
    }
    suspend fun removeTvFromWatchlist(tvId: Int) {
        return withContext(Dispatchers.IO) {
            userDao.removeTvFromWatchlist(tvId)
        }
    }
    suspend fun isMovieInWatchlist(movieId: Int):Boolean{
        return withContext(Dispatchers.IO) {
            userDao.isMovieInWatchlist(movieId)
        }
    }
    suspend fun isTvInWatchlist(tvId: Int):Boolean{
        return withContext(Dispatchers.IO) {
            userDao.isTvInWatchlist(tvId)
        }
    }
    suspend fun getAllMovieWatchlist():List<MovieDetails> {
        return withContext(Dispatchers.IO) {
            userDao.getAllWatchlistMovie( )
        }
    }
    suspend fun getAllTvWatchlist():List<TvSeriesDetail> {
        return withContext(Dispatchers.IO) {
            userDao.getAllWatchlistTv( )
        }
    }
}