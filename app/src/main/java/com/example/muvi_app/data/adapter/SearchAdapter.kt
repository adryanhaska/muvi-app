package com.example.muvi_app.data.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.muvi_app.data.response.Movie
import com.example.muvi_app.data.response.UserProfile

import com.example.muvi_app.data.utils.GenericDiffUtilCallback
import com.example.muvi_app.databinding.ItemProfileResultBinding
import com.example.muvi_app.databinding.ItemSearchLayoutBinding


class SearchAdapter(private val onItemClickCallback: OnItemClickCallback) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val listItems = mutableListOf<Any>()

    companion object {
        private const val TYPE_MOVIE = 0
        private const val TYPE_PERSON = 1
    }

    fun setData(data: List<Any>) {
        val diffCallback = GenericDiffUtilCallback(listItems, data)
        val diffResult = DiffUtil.calculateDiff(diffCallback)

        listItems.clear()
        listItems.addAll(data)

        diffResult.dispatchUpdatesTo(this)
    }

    override fun getItemViewType(position: Int): Int {
        return when (listItems[position]) {
            is Movie -> TYPE_MOVIE
            is UserProfile -> TYPE_PERSON
            else -> throw IllegalArgumentException("Invalid type of data $position")
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            TYPE_MOVIE -> {
                val binding = ItemSearchLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                MovieViewHolder(binding)
            }
            TYPE_PERSON -> {
                val binding = ItemProfileResultBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                PersonViewHolder(binding)
            }
            else -> throw IllegalArgumentException("Invalid view type")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is MovieViewHolder -> holder.bind(listItems[position] as Movie)
            is PersonViewHolder -> holder.bind(listItems[position] as UserProfile)
        }
    }

    override fun getItemCount(): Int = listItems.size

    inner class MovieViewHolder(private val binding: ItemSearchLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            itemView.setOnClickListener {
                onItemClickCallback.onItemClicked(listItems[adapterPosition])
            }
        }

        fun bind(movie: Movie) {
            with(binding) {
                textTitle.text = movie.title
                textCast.text = movie.releaseDate
                val genreNames = movie.genreIds?.toGenreNames()?.joinToString(", ")
                genre.text = genreNames
                Glide.with(itemView.context)
                    .load("https://image.tmdb.org/t/p/original${movie.posterPath}")
                    .into(imagePoster)
            }
        }
    }

    fun List<Int?>.toGenreNames(): List<String> {
        return this.mapNotNull { id ->
            when (id) {
                28 -> "Action"
                12 -> "Adventure"
                16 -> "Animation"
                35 -> "Comedy"
                80 -> "Crime"
                99 -> "Documentary"
                18 -> "Drama"
                10751 -> "Family"
                14 -> "Fantasy"
                36 -> "History"
                27 -> "Horror"
                10402 -> "Music"
                9648 -> "Mystery"
                10749 -> "Romance"
                878 -> "Science Fiction"
                10770 -> "TV Movie"
                53 -> "Thriller"
                10752 -> "War"
                37 -> "Western"
                else -> null
            }
        }
    }

    inner class PersonViewHolder(private val binding: ItemProfileResultBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            itemView.setOnClickListener {
                onItemClickCallback.onItemClicked(listItems[adapterPosition])
            }
        }

        fun bind(profile: UserProfile) {
            with(binding) {
                textName.text = profile.name
                textUsername.text = profile.username
            }
        }
    }

    interface OnItemClickCallback {
        fun onItemClicked(data: Any)
    }
}