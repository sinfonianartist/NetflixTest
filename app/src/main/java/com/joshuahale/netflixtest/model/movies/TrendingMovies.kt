package com.joshuahale.netflixtest.model.movies

data class TrendingMovies (
    val page: Int,
    val movies: List<Movie>,
    val totalPages: Int
)
