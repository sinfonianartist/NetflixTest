package com.joshuahale.netflixtest.network.repository

import com.joshuahale.netflixtest.constants.MoviesConstants
import com.joshuahale.netflixtest.model.configuration.Configuration
import com.joshuahale.netflixtest.model.movies.MoviesData
import com.joshuahale.netflixtest.network.api.MoviesApiHelper
import com.joshuahale.netflixtest.network.mapper.ResponseMapper
import io.reactivex.Single
import javax.inject.Inject

class MoviesRepository @Inject constructor(private val apiHelper: MoviesApiHelper) {

    private lateinit var configuration: Configuration

    fun getTrendingMovies(
        mediaType: String = "movie",
        timeWindow: String = "week",
        page: Int
    ): Single<MoviesData> {
        return getConfiguration()
            .flatMap {
                config -> this.configuration = config
                apiHelper.getTrendingMovies(
                    mediaType = mediaType,
                    timeWindow = timeWindow,
                    apiKey = MoviesConstants.API_KEY,
                    page = page)
                    .map { response -> ResponseMapper.getMovies(response, configuration)}
            }
    }

    fun searchMovies(
        query: String,
        page: Int
    ): Single<MoviesData> {
        return getConfiguration()
            .flatMap {
                config -> this.configuration = config
                apiHelper.searchMovies(
                    query = query,
                    page = page,
                    apiKey = MoviesConstants.API_KEY
                ).map { response ->
                    ResponseMapper.getMovies(response, configuration)
                }
            }
    }

    private fun getConfiguration(): Single<Configuration> {
        if (::configuration.isInitialized) { return Single.just(configuration) }
        return apiHelper.getConfiguration(MoviesConstants.API_KEY)
            .map { response -> ResponseMapper.getConfiguration(response) }
    }
}