package com.uade.dist.morfando.ui.view.restaurantList

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import com.uade.dist.morfando.R
import com.uade.dist.morfando.data.model.RestaurantModel

class RestaurantsAdapter(): RecyclerView.Adapter<RestaurantsAdapter.RestaurantsViewHolder>() {
    private lateinit var clickListener: ItemClickListener
    private var restaurants = mutableListOf<RestaurantModel>()

    constructor(clickListener: ItemClickListener): this() {
        this.clickListener = clickListener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RestaurantsViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.item_restaurant_vertical, parent, false)
        return RestaurantsViewHolder(view, restaurants, clickListener)
    }

    override fun onBindViewHolder(holder: RestaurantsViewHolder, position: Int) {
        restaurants[position].apply {
            holder.restaurantName.text = name
            holder.restaurantType.text = speciality
            holder.restaurantPrice.text = price
            holder.restaurantRating.rating = rating.toFloat()
            holder.restaurantRatingValue.text = rating.toString()
            holder.restaurantNeighborhood.text = neighborhood
            Picasso.get()
                .load(image)
                .placeholder(R.drawable.logo_morfando)
                .into(holder.restaurantImage)
        }
    }

    override fun getItemCount(): Int = restaurants.size

    fun setRestaurants(restaurants: List<RestaurantModel>) {
        this.restaurants = restaurants.toMutableList()
        notifyDataSetChanged()
    }

    class RestaurantsViewHolder(restaurantView: View, private val restaurants: List<RestaurantModel>, private val clickListener: ItemClickListener) : RecyclerView.ViewHolder(restaurantView), View.OnClickListener {
        val view: View = restaurantView
        val restaurantImage: ImageView = view.findViewById(R.id.restaurant_image)
        val restaurantName: TextView = view.findViewById(R.id.restaurant_name)
        val restaurantRating: RatingBar = view.findViewById(R.id.restaurant_rating)
        val restaurantRatingValue: TextView = view.findViewById(R.id.restaurant_rating_value)
        val restaurantPrice: TextView = view.findViewById(R.id.restaurant_price)
        val restaurantType: TextView = view.findViewById(R.id.restaurant_type)
        var restaurantNeighborhood: TextView = view.findViewById(R.id.restaurant_neighborhood)

        init {
            view.setOnClickListener(this)
        }

        override fun onClick(view: View) {
            clickListener.onItemClick(restaurants[adapterPosition])
        }
    }

    interface ItemClickListener {
        fun onItemClick(restaurant: RestaurantModel)
    }
}