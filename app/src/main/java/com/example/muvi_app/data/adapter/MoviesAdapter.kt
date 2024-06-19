package com.example.muvi_app.data.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.muvi_app.data.response.Movie
import com.example.muvi_app.databinding.SlideItemContainerBinding

class MoviesAdapter : RecyclerView.Adapter<MoviesAdapter.MovieViewHolder>() {

    private val moviesList = mutableListOf<Movie>()
    private lateinit var onItemClickCallback: OnItemClickCallback

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    fun submitList(movies: List<Movie>) {
        moviesList.clear()
        moviesList.addAll(movies)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        val binding = SlideItemContainerBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MovieViewHolder(binding)
    }

    override fun getItemCount(): Int = moviesList.size

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        holder.bind(moviesList[position])
    }

    inner class MovieViewHolder(private val binding: SlideItemContainerBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            itemView.setOnClickListener {
                onItemClickCallback.onItemClicked(moviesList[adapterPosition])
            }
        }

        fun bind(movie: Movie) {
            Glide.with(binding.moviePoster.context)
                .load("https://image.tmdb.org/t/p/original/${movie.posterPath}")
                .into(binding.moviePoster)
        }
    }

    interface OnItemClickCallback {
        fun onItemClicked(data: Movie)
    }
}
