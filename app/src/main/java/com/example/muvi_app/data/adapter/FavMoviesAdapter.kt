package com.example.muvi_app.data.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.muvi_app.data.response.Movie
import com.example.muvi_app.databinding.CardFavoriteItemBinding

class FavMoviesAdapter(
    private val movies: List<Movie>,
    private val onItemClick: (Movie) -> Unit
) : RecyclerView.Adapter<FavMoviesAdapter.MovieViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        val binding = CardFavoriteItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MovieViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        holder.bind(movies[position])
    }

    override fun getItemCount(): Int = movies.size

    inner class MovieViewHolder(private val binding: CardFavoriteItemBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(movie: Movie) {
            binding.favoriteItemTitle.text = movie.title
            binding.favoriteItemYear.text = movie.releaseDate
            Glide.with(binding.favoriteItemImage.context)
                .load("https://image.tmdb.org/t/p/original/${movie.posterPath}")
                .into(binding.favoriteItemImage)
            binding.root.setOnClickListener {
                onItemClick(movie)
            }
        }
    }

}
