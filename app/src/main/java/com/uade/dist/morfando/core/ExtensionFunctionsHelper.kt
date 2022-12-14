package com.uade.dist.morfando.core

import android.content.Context
import android.view.View
import android.widget.Toast
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.uade.dist.morfando.data.model.SearchFilterOptionsModel

data class ChipSearchOptionsModel(
    val text: Int,
    val option: SearchFilterOptionsModel
)

fun ChipGroup.addChips(context: Context, chips: Map<String, ChipSearchOptionsModel>, callback: (key: String) -> Unit) {
    chips.forEach {
        this.addChip(context, it.key, context.getString(it.value.text), callback)
    }
}

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

fun ChipGroup.addCheckedChips(context: Context, chips: Map<String, Int>) {
    chips.forEach {
        this.addCheckedChip(context, it.key, context.getString(it.value))
    }
}

fun ChipGroup.addCheckedChip(context: Context, key: String, label: String) {
    Chip(context).apply {
        id = View.generateViewId()
        text = label
        isClickable = true
        isCheckedIconVisible = true
        isCheckable = true
        tag = key
        addView(this)
    }
}

fun String.showToast(context: Context, duration: Int = Toast.LENGTH_SHORT) {
    Toast.makeText(context, this, duration).show()
}

fun Boolean.getVisibility(): Int {
    return if (this) View.VISIBLE else View.GONE
}

fun View.setVisibility(condition: Boolean) {
    this.visibility = if (condition) View.VISIBLE else View.GONE
}