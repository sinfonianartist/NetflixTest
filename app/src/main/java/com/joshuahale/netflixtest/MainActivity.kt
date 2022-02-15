package com.joshuahale.netflixtest

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.joshuahale.netflixtest.databinding.ActivityMainBinding
import com.joshuahale.netflixtest.ui.movies.MoviesListFragment
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportFragmentManager
            .beginTransaction()
            .replace(binding.content.id, MoviesListFragment())
            .commit()
    }
}