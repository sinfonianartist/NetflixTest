package com.joshuahale.netflixtest.network.api

import com.joshuahale.netflixtest.network.responses.configuration.ConfigurationResponse
import com.joshuahale.netflixtest.network.responses.movies.MoviesResponse
import io.reactivex.Single

interface MoviesApiHelper {

    fun getTrendingMovies(
        mediaType: String,
        timeWindow: String,
        apiKey: String,
        page: Int
    ): Single<MoviesResponse>

    fun getConfiguration(apiKey: String): Single<ConfigurationResponse>

    fun searchMovies(
        query: String,
        apiKey: String,
        page: Int
    ): Single<MoviesResponse>
}