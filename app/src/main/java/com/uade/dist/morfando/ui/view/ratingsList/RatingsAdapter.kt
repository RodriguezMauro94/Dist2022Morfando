package com.uade.dist.morfando.ui.view.ratingsList

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import com.uade.dist.morfando.R
import com.uade.dist.morfando.data.model.RatingModel

class RatingsAdapter: RecyclerView.Adapter<RatingsAdapter.RatingsViewHolder>() {
    private var ratings = mutableListOf<RatingModel>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RatingsViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.item_rating, parent, false)
        return RatingsViewHolder(view)
    }

    override fun onBindViewHolder(holder: RatingsViewHolder, position: Int) {
        ratings[position].apply {
            holder.ratingName.text = user
            holder.ratingTitle.text = title
            holder.ratingDescription.text = description
            holder.rating.rating = rating.toFloat()
            holder.ratingValue.text = rating.toString()
            Picasso.get()
                .load(userImage)
                .placeholder(R.drawable.logo_morfando)
                .into(holder.ratingImage)
        }
    }

    override fun getItemCount(): Int = ratings.size

    fun setRatings(ratings: List<RatingModel>) {
        this.ratings = ratings.toMutableList()
        notifyDataSetChanged()
    }

    open class RatingsViewHolder(ratingView: View) : RecyclerView.ViewHolder(ratingView) {
        val view: View = ratingView
        val ratingName: TextView = view.findViewById(R.id.rating_name)
        val rating: RatingBar = view.findViewById(R.id.rating)
        val ratingValue: TextView = view.findViewById(R.id.rating_value)
        val ratingTitle: TextView = view.findViewById(R.id.rating_title)
        val ratingDescription: TextView = view.findViewById(R.id.rating_description)
        val ratingImage: ImageView = view.findViewById(R.id.rating_image)
    }
}