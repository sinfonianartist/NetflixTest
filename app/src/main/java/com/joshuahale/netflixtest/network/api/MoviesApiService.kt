package com.joshuahale.netflixtest.network.api

import com.joshuahale.netflixtest.network.responses.configuration.ConfigurationResponse
import com.joshuahale.netflixtest.network.responses.movies.TrendingMoviesResponse
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface MoviesApiService {

    @GET("trending/{media_type}/{time_window}")
    fun getTrendingMovies(
        @Path("media_type") mediaType: String,
        @Path("time_window") timeWindow: String,
        @Query("api_key") apiKey: String,
        @Query("page") page: Int,
    ): Single<TrendingMoviesResponse>

    @GET("configuration")
    fun getConfiguration(
        @Query("api_key") apiKey: String
    ): Single<ConfigurationResponse>
}