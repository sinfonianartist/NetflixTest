package com.joshuahale.netflixtest.network.api

import javax.inject.Inject

class MoviesApiHelperImpl @Inject constructor(
    private val apiService: MoviesApiService) : MoviesApiHelper {

    override fun getTrendingMovies(
        mediaType: String,
        timeWindow: String,
        apiKey: String,
        page: Int
    ) = apiService.getTrendingMovies(
        mediaType = mediaType,
        timeWindow = timeWindow,
        apiKey = apiKey,
        page = page
    )

    override fun searchMovies(
        query: String,
        apiKey: String,
        page: Int
    ) = apiService.searchMovies(
        searchQuery = query,
        apiKey = apiKey,
        page = page
    )

    override fun getConfiguration(apiKey: String) = apiService.getConfiguration(apiKey)
}