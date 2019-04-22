package com.example.app.bank.extention

import android.widget.ImageView
import com.bumptech.glide.Glide

fun ImageView.loadUrl(url: String?) {
    url?.also {
        Glide.with(context)
                .load(url)
                .into(this)
    }
}
