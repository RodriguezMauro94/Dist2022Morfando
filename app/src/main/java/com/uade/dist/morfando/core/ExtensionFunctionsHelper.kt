package com.uade.dist.morfando.core

import android.content.Context
import android.view.View
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup

fun ChipGroup.addChip(context: Context, key: String, label: String, callback: (key: String) -> Unit) {
    Chip(context).apply {
        id = View.generateViewId()
        text = label
        isClickable = true
        setOnClickListener { _ ->
            callback(key)
        }
        addView(this)
    }
}

