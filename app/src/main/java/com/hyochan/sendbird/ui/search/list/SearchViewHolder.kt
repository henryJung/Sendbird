package com.hyochan.sendbird.ui.search.list

import android.app.Activity
import androidx.core.app.ActivityOptionsCompat
import androidx.core.view.ViewCompat
import androidx.recyclerview.widget.RecyclerView
import com.hyochan.sendbird.data.Books
import com.hyochan.sendbird.databinding.ItemBookBinding
import com.hyochan.sendbird.ui.detail.DetailActivity

class SearchViewHolder(
    private val binding: ItemBookBinding,
) : RecyclerView.ViewHolder(binding.root) {


    fun bind(book: Books) {
        binding.item = book
        binding.executePendingBindings()

        itemView.setOnClickListener {
            val options = ActivityOptionsCompat.makeSceneTransitionAnimation(
                it.context as Activity, binding.imageView, ViewCompat.getTransitionName(binding.imageView)!!
            )
            DetailActivity.start(it.context, book, options)
        }
    }
}

