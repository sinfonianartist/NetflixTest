package com.joshuahale.netflixtest.network.api

import com.joshuahale.netflixtest.network.responses.configuration.ConfigurationResponse
import com.joshuahale.netflixtest.network.responses.movies.TrendingMoviesResponse
import io.reactivex.Single

interface MoviesApiHelper {

    fun getTrendingMovies(
        mediaType: String,
        timeWindow: String,
        apiKey: String,
        page: Int
    ): Single<TrendingMoviesResponse>

    fun getConfiguration(apiKey: String): Single<ConfigurationResponse>
}