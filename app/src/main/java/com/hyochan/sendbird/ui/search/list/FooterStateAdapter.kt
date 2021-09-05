package com.hyochan.sendbird.ui.search.list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import com.hyochan.sendbird.R
import com.hyochan.sendbird.databinding.ItemFooterBinding

class FooterStateAdapter(private val retry: () -> Unit) :
    LoadStateAdapter<FooterStateViewHolder>() {

    override fun onBindViewHolder(holder: FooterStateViewHolder, loadState: LoadState) {
        holder.bind(loadState)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        loadState: LoadState
    ): FooterStateViewHolder {
        return FooterStateViewHolder(
            ItemFooterBinding.bind(
                LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_footer, parent, false)
            ), retry
        )
    }
}