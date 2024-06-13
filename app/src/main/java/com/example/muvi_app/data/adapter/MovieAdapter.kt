package com.example.muvi_app.data.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.muvi_app.data.response.Movie
import com.example.muvi_app.databinding.ItemSearchLayoutBinding

//class MovieAdapter : RecyclerView.Adapter<MovieAdapter.MovieViewHolder>() {

//    private var movies: List<Movie> = emptyList()
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
//        val binding = ItemSearchLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
//        return MovieViewHolder(binding)
//    }
//
//    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
//        holder.bind(movies[position])
//    }
//
//    override fun getItemCount(): Int = movies.size
//
//    fun submitList(movieList: List<Movie>) {
//        movies = movieList
//        notifyDataSetChanged()
//    }
//
//    class MovieViewHolder(private val binding: ItemSearchLayoutBinding) : RecyclerView.ViewHolder(binding.root) {
//        fun bind(movie: Movie) {
//            binding.textTitle.text = movie.title
//            binding.textCast.text = movie.release_date
//            Glide.with(binding.imagePoster.context)
//                .load("https://image.tmdb.org/t/p/original/${movie.poster_path}")
//                .into(binding.imagePoster)
//        }
//    }
//}
