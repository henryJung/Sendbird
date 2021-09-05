package com.hyochan.sendbird.ui.search.list

import androidx.core.view.isVisible
import androidx.paging.LoadState
import androidx.recyclerview.widget.RecyclerView
import com.hyochan.sendbird.databinding.ItemFooterBinding

class FooterStateViewHolder(
    private val binding: ItemFooterBinding,
    retry: () -> Unit
) : RecyclerView.ViewHolder(binding.root) {

    init {
        binding.retryButton.setOnClickListener {
            retry()
        }
    }

    fun bind(loadState: LoadState) {
        binding.progressBar.isVisible = loadState is LoadState.Loading
        binding.retryButton.isVisible = loadState is LoadState.Error
        binding.errorMsg.isVisible =
            !(loadState as? LoadState.Error)?.error?.message.isNullOrBlank()
        binding.errorMsg.text = (loadState as? LoadState.Error)?.error?.message
    }
}