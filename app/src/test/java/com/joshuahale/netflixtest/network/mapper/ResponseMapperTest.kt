package com.joshuahale.netflixtest.network.mapper

import android.os.Build
import com.joshuahale.netflixtest.model.configuration.Configuration
import com.joshuahale.netflixtest.network.responses.configuration.ConfigurationResponse
import com.joshuahale.netflixtest.network.responses.configuration.ImageConfiguration
import com.joshuahale.netflixtest.network.responses.movies.MovieResult
import com.joshuahale.netflixtest.network.responses.movies.MoviesResponse
import junit.framework.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [Build.VERSION_CODES.N])
class ResponseMapperTest {

    private val configurationResponse = ConfigurationResponse(
        ImageConfiguration(
            secureBaseUrl = "https://www.themoviedb.org/3/",
            backdropSizes = arrayListOf("w300", "w780", "w1280", "original"),
            posterSizes = arrayListOf("w92", "w154", "w185", "w342", "w500", "w780", "original")
        )
    )

    private val testConfiguration = Configuration(
        baseUrl = "https://www.themoviedb.org/3/",
        backdropSizes = arrayListOf("w300", "w780", "w1280", "original"),
        posterSizes = arrayListOf("w92", "w154", "w185", "w342", "w500", "w780", "original")

    )

    private val moviesResponse = MoviesResponse(
        page = 7,
        totalPages = 1000,
        moviesListResult = arrayListOf(
            MovieResult(
                posterUri = "/abc.jpg",
                overview = "This is a test overview",
                id = 4,
                title = "Test Title",
                backdropUri = "/backdrop.jpg"
            )
        )
    )

    @Test
    fun testConfigurationParsing() {
        val configuration = ResponseMapper.getConfiguration(configurationResponse)
        assertEquals("https://www.themoviedb.org/3/", configuration.baseUrl)
        assertEquals("w300", configuration.backdropSizes[0])
        assertEquals("w780", configuration.backdropSizes[1])
        assertEquals("w92", configuration.posterSizes[0])
        assertEquals("original",
            configuration.posterSizes[configuration.posterSizes.lastIndex])
    }

    @Test
    fun testMoviesParsing() {
        val moviesData = ResponseMapper.getMovies(moviesResponse, testConfiguration)
        assertEquals(7, moviesData.page)
        assertEquals(1000, moviesData.totalPages)
        val movie = moviesData.movies[0]
        assertEquals("This is a test overview", movie.description)
        assertEquals(4, movie.id)
        assertEquals("Test Title", movie.title)
        assertEquals("https://www.themoviedb.org/3/w154/abc.jpg", movie.posterUrl)
        assertEquals("https://www.themoviedb.org/3/w780/backdrop.jpg", movie.backdropUrl)
    }
}