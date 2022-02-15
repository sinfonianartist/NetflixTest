package com.joshuahale.netflixtest.ui.movies

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.joshuahale.netflixtest.R
import com.joshuahale.netflixtest.databinding.FragmentMoviesListBinding
import com.joshuahale.netflixtest.model.movies.Movie
import com.joshuahale.netflixtest.ui.moviedetails.MovieDetailsFragment
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

@AndroidEntryPoint
class MoviesListFragment : Fragment() {

    companion object {
        private const val COLUMNS = 3
    }

    private lateinit var binding: FragmentMoviesListBinding
    private val viewModel: MoviesListViewModel by viewModels()
    private val disposable = CompositeDisposable()
    private var moviesAdapter = TrendingMoviesAdapter { movie ->
        showMovieDetails(movie)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMoviesListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        setupUi()
        setupObserver()
    }

    private fun setupUi() {
        activity?.title = getString(R.string.movie_list)
        lifecycle.addObserver(viewModel)
        val layoutManager = GridLayoutManager(context, COLUMNS)
        binding.recyclerView.layoutManager = layoutManager
        binding.recyclerView.adapter = moviesAdapter
    }

    private fun setupObserver() {
        disposable.add(viewModel.onTrendingMoviesFound()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { movies -> moviesAdapter.addMovies(movies)},
                { error -> error.printStackTrace() }
            )
        )
    }

    private fun showMovieDetails(movie: Movie) {
        val action = MoviesListFragmentDirections
            .actionMoviesFragmentToMovieDetailsFragment()
            .setMovie(movie)
        findNavController().navigate(action)
    }

    override fun onPause() {
        disposable.clear()
        super.onPause()
    }
}