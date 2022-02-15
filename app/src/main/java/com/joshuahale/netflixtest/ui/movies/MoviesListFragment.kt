package com.joshuahale.netflixtest.ui.movies

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.joshuahale.netflixtest.databinding.FragmentMoviesListBinding
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
    private var moviesAdapter = TrendingMoviesAdapter()

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

    override fun onPause() {
        disposable.clear()
        super.onPause()
    }
}