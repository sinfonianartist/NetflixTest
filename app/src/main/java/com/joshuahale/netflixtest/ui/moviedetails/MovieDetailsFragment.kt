package com.joshuahale.netflixtest.ui.moviedetails

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.joshuahale.netflixtest.model.movies.Movie
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MovieDetailsFragment : Fragment() {

    private lateinit var movie: Movie

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        arguments?.let { bundle ->
            MovieDetailsFragmentArgs.fromBundle(bundle).movie?.let { movie = it }
        }
        return super.onCreateView(inflater, container, savedInstanceState)
    }
}