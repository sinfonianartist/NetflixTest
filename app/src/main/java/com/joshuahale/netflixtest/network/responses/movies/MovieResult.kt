package com.joshuahale.netflixtest.network.responses.movies

import com.google.gson.annotations.SerializedName
import com.joshuahale.netflixtest.model.configuration.Configuration

data class MovieResult(
    @SerializedName("poster_path") val posterPath: String?,
    @SerializedName("overview") val overview: String,
    @SerializedName("release_date") val releaseDate: String,
    @SerializedName("id") val id: Int,
    @SerializedName("original_title") val title: String,
    @SerializedName("backdrop_path") val backdropPath: String?
)

enum class ImageType {
    BACKDROP,
    POSTER
}

fun MovieResult.getImageUrl(configuration: Configuration, imageType: ImageType): String {
    var size = ""
    var imageUri = ""

    when (imageType) {
        ImageType.BACKDROP -> {
            size = configuration.backdropSize
            imageUri = backdropPath ?: ""
        }
        ImageType.POSTER -> {
            size = configuration.posterSize
            imageUri = posterPath ?: ""
        }
    }

    return "${configuration.baseUrl}${size}${imageUri}"
}