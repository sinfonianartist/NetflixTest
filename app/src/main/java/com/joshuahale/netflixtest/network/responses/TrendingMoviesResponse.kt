package com.joshuahale.netflixtest.network.responses

import com.google.gson.annotations.SerializedName

data class TrendingMoviesResponse(
    @SerializedName("page") val page: Int,
    @SerializedName("total_pages") val totalPages: Int,
    @SerializedName("total_results") val totalResults: Int,
    @SerializedName("results") val moviesListResult: List<MovieResult>?
)