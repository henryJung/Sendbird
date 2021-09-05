package com.hyochan.sendbird.ui.search

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.core.app.ActivityOptionsCompat
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import com.hyochan.sendbird.R
import com.hyochan.sendbird.databinding.ActivitySearchBinding
import com.hyochan.sendbird.repository.BookRepositoryImpl
import com.hyochan.sendbird.ui.search.list.FooterStateAdapter
import com.hyochan.sendbird.ui.search.list.SearchAdapter
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import java.util.*


class SearchActivity : AppCompatActivity() {

    private val viewModel: SearchViewModel by viewModels {
        SearchViewModelFactory(
            BookRepositoryImpl()
        )
    }

    private lateinit var binding: ActivitySearchBinding
    private val adapter by lazy {
        SearchAdapter()
    }
    private var searchString: String? = null

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(KEY_STRING_SEARCH, searchString)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        searchString = savedInstanceState.getString(KEY_STRING_SEARCH)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_search)
        binding.apply {
            activity = this@SearchActivity
        }
        setUpViews()
        observeViewModel()
        searchString = if (savedInstanceState != null) {
            savedInstanceState.getString(KEY_STRING_SEARCH)
        } else {
            intent.extras?.getString(KEY_STRING_SEARCH)
        }
    }

    override fun finish() {
        setResult(Activity.RESULT_OK, Intent().putExtra(KEY_STRING_SEARCH, searchString))
        super.finish()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.search, menu)

        (menu.findItem(R.id.action_search).actionView as SearchView).apply {
            maxWidth = Integer.MAX_VALUE
            setIconifiedByDefault(false)
            setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean {
                    if (!query.isNullOrEmpty()) {
                        clearFocus()
                        sendIntent(SearchIntent.Search(query))
                        searchString = query
                    }
                    return true
                }

                override fun onQueryTextChange(newText: String?): Boolean {
                    return true
                }
            })
            setQuery(searchString, true)
        }
        return true
    }

    private fun sendIntent(intent: SearchIntent) {
        lifecycleScope.launchWhenStarted {
            viewModel.intent.send(intent)
        }
    }

    private fun setUpViews() {
        supportActionBar?.apply {
            setDisplayShowTitleEnabled(false)
        }

        binding.recyclerView.adapter =
            adapter.withLoadStateFooter(FooterStateAdapter { adapter.retry() })
        adapter.addLoadStateListener { loadState ->
            val isListEmpty = loadState.refresh is LoadState.NotLoading && adapter.itemCount == 0
            binding.emptyLayout.isVisible = isListEmpty
            binding.recyclerView.isVisible =
                loadState.refresh is LoadState.NotLoading && adapter.itemCount != 0
            binding.progressBar.isVisible = loadState.refresh is LoadState.Loading

            val errorState = loadState.refresh as? LoadState.Error
                ?: loadState.append as? LoadState.Error
                ?: loadState.prepend as? LoadState.Error
            errorState?.let {
                Toast.makeText(this, it.error.toString(), Toast.LENGTH_LONG).show()
            }
            binding.retryButton.isVisible = errorState is LoadState.Error
        }

        binding.retryButton.setOnClickListener { adapter.retry() }
        binding.swipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary)
        binding.swipeRefreshLayout.setOnRefreshListener {
            adapter.refresh()
            binding.swipeRefreshLayout.isRefreshing = false
        }
    }

    private fun observeViewModel() {
        lifecycleScope.launchWhenStarted {
            viewModel.state.collect {
                when (it) {
                    is SearchState.Idle -> {
                    }
                    is SearchState.ListSuccess -> {
                        lifecycleScope.launchWhenStarted {
                            it.result.collectLatest { data ->
                                adapter.submitData(data)
                            }
                        }
                    }
                }
            }
        }
    }

    companion object {
        const val KEY_STRING_SEARCH = "searchText"

        fun startForResult(
            context: Context,
            resultLauncher: ActivityResultLauncher<Intent>,
            searchText: String,
        ) {
            Intent(context, SearchActivity::class.java).apply {
                putExtra(KEY_STRING_SEARCH, searchText)
                resultLauncher.launch(this)
            }
        }
    }
}