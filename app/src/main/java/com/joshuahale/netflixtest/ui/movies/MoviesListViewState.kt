package com.joshuahale.netflixtest.ui.movies

import com.joshuahale.netflixtest.model.movies.Movie

data class MoviesListViewState(
    val clearMovies: Boolean,
    val movies: List<Movie>
)