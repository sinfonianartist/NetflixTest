package com.joshuahale.netflixtest.network.repository

import com.joshuahale.netflixtest.constants.MoviesConstants
import com.joshuahale.netflixtest.model.TrendingMovies
import com.joshuahale.netflixtest.network.api.MoviesApiHelper
import com.joshuahale.netflixtest.network.mapper.ResponseMapper
import io.reactivex.Single
import javax.inject.Inject

class MoviesRepository @Inject constructor(private val apiHelper: MoviesApiHelper) {

    fun getTrendingMovies(
        mediaType: String = "movie",
        timeWindow: String = "week"
    ): Single<TrendingMovies> {
        return apiHelper.getTrendingMovies(MoviesConstants.API_KEY, mediaType, timeWindow)
            .map { response -> ResponseMapper.getTrendingMovies(response)}
    }
}