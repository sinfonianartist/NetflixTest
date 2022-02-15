package com.joshuahale.netflixtest.ui.movies

import androidx.recyclerview.widget.DiffUtil
import com.joshuahale.netflixtest.model.movies.Movie

class MovieDiffCallback : DiffUtil.ItemCallback<Movie>() {

    override fun areItemsTheSame(oldItem: Movie, newItem: Movie): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Movie, newItem: Movie): Boolean {
        return oldItem == newItem
    }
}