package com.example.moviehub.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.annotations.SerializedName
import com.google.gson.reflect.TypeToken


@Entity(tableName = "watchlist")
data class MovieDetails(
    val adult: Boolean,
    @SerializedName("backdrop_path")
    val backdropPath: String,
    @SerializedName("belongs_to_collection")
    val belongsToCollection: BelongsToCollection?,
    val budget: Int,
    val genres: List<Genre>,
    val homepage: String,
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    @SerializedName("imdb_id")
    val imdbId: String,
    @SerializedName("origin_country")
    val originCountry: List<String>,
    @SerializedName("original_language")
    val originalLanguage: String,
    @SerializedName("original_title")
    val originalTitle: String,
    val overview: String,
    val popularity: Double,
    @SerializedName("poster_path")
    var posterPath: String,
    @SerializedName("production_companies")
    val productionCompanies: List<ProductionCompany>,
    @SerializedName("production_countries")
    val productionCountries: List<ProductionCountry>,
    @SerializedName("release_date")
    val releaseDate: String,
    val revenue: Int,
    val runtime: Int,
    @SerializedName("spoken_languages")
    val spokenLanguages: List<SpokenLanguage>,
    val status: String,
    val tagline: String,
    val title: String,
    val video: Boolean,
    @SerializedName("vote_average")
    val voteAverage: Double,
    @SerializedName("vote_count")
    val voteCount: Int
)
class BelongsToCollectionTypeConverter {
    @TypeConverter
    fun fromBelongsToCollection(belongsToCollection: BelongsToCollection?): String {
        return if (belongsToCollection == null) {
            ""
        } else {
            Gson().toJson(belongsToCollection)
        }
    }

    @TypeConverter
    fun toBelongsToCollection(belongsToCollectionString: String): BelongsToCollection? {
        if (belongsToCollectionString.isEmpty()) {
            return null
        }
        return Gson().fromJson(belongsToCollectionString, BelongsToCollection::class.java)
    }
}

class GenreTypeConverter {
    @TypeConverter
    fun fromGenreList(genres: List<Genre>): String {
        return Gson().toJson(genres)
    }

    @TypeConverter
    fun toGenreList(genresString: String): List<Genre> {
        return Gson().fromJson(genresString, object : TypeToken<List<Genre>>() {}.type)
    }
}

class ProductionCompanyTypeConverter {
    @TypeConverter
    fun fromProductionCompanyList(productionCompanies: List<ProductionCompany>): String {
        return Gson().toJson(productionCompanies)
    }

    @TypeConverter
    fun toProductionCompanyList(productionCompaniesString: String): List<ProductionCompany> {
        return Gson().fromJson(productionCompaniesString, object : TypeToken<List<ProductionCompany>>() {}.type)
    }
}

class ProductionCountryTypeConverter {
    @TypeConverter
    fun fromProductionCountryList(productionCountries: List<ProductionCountry>): String {
        return Gson().toJson(productionCountries)
    }

    @TypeConverter
    fun toProductionCountryList(productionCountriesString: String): List<ProductionCountry> {
        return Gson().fromJson(productionCountriesString, object : TypeToken<List<ProductionCountry>>() {}.type)
    }
}

class SpokenLanguageTypeConverter {
    @TypeConverter
    fun fromSpokenLanguageList(spokenLanguages: List<SpokenLanguage>): String {
        return Gson().toJson(spokenLanguages)
    }

    @TypeConverter
    fun toSpokenLanguageList(spokenLanguagesString: String): List<SpokenLanguage> {
        return Gson().fromJson(spokenLanguagesString, object : TypeToken<List<SpokenLanguage>>() {}.type)
    }
}
class StringTypeConverter {
    @TypeConverter
    fun fromStringList(stringList: List<String>): String {
        return Gson().toJson(stringList)
    }

    @TypeConverter
    fun toStringList(stringListString: String): List<String> {
        return Gson().fromJson(stringListString, object : TypeToken<List<String>>() {}.type)
    }
}