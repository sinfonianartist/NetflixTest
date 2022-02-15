package com.joshuahale.netflixtest.model.movies

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Movie(
    val backdropUrl: String,
    val posterUrl: String,
    val title: String,
    val description: String
): Parcelable