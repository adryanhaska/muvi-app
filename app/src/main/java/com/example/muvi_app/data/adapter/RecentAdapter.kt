package com.example.muvi_app.data.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.muvi_app.data.response.Movie
import com.example.muvi_app.databinding.SlideItemContainerBinding
import com.example.muvi_app.utils.MovieDiffUtilCallback


class RecentAdapter : RecyclerView.Adapter<RecentAdapter.ListViewHolder>() {

    private val listUser = mutableListOf<Movie>()
    private lateinit var onItemClickCallback: OnItemClickCallback

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    fun setData(data: List<Movie>) {
        val diffCallback = MovieDiffUtilCallback(listUser, data)
        val diffResult = DiffUtil.calculateDiff(diffCallback)

        listUser.clear()
        listUser.addAll(data)

        diffResult.dispatchUpdatesTo(this)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val binding = SlideItemContainerBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        holder.bind(listUser[position])
    }

    override fun getItemCount(): Int = listUser.size

    inner class ListViewHolder(private val binding: SlideItemContainerBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            itemView.setOnClickListener {
                onItemClickCallback.onItemClicked(listUser[adapterPosition])
            }
        }

        fun bind(movie: Movie) {
            with(binding) {

                Glide.with(itemView.context)
                    .load("https://image.tmdb.org/t/p/original${movie.posterPath}")
                    .centerCrop()
                    .into(moviePoster)
            }
        }
    }

    interface OnItemClickCallback {
        fun onItemClicked(data: Movie)
    }
}