package com.example.muvi_app.utils

import androidx.recyclerview.widget.DiffUtil
import com.example.muvi_app.data.response.Movie

class MovieDiffUtilCallback(private val oldItems: List<Movie>, private val newItems: List<Movie>) :
    DiffUtil.Callback() {

    override fun getOldListSize(): Int = oldItems.size

    override fun getNewListSize(): Int = newItems.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
        oldItems[oldItemPosition].id == newItems[newItemPosition].id

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
        oldItems[oldItemPosition].hashCode() == newItems[newItemPosition].hashCode()
}