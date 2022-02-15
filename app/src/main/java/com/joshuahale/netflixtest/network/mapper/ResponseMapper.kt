package com.joshuahale.netflixtest.network.mapper

import com.joshuahale.netflixtest.model.configuration.Configuration
import com.joshuahale.netflixtest.model.movies.Movie
import com.joshuahale.netflixtest.model.movies.MoviesData
import com.joshuahale.netflixtest.network.responses.configuration.ConfigurationResponse
import com.joshuahale.netflixtest.network.responses.movies.ImageType
import com.joshuahale.netflixtest.network.responses.movies.MovieResult
import com.joshuahale.netflixtest.network.responses.movies.MoviesResponse
import com.joshuahale.netflixtest.network.responses.movies.getImageUrl

object ResponseMapper {

    fun getConfiguration(response: ConfigurationResponse): Configuration {
        val baseUrl = response.imageConfiguration.secureBaseUrl
        val backdropSize = "w1280"
        val posterSize = "w500"
        return Configuration(baseUrl, backdropSize, posterSize)
    }

    fun getMovies(response: MoviesResponse, configuration: Configuration): MoviesData {
        val currentPage = response.page
        val totalPages = response.totalPages
        val movies = ArrayList<Movie>()
        response.moviesListResult?.let { results ->
            for (movie in results) {
                movies.add(getMovie(movie, configuration))
            }
        }
        return MoviesData(
            page = currentPage,
            totalPages = totalPages,
            movies = movies
        )
    }

    private fun getMovie(movieResult: MovieResult, configuration: Configuration): Movie {
        val backdropUrl = movieResult.getImageUrl(configuration, ImageType.BACKDROP)
        val posterUrl = movieResult.getImageUrl(configuration, ImageType.POSTER)
        return Movie(
            posterUrl = posterUrl,
            backdropUrl = backdropUrl,
            title = movieResult.title,
            description = movieResult.overview
        )
    }
}