package com.joshuahale.netflixtest.network.api

import javax.inject.Inject

class MoviesApiHelperImpl @Inject constructor(
    private val apiService: MoviesApiService) : MoviesApiHelper {

    override fun getTrendingMovies(
        apiKey: String,
        mediaType: String,
        timeWindow: String
    ) = apiService.getTrendingMovies(apiKey, mediaType, timeWindow)
}