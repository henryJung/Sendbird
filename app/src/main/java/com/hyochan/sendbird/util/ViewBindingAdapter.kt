package com.hyochan.sendbird.util

import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide

@BindingAdapter("onEditorActionListener")
fun bindOnEditorActionListener(
    editText: EditText,
    editorActionListener: TextView.OnEditorActionListener
) {
    editText.setOnEditorActionListener(editorActionListener)
}

@BindingAdapter("image")
fun bindImage(view: ImageView, url: String) {
    Glide.with(view.context)
        .load(url)
        .thumbnail(0.2f)
        .into(view)
}