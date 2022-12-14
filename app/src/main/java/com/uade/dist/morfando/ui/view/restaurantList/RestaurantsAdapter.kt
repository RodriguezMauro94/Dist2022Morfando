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
import com.uade.dist.morfando.core.toPriceRange
import com.uade.dist.morfando.data.model.RestaurantModel
import com.uade.dist.morfando.ui.view.home.categories.categories

class RestaurantsAdapter(): RecyclerView.Adapter<RestaurantsAdapter.RestaurantsViewHolderBase>() {
    private lateinit var clickListener: ItemClickListener
    private var viewMode: RestaurantViewMode = RestaurantViewMode.MINIFIED
    private var restaurants = mutableListOf<RestaurantModel>()

    constructor(clickListener: ItemClickListener, viewMode: RestaurantViewMode): this() {
        this.clickListener = clickListener
        this.viewMode = viewMode
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RestaurantsViewHolderBase {
        val inflater = LayoutInflater.from(parent.context)
        return when (viewMode) {
            RestaurantViewMode.VERTICAL -> {
                val view = inflater.inflate(R.layout.item_restaurant_vertical, parent, false)
                RestaurantsViewHolder(view, restaurants, clickListener)
            }
            RestaurantViewMode.HORIZONTAL -> {
                val view = inflater.inflate(R.layout.item_restaurant_horizontal, parent, false)
                RestaurantsViewHolder(view, restaurants, clickListener)
            }
            RestaurantViewMode.MINIFIED -> {
                val view = inflater.inflate(R.layout.item_restaurant_minified, parent, false)
                RestaurantsViewHolderBase(view, restaurants, clickListener)
            }
        }
    }

    override fun onBindViewHolder(holder: RestaurantsViewHolderBase, position: Int) {
        restaurants[position].apply {
            holder.restaurantName.text = name

            if (holder is RestaurantsViewHolder) {
                val category = categories.find { category ->
                    category.id == cookingType
                }

                holder.restaurantType.setText(category!!.text)
                holder.restaurantPrice.text = priceRange.toPriceRange()
                holder.restaurantRating.rating = rating.toFloat()
                holder.restaurantRatingValue.text = rating.toString()
                holder.restaurantNeighborhood.text = neighborhood
                images?.get(0)?.apply {
                    Picasso.get()
                        .load(this)
                        .placeholder(R.drawable.logo_morfando)
                        .into(holder.restaurantImage)
                }
            }
        }
    }

    override fun getItemCount(): Int = restaurants.size

    fun getRestaurants(): List<RestaurantModel> = this.restaurants

    fun setRestaurants(restaurants: List<RestaurantModel>) {
        this.restaurants = restaurants.toMutableList()
        notifyDataSetChanged()
    }

    class RestaurantsViewHolder(restaurantView: View, private val restaurants: List<RestaurantModel>, private val clickListener: ItemClickListener) : RestaurantsViewHolderBase(restaurantView, restaurants, clickListener) {
        val restaurantImage: ImageView = view.findViewById(R.id.restaurant_image)
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

    open class RestaurantsViewHolderBase(restaurantView: View, private val restaurants: List<RestaurantModel>, private val clickListener: ItemClickListener) : RecyclerView.ViewHolder(restaurantView), View.OnClickListener {
        val view: View = restaurantView
        val restaurantName: TextView = view.findViewById(R.id.restaurant_name)

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