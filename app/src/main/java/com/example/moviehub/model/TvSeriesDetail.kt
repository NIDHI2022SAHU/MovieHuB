package com.example.moviehub.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import com.google.gson.Gson
import com.google.gson.annotations.SerializedName
import com.google.gson.reflect.TypeToken

@Entity(tableName = "watchlist_tv")
data class TvSeriesDetail(
    val adult: Boolean,
    @SerializedName("backdrop_path")
    val backdropPath: String,
    @SerializedName("first_air_date")
    val firstAirDate: String?,
    @TypeConverters(GenreTypeConverterTv::class)
    @SerializedName("genre_ids")
    val genreIds: List<Int>,
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val name: String?,
    @SerializedName("origin_country")
    val originCountry: List<String>,
    @SerializedName("original_language")
    val originalLanguage: String,
    @SerializedName("original_name")
    val originalName: String?,
    val overview: String,
    val popularity: Double,
    @SerializedName("poster_path")
    val posterPath: String,
    @SerializedName("vote_average")
    val voteAverage: Double,
    @SerializedName("vote_count")
    val voteCount: Int
)

class GenreTypeConverterTv {
    @TypeConverter
    fun fromGenreList(genres: List<Int>): String {
        return Gson().toJson(genres)
    }

    @TypeConverter
    fun toGenreList(genresString: String): List<Int> {
        return Gson().fromJson(genresString, object : TypeToken<List<Int>>() {}.type)
    }
}