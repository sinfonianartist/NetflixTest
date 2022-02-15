package com.joshuahale.netflixtest.ui.movies

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.joshuahale.netflixtest.databinding.MoviePosterBinding
import com.joshuahale.netflixtest.model.movies.Movie
import com.squareup.picasso.Picasso

class TrendingMoviesAdapter : RecyclerView.Adapter<TrendingMoviesAdapter.MoviesViewHolder>() {

    private val movies = ArrayList<Movie>()

    class MoviesViewHolder(private val binding: MoviePosterBinding
        ): RecyclerView.ViewHolder(binding.root) {
        fun bind(movie: Movie) {
            Picasso
                .get()
                .load(movie.posterUrl)
                .into(binding.posterView)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        MoviesViewHolder(
            MoviePosterBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )


    override fun onBindViewHolder(holder: MoviesViewHolder, position: Int) {
        holder.bind(movies[position])
    }

    override fun getItemCount() = movies.size

    fun addMovies(newMovies: List<Movie>) {
        movies.addAll(newMovies)
        notifyDataSetChanged()
    }
}