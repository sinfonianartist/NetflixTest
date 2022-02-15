package com.joshuahale.netflixtest.ui.movies

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.joshuahale.netflixtest.databinding.MoviePosterBinding
import com.joshuahale.netflixtest.model.movies.Movie
import com.squareup.picasso.Picasso

class MoviesAdapter(private val onItemClicked: (Movie) -> Unit
) : ListAdapter<Movie, MoviesAdapter.MoviesViewHolder>(MovieDiffCallback()) {

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
                .into(binding.posterView)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        MoviesViewHolder(
            MoviePosterBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        ) {
            onItemClicked(currentList[it])
        }


    override fun onBindViewHolder(holder: MoviesViewHolder, position: Int) {
        holder.bind(currentList[position])
    }

    override fun getItemCount() = currentList.size

    fun addMovies(newMovies: List<Movie>, clearList: Boolean) {
        submitList(newMovies)
    }
}