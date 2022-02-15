package com.joshuahale.netflixtest.ui.movies

import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
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

    private var isLoading = false

    fun setMovieType(movieType: MovieType) {
        if (movieType != currentMovieType) {
            currentMovieType = movieType
            nextPage = 1
            shouldClearList = true
        }
    }

    fun getNextMovies() {
        if (!isLoading) {
            if (currentMovieType == MovieType.SearchResults) {
                searchMovies()
            } else {
                fetchTrendingMovies()
            }
        }
    }

    fun searchMovies(query: String) {
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
            .doOnSubscribe { isLoading = true }
            .doOnSuccess { isLoading = false }
            .doOnError { isLoading = false }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ moviesData ->
                handleMoviesData(moviesData)
            }, { e ->
                viewStateSubject.onError(e)
                viewStateSubject = PublishSubject.create()
            })
        )
    }

    private fun searchMovies() {
        disposable.add(repository.searchMovies(searchQuery, nextPage)
            .subscribeOn(Schedulers.io())
            .doOnSubscribe { isLoading = true }
            .doOnSuccess { isLoading = false }
            .doOnError { isLoading = false }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ moviesData ->
                handleMoviesData(moviesData)
            }, { e ->
                viewStateSubject.onError(e)
                viewStateSubject = PublishSubject.create()
            })
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

    fun onViewStateChanged(): Observable<MoviesListViewState> {
        return viewStateSubject
    }

    override fun onPause(owner: LifecycleOwner) {
        disposable.clear()
        nextPage = 1
        super.onPause(owner)
    }
}