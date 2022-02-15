package com.joshuahale.netflixtest.network.responses.configuration

import com.google.gson.annotations.SerializedName

data class ConfigurationResponse(
    @SerializedName("images") val imageConfiguration: ImageConfiguration
)

data class ImageConfiguration(
    @SerializedName("secure_base_url") val secureBaseUrl: String,
    @SerializedName("backdrop_sizes") val backdropSizes: List<String>,
    @SerializedName("poster_sizes") val posterSizes: List<String>
)