package com.uade.dist.morfando.ui.view.restaurantList

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.uade.dist.morfando.R
import com.uade.dist.morfando.data.model.RestaurantModel

class RestaurantsAdapter(): RecyclerView.Adapter<RestaurantsAdapter.ViewHolder>() {
    private lateinit var clickListener: ItemClickListener
    private lateinit var restaurants: List<RestaurantModel>
    private lateinit var inflater: LayoutInflater

    constructor(context: Context, restaurants: List<RestaurantModel>, clickListener: ItemClickListener): this() {
        this.inflater = LayoutInflater.from(context)
        this.restaurants = restaurants
        this.clickListener = clickListener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = inflater.inflate(R.layout.item_restaurant_vertical, parent, false)
        return ViewHolder(view, clickListener)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val restaurant = restaurants[position]
        holder.restaurantName.text = restaurant.name
        holder.restaurantType.text = restaurant.speciality
        holder.restaurantPrice.text = restaurant.price
        holder.restaurantRating.rating = restaurant.rating.toFloat()
        holder.restaurantRatingValue.text = restaurant.rating.toString()
        holder.restaurantNeighborhood.text = restaurant.neighborhood

        //TODO cargar imagen
    }

    override fun getItemCount(): Int = restaurants.size

    fun getRestaurant(position: Int) = restaurants[position]

    class ViewHolder(restaurantView: View, val clickListener: ItemClickListener) : RecyclerView.ViewHolder(restaurantView), View.OnClickListener {
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
            clickListener.onItemClick(view, adapterPosition)
        }
    }

    interface ItemClickListener {
        fun onItemClick(view: View, position: Int)
    }
}