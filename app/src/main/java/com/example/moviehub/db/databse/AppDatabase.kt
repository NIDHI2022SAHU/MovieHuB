package com.example.moviehub.db.databse

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.moviehub.db.dao.UserDao
import com.example.moviehub.db.entity.Users
import com.example.moviehub.model.BelongsToCollectionTypeConverter
import com.example.moviehub.model.GenreTypeConverter
import com.example.moviehub.model.GenreTypeConverterTv
import com.example.moviehub.model.MovieDetails
import com.example.moviehub.model.ProductionCompanyTypeConverter
import com.example.moviehub.model.ProductionCountryTypeConverter
import com.example.moviehub.model.SpokenLanguageTypeConverter
import com.example.moviehub.model.StringTypeConverter
import com.example.moviehub.model.TvSeriesDetail
import com.example.moviehub.utilities.Constants.Companion.DB_NAME

@Database(entities = [Users::class,MovieDetails::class,TvSeriesDetail::class], version = 2, exportSchema = false)
@TypeConverters(
    BelongsToCollectionTypeConverter::class,
    GenreTypeConverter::class,
    ProductionCompanyTypeConverter::class,
    ProductionCountryTypeConverter::class,
    SpokenLanguageTypeConverter::class,
    StringTypeConverter::class,
    GenreTypeConverterTv::class
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    companion object {
        private var instance: AppDatabase? = null
        fun getDatabase(context: Context): AppDatabase {
            if (instance == null) {
                synchronized(AppDatabase::class) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        AppDatabase::class.java,
                        DB_NAME
                    ).build()
                }
            }
            return instance!!
        }
    }
}
