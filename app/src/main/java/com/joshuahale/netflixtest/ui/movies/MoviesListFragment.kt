package com.joshuahale.netflixtest.ui.movies

import android.os.Bundle
import android.view.*
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.joshuahale.netflixtest.R
import com.joshuahale.netflixtest.constants.MoviesConstants
import com.joshuahale.netflixtest.databinding.FragmentMoviesListBinding
import com.joshuahale.netflixtest.error.ErrorDialogBuilder
import com.joshuahale.netflixtest.error.interfaces.OnErrorDialogListener
import com.joshuahale.netflixtest.model.movies.Movie
import com.joshuahale.netflixtest.ui.recyclerview.GridViewSpacingDecoration
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers


@AndroidEntryPoint
class MoviesListFragment : Fragment(), OnErrorDialogListener {

    private lateinit var binding: FragmentMoviesListBinding
    private val viewModel: MoviesListViewModel by viewModels()
    private val disposable = CompositeDisposable()
    private var moviesAdapter = MoviesAdapter { movie ->
        showMovieDetails(movie)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        setHasOptionsMenu(true)
        binding = FragmentMoviesListBinding.inflate(inflater, container, false)
        setupUi()
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        setupObserver()
        viewModel.getNextMovies()
    }

    private fun setupUi() {
        activity?.title = getString(R.string.trending_movies)
        lifecycle.addObserver(viewModel)
        val layoutManager = GridLayoutManager(context, MoviesConstants.NUMBER_OF_POSTER_COLUMNS)
        binding.recyclerView.layoutManager = layoutManager
        binding.recyclerView.adapter = moviesAdapter
        val spacing = resources.getDimensionPixelSize(R.dimen.grid_view_spacing)
        val spacingDecoration = GridViewSpacingDecoration(spacing)
        binding.recyclerView.addItemDecoration(spacingDecoration)
        binding.recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val lastVisibleItem = layoutManager.findLastCompletelyVisibleItemPosition()
                if (lastVisibleItem == moviesAdapter.itemCount - 1) {
                    viewModel.getNextMovies()
                }
            }
        })
    }

    private fun setupObserver() {
        disposable.add(
            viewModel.onViewStateChanged()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ state ->
                    moviesAdapter.addMovies(state.movies, state.clearMovies)
                }, { e ->
                    e.printStackTrace()
                    ErrorDialogBuilder.getErrorDialog(requireContext(),
                        getString(R.string.error_network_description), this)

                })
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
        setupSearch(menu.findItem(R.id.action_search))
        return super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onRetryClicked() {
        viewModel.getNextMovies()
    }

    private fun setupSearch(searchItem: MenuItem) {
        searchItem.setOnActionExpandListener(object : MenuItem.OnActionExpandListener {
            override fun onMenuItemActionCollapse(item: MenuItem): Boolean {
                viewModel.setMovieType(MovieType.Trending)
                viewModel.getNextMovies()
                return true
            }

            override fun onMenuItemActionExpand(item: MenuItem): Boolean {
                viewModel.setMovieType(MovieType.SearchResults)
                return true
            }
        })
        val searchView = searchItem.actionView as SearchView
        searchView.setIconifiedByDefault(false)
        searchView.maxWidth = Integer.MAX_VALUE
        searchView.queryHint = getString(R.string.search_movies)
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                viewModel.searchMovies(query)
                return false
            }

            override fun onQueryTextChange(s: String): Boolean {
                return false
            }
        })
    }

    override fun onPause() {
        disposable.clear()
        super.onPause()
    }
}