package com.hyochan.sendbird.ui.main

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityOptionsCompat
import androidx.core.view.ViewCompat
import androidx.databinding.DataBindingUtil
import com.hyochan.sendbird.R
import com.hyochan.sendbird.databinding.ActivityMainBinding
import com.hyochan.sendbird.ui.search.SearchActivity

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private val resultLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val searText = result.data?.getStringExtra(SearchActivity.KEY_STRING_SEARCH)
            if (!searText.isNullOrEmpty()) {
                binding.searchEditText.setText(searText)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView<ActivityMainBinding>(this, R.layout.activity_main)
            .apply {
                activity = this@MainActivity
            }
    }

    override fun onResume() {
        super.onResume()
        binding.searchEditText.run {
            if (!text.isNullOrEmpty()) {
                postDelayed({
                    showKeyboard()
                }, 100)
            }
        }
    }

    override fun onPause() {
        super.onPause()
        binding.searchEditText.closeKeyboard()
    }

    fun search() = TextView.OnEditorActionListener { v, actionId, _ ->
        if (actionId == EditorInfo.IME_ACTION_SEARCH) {
            if (v.text.toString().isNotEmpty()) {
                SearchActivity.startForResult(this, resultLauncher, v.text.toString())
            } else {
                Toast.makeText(this, "검색어를 입력하세요.", Toast.LENGTH_SHORT).show()
            }
            true
        } else {
            false
        }
    }

    private fun EditText.showKeyboard() {
        this.setSelection(this.length())
        this.requestFocus()
        val inputMethodManager: InputMethodManager =
            this.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.showSoftInput(this, InputMethodManager.SHOW_IMPLICIT)
    }

    private fun EditText.closeKeyboard() {
        this.clearFocus()
        val inputMethodManager: InputMethodManager =
            this.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(this.windowToken, 0)
    }
}