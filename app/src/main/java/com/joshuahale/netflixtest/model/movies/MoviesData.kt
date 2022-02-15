package com.joshuahale.netflixtest.model.movies

data class MoviesData (
    val page: Int,
    val movies: List<Movie>,
    val totalPages: Int
)
