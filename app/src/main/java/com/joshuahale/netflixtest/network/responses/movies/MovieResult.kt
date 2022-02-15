package com.joshuahale.netflixtest.network.responses.movies

import com.google.gson.annotations.SerializedName

data class MovieResult(
    @SerializedName("poster_path") val posterPath: String?,
    @SerializedName("overview") val overview: String,
    @SerializedName("release_date") val releaseDate: String,
    @SerializedName("id") val id: Int,
    @SerializedName("original_title") val title: String,
    @SerializedName("backdrop_path") val backdropPath: String?
)