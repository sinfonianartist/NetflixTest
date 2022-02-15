package com.joshuahale.netflixtest.network.mapper

import com.joshuahale.netflixtest.model.configuration.Configuration
import com.joshuahale.netflixtest.model.movies.Movie
import com.joshuahale.netflixtest.model.movies.TrendingMovies
import com.joshuahale.netflixtest.network.responses.configuration.ConfigurationResponse
import com.joshuahale.netflixtest.network.responses.movies.MovieResult
import com.joshuahale.netflixtest.network.responses.movies.TrendingMoviesResponse

object ResponseMapper {

    fun getConfiguration(response: ConfigurationResponse): Configuration {
        val baseUrl = response.imageConfiguration.secureBaseUrl
        val backdropSize = "w1280"
        val posterSize = "w500"
        return Configuration(baseUrl, backdropSize, posterSize)
    }

    fun getTrendingMovies(response: TrendingMoviesResponse, configuration: Configuration): TrendingMovies {
        val currentPage = response.page
        val totalPages = response.totalPages
        val movies = ArrayList<Movie>()
        response.moviesListResult?.let { results ->
            for (movie in results) {
                movies.add(getMovie(movie, configuration))
            }
        }
        return TrendingMovies(
            page = currentPage,
            totalPages = totalPages,
            movies = movies
        )
    }

    private fun getMovie(movieResult: MovieResult, configuration: Configuration): Movie {
        val url = "${configuration.baseUrl}${configuration.posterSize}${movieResult.posterPath}"
        return Movie(
            posterUrl = url
        )
    }
}