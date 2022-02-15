package com.joshuahale.netflixtest.ui.movies

import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.ViewModel
import com.joshuahale.netflixtest.model.movies.MoviesData
import com.joshuahale.netflixtest.network.repository.MoviesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject
import javax.inject.Inject

@HiltViewModel
class MoviesListViewModel @Inject constructor(
    private val repository: MoviesRepository
) : ViewModel(), DefaultLifecycleObserver {

    private var nextPage = 1
    private var totalPages = 0

    private var searchQuery = ""

    private val disposable = CompositeDisposable()
    private var currentMovieType: MovieType = MovieType.Trending
    private var viewStateSubject = PublishSubject.create<MoviesListViewState>()
    private var shouldClearList: Boolean = false

    private enum class MovieType {
        SearchResults,
        Trending
    }

    fun getNextMovies() {
        if (currentMovieType == MovieType.SearchResults) {
            checkIfMovieTypeSwapped(MovieType.SearchResults)
            searchMovies()
        } else {
            checkIfMovieTypeSwapped(MovieType.Trending)
            fetchTrendingMovies()
        }
    }

    fun updateSearchQuery(query: String) {
        if (searchQuery != query) {
            nextPage = 1
            shouldClearList = true
        }
        this.searchQuery = query
        searchMovies()
    }

    private fun fetchTrendingMovies() {
        disposable.add(repository.getTrendingMovies(page = nextPage)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ moviesData ->
                handleMoviesData(moviesData)
            }, { e -> e.printStackTrace()})
        )
    }

    private fun searchMovies() {
        disposable.add(repository.searchMovies(searchQuery, nextPage)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ moviesData ->
                handleMoviesData(moviesData)
            }, { e -> e.printStackTrace()})
        )
    }

    private fun handleMoviesData(moviesData: MoviesData) {
        nextPage = moviesData.page + 1
        totalPages = moviesData.totalPages

        viewStateSubject.onNext(MoviesListViewState(
            clearMovies = shouldClearList,
            movies = moviesData.movies))
        shouldClearList = false
    }

    private fun checkIfMovieTypeSwapped(movieType: MovieType) {
        if (currentMovieType != movieType) {
            currentMovieType = movieType
            nextPage = 1
            shouldClearList = true
        }
    }

    fun onViewStateChanged(): Observable<MoviesListViewState> {
        return viewStateSubject
    }

    override fun onCleared() {
        disposable.clear()
        super.onCleared()
    }
}