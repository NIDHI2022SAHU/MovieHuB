package com.example.moviehub.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.moviehub.db.entity.Users
import com.example.moviehub.model.MovieDetails
import com.example.moviehub.model.TvSeriesDetail

@Dao
interface UserDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(users: Users):Long

    @Query("SELECT EXISTS(SELECT 1 FROM Users WHERE email = :email AND password = :password)")
    suspend fun checkLoginData(email: String, password: String): Boolean

    @Query("UPDATE Users SET password = :newPassword WHERE email = :email")
    suspend fun updateUserByEmail(email: String, newPassword: String):Int
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addToWatchlistMovie(movieDetails: MovieDetails)
    @Query("SELECT EXISTS(SELECT 1 FROM watchlist WHERE id = :movieId)")
    suspend fun isMovieInWatchlist(movieId: Int): Boolean
    @Query("SELECT * From watchlist ORDER By id Asc")
    suspend fun getAllWatchlistMovie():List<MovieDetails>
     @Query("DELETE FROM watchlist WHERE id = :movieId ")
    suspend fun removeMovieFromWatchlist(movieId: Int)
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addToWatchlistTv(tvSeriesDetail: TvSeriesDetail)
    @Query("SELECT EXISTS(SELECT 1 FROM watchlist_tv WHERE id = :tvId)")
    suspend fun isTvInWatchlist(tvId: Int): Boolean
    @Query("SELECT * From watchlist_tv ORDER By id Asc")
    suspend fun getAllWatchlistTv():List<TvSeriesDetail>
    @Query("DELETE FROM watchlist_tv WHERE id = :tvId ")
    suspend fun removeTvFromWatchlist(tvId: Int)
}