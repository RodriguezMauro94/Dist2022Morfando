package com.uade.dist.morfando.ui.view.gallery

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import com.squareup.picasso.Picasso
import com.uade.dist.morfando.R

class GalleryAdapter(val context: Context): BaseAdapter() {
    private var layoutInflater: LayoutInflater? = null
    var images: List<String> = emptyList()

    override fun getCount() = images.size
    override fun getItem(position: Int) = images[position]
    override fun getItemId(position: Int): Long = position.toLong()

    override fun getView(position: Int, convertView: View?, viewGroup: ViewGroup?): View {
        var view: ImageView? = convertView as ImageView?
        if (view == null) {
            view = ImageView(context)
            view.scaleType = ImageView.ScaleType.CENTER_CROP
        }

        val url = getItem(position)

        Picasso.get()
            .load(url)
            .placeholder(R.drawable.logo_morfando)
            .fit()
            .tag(context)
            .into(view)

        return view
    }
}