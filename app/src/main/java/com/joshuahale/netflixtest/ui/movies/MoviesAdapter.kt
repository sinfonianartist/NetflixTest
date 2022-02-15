package com.joshuahale.netflixtest.ui.movies

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.joshuahale.netflixtest.R
import com.joshuahale.netflixtest.databinding.MoviePosterBinding
import com.joshuahale.netflixtest.model.movies.Movie
import com.squareup.picasso.Picasso

class MoviesAdapter(private val onItemClicked: (Movie) -> Unit
) : RecyclerView.Adapter<MoviesAdapter.MoviesViewHolder>() {

    val movies = mutableListOf<Movie>()

    class MoviesViewHolder(
        private val binding: MoviePosterBinding,
        onItemClicked: (Int) -> Unit
        ): RecyclerView.ViewHolder(binding.root) {

        init {
            itemView.setOnClickListener { onItemClicked(adapterPosition) }
        }

        fun bind(movie: Movie) {
            Picasso
                .get()
                .load(movie.posterUrl)
                .placeholder(R.drawable.ic_placeholder_poster)
                .into(binding.posterView)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        MoviesViewHolder(
            MoviePosterBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        ) {
            onItemClicked(movies[it])
        }


    override fun onBindViewHolder(holder: MoviesViewHolder, position: Int) {
        holder.bind(movies[position])
    }

    override fun getItemCount() = movies.size

    fun addMovies(newMovies: List<Movie>, clearList: Boolean) {
        if (clearList) {
            notifyItemRangeRemoved(0, movies.size)
            movies.clear()
            movies.addAll(newMovies)
            notifyItemInserted(movies.size)
        } else {
            movies.addAll(newMovies)
            notifyItemInserted(movies.size)
        }
    }
}