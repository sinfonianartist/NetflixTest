package com.joshuahale.netflixtest.ui.movies

import android.os.Build
import androidx.lifecycle.LifecycleOwner
import com.joshuahale.netflixtest.model.movies.MoviesData
import com.joshuahale.netflixtest.network.repository.MoviesRepository
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import dagger.hilt.android.testing.HiltTestApplication
import io.reactivex.Single
import junit.framework.Assert.assertNotNull
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentMatchers.anyInt
import org.mockito.ArgumentMatchers.anyString
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [Build.VERSION_CODES.N], application = HiltTestApplication::class)
class MoviesListViewModelTest {

    private lateinit var viewModel: MoviesListViewModel

    @Mock
    private lateinit var repository: MoviesRepository

    @Mock
    private lateinit var lifecycleOwner: LifecycleOwner

    @Mock
    private lateinit var moviesData: MoviesData

    @Before
    fun setup() {
        MockitoAnnotations.openMocks(this)
        viewModel = MoviesListViewModel(repository)
        whenever(repository.searchMovies(anyString(), anyInt()))
            .thenReturn(Single.just(moviesData))
        whenever(repository.getTrendingMovies(anyString(), anyString(), anyInt()))
            .thenReturn(Single.just(moviesData))
    }

    @Test
    fun testNotNull() {
        assertNotNull(viewModel)
    }

    @Test
    fun testSetMovieType() {
        viewModel.setMovieType(MovieType.Trending)
        viewModel.getNextMovies()
        verify(repository).getTrendingMovies(anyString(), anyString(), anyInt())
        viewModel.setMovieType(MovieType.SearchResults)
        viewModel.getNextMovies()
        verify(repository).searchMovies(anyString(), anyInt())
    }
}