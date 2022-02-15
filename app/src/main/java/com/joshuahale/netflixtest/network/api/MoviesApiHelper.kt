package com.joshuahale.netflixtest.network.api

import com.joshuahale.netflixtest.network.responses.TrendingMoviesResponse
import io.reactivex.Single

interface MoviesApiHelper {

    fun getTrendingMovies(
        apiKey: String,
        mediaType: String,
        timeWindow: String
    ): Single<TrendingMoviesResponse>
}