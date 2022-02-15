package com.joshuahale.netflixtest.network.mapper

import com.joshuahale.netflixtest.model.Movie
import com.joshuahale.netflixtest.model.TrendingMovies
import com.joshuahale.netflixtest.network.responses.MovieResult
import com.joshuahale.netflixtest.network.responses.TrendingMoviesResponse

object ResponseMapper {

    fun getTrendingMovies(response: TrendingMoviesResponse): TrendingMovies {
        val currentPage = response.page
        val totalPages = response.totalPages
        val movies = ArrayList<Movie>()
        response.moviesListResult?.let { results ->
            for (movie in results) {
                movies.add(getMovie(movie))
            }
        }
        return TrendingMovies(
            page = currentPage,
            totalPages = totalPages,
            movies = movies
        )
    }

    private fun getMovie(movieResult: MovieResult): Movie {
        return Movie(
            title = movieResult.title
        )
    }
}