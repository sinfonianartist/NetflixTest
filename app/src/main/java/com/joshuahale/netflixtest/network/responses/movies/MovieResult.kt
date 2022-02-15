package com.joshuahale.netflixtest.network.responses.movies

import com.google.gson.annotations.SerializedName
import com.joshuahale.netflixtest.model.configuration.Configuration
import com.joshuahale.netflixtest.ui.recyclerview.ImageSizeHelper

data class MovieResult(
    @SerializedName("poster_path") val posterUri: String?,
    @SerializedName("overview") val overview: String,
    @SerializedName("release_date") val releaseDate: String,
    @SerializedName("id") val id: Int,
    @SerializedName("original_title") val title: String,
    @SerializedName("backdrop_path") val backdropUri: String?
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
            size = ImageSizeHelper.backdropSize(configuration.backdropSizes)
            imageUri = backdropUri ?: ""
        }
        ImageType.POSTER -> {
            size = ImageSizeHelper.posterSize(configuration.posterSizes)
            imageUri = posterUri ?: ""
        }
    }

    return "${configuration.baseUrl}${size}${imageUri}"
}