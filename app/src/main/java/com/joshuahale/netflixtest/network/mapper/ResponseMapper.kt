package com.joshuahale.netflixtest.network.mapper

import com.joshuahale.netflixtest.model.configuration.Configuration
import com.joshuahale.netflixtest.model.movies.Movie
import com.joshuahale.netflixtest.model.movies.MoviesData
import com.joshuahale.netflixtest.network.responses.configuration.ConfigurationResponse
import com.joshuahale.netflixtest.network.responses.movies.MovieResult
import com.joshuahale.netflixtest.network.responses.movies.MoviesResponse
import com.joshuahale.netflixtest.ui.recyclerview.ImageSizeHelper

object ResponseMapper {

    fun getConfiguration(response: ConfigurationResponse): Configuration {
        val imageConfiguration = response.imageConfiguration
        val baseUrl = imageConfiguration.secureBaseUrl
        return Configuration(
            baseUrl = baseUrl,
            backdropSizes = imageConfiguration.backdropSizes,
            posterSizes = imageConfiguration.posterSizes)
    }

    fun getMovies(response: MoviesResponse, configuration: Configuration): MoviesData {
        val currentPage = response.page
        val totalPages = response.totalPages
        val movies = ArrayList<Movie>()
        val backdropSize = ImageSizeHelper.backdropSize(configuration.backdropSizes)
        val posterSize = ImageSizeHelper.posterSize(configuration.posterSizes)
        val backdropPath = "${configuration.baseUrl}${backdropSize}"
        val posterPath = "${configuration.baseUrl}${posterSize}"
        response.moviesListResult?.let { results ->
            for (movie in results) {
                movies.add(getMovie(movie, backdropPath, posterPath))
            }
        }
        return MoviesData(
            page = currentPage,
            totalPages = totalPages,
            movies = movies
        )
    }

    private fun getMovie(
        movieResult: MovieResult,
        backdropPath: String,
        posterPath: String): Movie {
        return Movie(
            posterUrl = "${posterPath}${movieResult.posterUri}",
            backdropUrl = "${backdropPath}${movieResult.backdropUri}",
            title = movieResult.title,
            description = movieResult.overview,
            id = movieResult.id
        )
    }
}