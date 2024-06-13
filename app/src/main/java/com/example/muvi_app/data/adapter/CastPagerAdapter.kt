package com.example.muvi_app.data.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.muvi_app.R
import com.example.muvi_app.data.response.CastItem
import com.example.muvi_app.databinding.ItemCastBinding

class CastPagerAdapter : RecyclerView.Adapter<CastPagerAdapter.CastViewHolder>() {

    private var castList: List<CastItem?> = emptyList()

    fun submitList(list: List<CastItem?>) {
        castList = list
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CastViewHolder {
        val binding = ItemCastBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CastViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CastViewHolder, position: Int) {
        val castItem = castList[position]
        holder.bind(castItem)
    }

    override fun getItemCount(): Int = castList.size

    inner class CastViewHolder(private val binding: ItemCastBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(castItem: CastItem?) {
            // Bind data to UI elements in cast item layout
            binding.castName.text = castItem?.name ?: "cast"
            val profilePath = castItem?.profilePath
            if (!profilePath.isNullOrEmpty()) {
                Glide.with(binding.root.context)
                    .load("https://image.tmdb.org/t/p/original$profilePath")
                    .circleCrop()
                    .into(binding.castImage)
            } else {
                // Set a placeholder or default image if profile path is empty or null
                binding.castImage.setImageResource(R.drawable.ic_person) // Change to your placeholder image
            }

        }
    }

}