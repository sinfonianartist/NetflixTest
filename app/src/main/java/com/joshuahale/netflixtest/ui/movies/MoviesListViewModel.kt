package com.joshuahale.netflixtest.ui.movies

import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModel
import com.joshuahale.netflixtest.model.Movie
import com.joshuahale.netflixtest.model.TrendingMovies
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

    private val disposable = CompositeDisposable()
    private var moviesSubject = createSubject()
    private var currentPage = 1
    private var totalPages = 0

    override fun onResume(owner: LifecycleOwner) {
        super.onResume(owner)
        fetchTrendingMovies()
    }

    private fun fetchTrendingMovies() {
        disposable.add(repository.getTrendingMovies(page = currentPage)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { trendingMovies ->
                    currentPage = trendingMovies.page
                    totalPages = trendingMovies.totalPages
                    moviesSubject.onNext(trendingMovies.movies)
                },
                { error ->
                    moviesSubject.onError(error)
                    moviesSubject = createSubject()
                }
            )
        )
    }

    private fun createSubject() = PublishSubject.create<List<Movie>>()

    fun onTrendingMoviesFound(): Observable<List<Movie>> {
        return moviesSubject
    }

    override fun onCleared() {
        disposable.clear()
        super.onCleared()
    }

}