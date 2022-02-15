package com.joshuahale.netflixtest.model.configuration

data class Configuration(
    val baseUrl: String = "",
    val backdropSizes: List<String>,
    val posterSizes: List<String>
)