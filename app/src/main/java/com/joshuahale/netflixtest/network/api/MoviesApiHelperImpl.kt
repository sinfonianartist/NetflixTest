package com.joshuahale.netflixtest.network.api

import com.joshuahale.netflixtest.network.responses.configuration.ConfigurationResponse
import io.reactivex.Single
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

    override fun getConfiguration(apiKey: String) = apiService.getConfiguration(apiKey)
}