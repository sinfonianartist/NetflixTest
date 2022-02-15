package com.joshuahale.netflixtest.network.api

import com.joshuahale.netflixtest.network.responses.TrendingMoviesResponse
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface MoviesApiService {

    @GET("/trending/{media_type}/{time_window}")
    fun getTrendingMovies(
        @Query("api_key") apiKey: String,
        @Path("media_type") mediaType: String,
        @Path("time_window") timeWindow: String
    ): Single<TrendingMoviesResponse>
}