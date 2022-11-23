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
        "https://res.cloudinary.com/de7zrcqyz/image/upload/c_pad,b_auto:predominant,fl_preserve_transparency/v1669240032/meat_ikzonc.jpg"
    ),
    Category(
        "chicken",
        R.string.cooking_type_chicken,
        "https://res.cloudinary.com/de7zrcqyz/image/upload/c_pad,b_auto:predominant,fl_preserve_transparency/v1669240032/chiken_avszxx.jpg"
    ),
    Category(
        "pasta",
        R.string.cooking_type_pasta,
        "https://res.cloudinary.com/de7zrcqyz/image/upload/c_pad,b_auto:predominant,fl_preserve_transparency/v1669240033/pasta_tz6vvo.jpg"
    ),
    Category(
        "pizza",
        R.string.cooking_type_pizza,
        "https://res.cloudinary.com/de7zrcqyz/image/upload/c_pad,b_auto:predominant,fl_preserve_transparency/v1669240032/pizza_em2kcw.jpg"
    ),
    Category(
        "salad",
        R.string.cooking_type_salad,
        "https://res.cloudinary.com/de7zrcqyz/image/upload/c_pad,b_auto:predominant,fl_preserve_transparency/v1669240033/salad_jodw9c.jpg"
    ),
    Category(
        "sushi",
        R.string.cooking_type_sushi,
        "https://res.cloudinary.com/de7zrcqyz/image/upload/c_pad,b_auto:predominant,fl_preserve_transparency/v1669240033/sushi_hufpwy.jpg"
    ),
    Category(
        "empanada",
        R.string.cooking_type_empanada,
        "https://res.cloudinary.com/de7zrcqyz/image/upload/c_pad,b_auto:predominant,fl_preserve_transparency/v1669240032/empanadas_r16cbl.jpg"
    ),
    Category(
        "taco",
        R.string.cooking_type_tacos,
        "https://res.cloudinary.com/de7zrcqyz/image/upload/c_pad,b_auto:predominant,fl_preserve_transparency/v1669240033/tacos_ffxoej.jpg"
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