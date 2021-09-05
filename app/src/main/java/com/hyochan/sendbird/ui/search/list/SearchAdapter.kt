package com.hyochan.sendbird.ui.search.list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import com.hyochan.sendbird.data.Books
import com.hyochan.sendbird.databinding.ItemBookBinding
import com.hyochan.sendbird.ui.search.SearchActivity

class SearchAdapter : PagingDataAdapter<Books, SearchViewHolder>(BOOK_COMPARATOR) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchViewHolder {
        return SearchViewHolder(
            ItemBookBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: SearchViewHolder, position: Int) {
        val item = getItem(position)
        if (item != null) {
            holder.bind(item)
        }
    }

    companion object {
        private val BOOK_COMPARATOR = object : DiffUtil.ItemCallback<Books>() {
            override fun areItemsTheSame(oldItem: Books, newItem: Books): Boolean =
                oldItem.isbn13 == newItem.isbn13

            override fun areContentsTheSame(oldItem: Books, newItem: Books): Boolean =
                oldItem == newItem
        }
    }
}