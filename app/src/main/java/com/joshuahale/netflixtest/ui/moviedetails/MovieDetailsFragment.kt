package com.joshuahale.netflixtest.ui.moviedetails

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.joshuahale.netflixtest.R
import com.joshuahale.netflixtest.databinding.FragmentMovieDetailsBinding
import com.joshuahale.netflixtest.model.movies.Movie
import com.squareup.picasso.Picasso
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MovieDetailsFragment : Fragment() {

    private lateinit var movie: Movie
    private lateinit var binding: FragmentMovieDetailsBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        arguments?.let { bundle ->
            MovieDetailsFragmentArgs.fromBundle(bundle).movie?.let { movie = it }
        }
        binding = FragmentMovieDetailsBinding.inflate(inflater, container, false)
        setupUi()
        return binding.root
    }

    private fun setupUi() {
        activity?.title = getString(R.string.movie_details)
        binding.title.text = movie.title
        binding.description.text = movie.description
        Picasso.get()
            .load(movie.backdropUrl)
            .into(binding.backdropImage)
    }
}