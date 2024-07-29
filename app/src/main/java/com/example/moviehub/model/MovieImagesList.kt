package com.example.moviehub.model

data class MovieImagesList(
    val backdrops: List<Backdrop>,
    val id: Int,
    val logos: List<Any>,
    val posters: List<Poster>
)