package com.joshuahale.netflixtest.network.responses.movies

import com.google.gson.annotations.SerializedName

data class MoviesResponse(
    @SerializedName("page") val page: Int,
    @SerializedName("total_pages") val totalPages: Int,
    @SerializedName("results") val moviesListResult: List<MovieResult>?
)