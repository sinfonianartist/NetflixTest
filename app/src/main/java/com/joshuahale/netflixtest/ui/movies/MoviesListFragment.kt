package com.joshuahale.netflixtest.ui.movies

import android.os.Bundle
import android.view.*
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.joshuahale.netflixtest.R
import com.joshuahale.netflixtest.databinding.FragmentMoviesListBinding
import com.joshuahale.netflixtest.model.movies.Movie
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
        setHasOptionsMenu(true)
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

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.main_menu, menu)

        val searchItem = menu.findItem(R.id.action_search)
        val searchView = searchItem.actionView as SearchView
        searchView.maxWidth = Integer.MAX_VALUE
        searchView.queryHint = getString(R.string.search_movies)
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                searchItem.collapseActionView()
                return false
            }

            override fun onQueryTextChange(s: String): Boolean {
                return false
            }
        })
        return super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onPause() {
        disposable.clear()
        super.onPause()
    }
}