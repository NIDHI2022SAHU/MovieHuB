package com.example.moviehub.model

import com.google.gson.annotations.SerializedName

data class TvSeriesList(
    val page: Int,
    val results: List<TvSeriesDetail>,
    @SerializedName("total_pages")
    val totalPages: Int,
    @SerializedName("total_results")
    val totalResults: Int
)