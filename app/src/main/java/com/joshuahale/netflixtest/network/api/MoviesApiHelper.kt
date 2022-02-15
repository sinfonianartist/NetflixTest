package com.joshuahale.netflixtest.network.api

import com.joshuahale.netflixtest.network.responses.TrendingMoviesResponse
import io.reactivex.Single

interface MoviesApiHelper {

    fun getTrendingMovies(
        mediaType: String,
        timeWindow: String,
        apiKey: String,
        page: Int
    ): Single<TrendingMoviesResponse>
}