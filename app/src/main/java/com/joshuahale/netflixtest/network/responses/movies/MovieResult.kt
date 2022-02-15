package com.joshuahale.netflixtest.network.responses.movies

import com.google.gson.annotations.SerializedName

data class MovieResult(
    @SerializedName("poster_path") val posterUri: String?,
    @SerializedName("overview") val overview: String,
    @SerializedName("id") val id: Int,
    @SerializedName("original_title") val title: String,
    @SerializedName("backdrop_path") val backdropUri: String?
)