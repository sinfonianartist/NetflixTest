package com.joshuahale.netflixtest.ui.moviedetails

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.joshuahale.netflixtest.model.movies.Movie

class MovieDetailsViewModelFactory(
    private val movie: Movie
    ): ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel> create(modelClass: Class<T>) = MovieDetailsViewModel(movie) as T
}