package com.example.muvi_app.ui.main

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.muvi_app.data.response.MovieDetailResponse
import com.example.muvi_app.databinding.SlideItemContainerBinding
import com.bumptech.glide.Glide
import com.example.muvi_app.data.adapter.SearchAdapter
import com.example.muvi_app.data.response.Movie
import com.example.muvi_app.utils.MovieDiffUtilCallback

class MoviesAdapter :  RecyclerView.Adapter<MoviesAdapter.MovieViewHolder>() {

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

    class DiffCallback : DiffUtil.ItemCallback<Movie>() {
        override fun areItemsTheSame(oldItem: Movie, newItem: Movie): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Movie, newItem: Movie): Boolean {
            return oldItem == newItem
        }
    }

    interface OnItemClickCallback {
        fun onItemClicked(data: Movie)
    }
}
