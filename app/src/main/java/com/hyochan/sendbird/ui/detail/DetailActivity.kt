package com.hyochan.sendbird.ui.detail

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.transition.TransitionInflater
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityOptionsCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import com.hyochan.sendbird.R
import com.hyochan.sendbird.data.BookDetailResponse
import com.hyochan.sendbird.data.Books
import com.hyochan.sendbird.databinding.ActivityDetailBinding
import com.hyochan.sendbird.repository.BookRepositoryImpl
import kotlinx.coroutines.flow.collect

class DetailActivity : AppCompatActivity() {

    private val viewModel: DetailViewModel by viewModels {
        DetailViewModelFactory(
            BookRepositoryImpl()
        )
    }

    private lateinit var binding: ActivityDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_detail)
        setUpViews()
        observeViewModel()
        val book = intent.extras?.getSerializable(KEY_BOOK) as? Books
        if (book != null) {
            fun Books.convertBookDetail() = BookDetailResponse(
                title = title,
                subtitle = subtitle,
                isbn13 = isbn13,
                price = price,
                image = image,
                url = url
            )

            binding.item = book.convertBookDetail()
            sendIntent(DetailIntent.Load(book.isbn13))
        } else {
            Toast.makeText(this, "잘못 된 정보입니다.", Toast.LENGTH_SHORT).show()
            finish()
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            onBackPressed()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    private fun sendIntent(intent: DetailIntent) {
        lifecycleScope.launchWhenStarted {
            viewModel.intent.send(intent)
        }
    }

    private fun setUpViews() {
        window.enterTransition = TransitionInflater.from(this)
            .inflateTransition(R.transition.shared_element_transation)
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            title = "Book Detail"
        }
    }

    private fun observeViewModel() {
        lifecycleScope.launchWhenStarted {
            viewModel.state.collect {
                when (it) {
                    is DetailState.Idle -> {
                    }
                    is DetailState.Loading -> {
                        binding.progressBar.show()
                    }
                    is DetailState.LoadComplete -> {
                        binding.progressBar.hide()
                        binding.item = it.response
                    }
                    is DetailState.Error -> {
                        it.throwable.message?.let { message ->
                            Toast.makeText(this@DetailActivity, message, Toast.LENGTH_SHORT).show()
                            finish()
                        }
                    }
                }
            }
        }
    }

    companion object {
        private const val KEY_BOOK = "book"

        fun start(context: Context, book: Books, option: ActivityOptionsCompat) {
            Intent(context, DetailActivity::class.java).run {
                putExtra(KEY_BOOK, book)
                context.startActivity(this, option.toBundle())
            }
        }
    }
}