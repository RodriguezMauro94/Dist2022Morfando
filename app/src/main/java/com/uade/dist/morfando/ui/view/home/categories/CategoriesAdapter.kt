package com.uade.dist.morfando.ui.view.home.categories

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import com.squareup.picasso.Picasso
import com.uade.dist.morfando.R


val categories = listOf(
    Category(
        "meat",
        R.string.cooking_type_meat,
        "https://i.imgur.com/GB7lTPH.jpeg"
    ),
    Category(
        "chicken",
        R.string.cooking_type_chicken,
        "https://i.imgur.com/GB7lTPH.jpeg"
    ),
    Category(
        "pasta",
        R.string.cooking_type_pasta,
        "https://i.imgur.com/GB7lTPH.jpeg"
    ),
    Category(
        "pizza",
        R.string.cooking_type_pizza,
        "https://i.imgur.com/GB7lTPH.jpeg"
    ),
    Category(
        "salad",
        R.string.cooking_type_salad,
        "https://i.imgur.com/GB7lTPH.jpeg"
    ),
    Category(
        "sushi",
        R.string.cooking_type_sushi,
        "https://i.imgur.com/GB7lTPH.jpeg"
    ),
    Category(
        "empanada",
        R.string.cooking_type_empanada,
        "https://i.imgur.com/GB7lTPH.jpeg"
    ),
    Category(
        "taco",
        R.string.cooking_type_tacos,
        "https://i.imgur.com/GB7lTPH.jpeg"
    ),
)

data class Category(
    val id: String,
    val text: Int,
    val image: String
)

class CategoriesAdapter(val context: Context): BaseAdapter() {
    private var layoutInflater: LayoutInflater? = null
    private lateinit var itemTitle: TextView
    private lateinit var itemBackground: ImageView

    override fun getCount() = categories.size
    override fun getItem(position: Int) = categories[position]
    override fun getItemId(position: Int): Long = getItem(position).id.hashCode().toLong()

    override fun getView(position: Int, convertView: View?, viewGroup: ViewGroup?): View {
        var convertView = convertView
        if (layoutInflater == null) {
            layoutInflater =
                context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        }

        if (convertView == null) {
            convertView = layoutInflater!!.inflate(R.layout.item_categories, null)
        }

        itemBackground = convertView!!.findViewById(R.id.item_categories_background)
        itemTitle = convertView.findViewById(R.id.item_categories_title)

        val category = categories[position]
        Picasso.get()
            .load(category.image)
            .placeholder(R.drawable.logo_morfando)
            .into(itemBackground)
        itemTitle.setText(category.text)
        return convertView
    }

}