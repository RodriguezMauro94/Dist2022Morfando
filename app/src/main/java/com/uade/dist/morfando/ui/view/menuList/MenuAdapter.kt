package com.uade.dist.morfando.ui.view.menuList

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import com.uade.dist.morfando.R
import com.uade.dist.morfando.core.getVisibility

class MenuAdapter(): RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private lateinit var clickListener: ItemClickListener
    private var menuItems = mutableListOf<MenuItemList>()

    constructor(clickListener: ItemClickListener): this() {
        this.clickListener = clickListener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return if (MenuViewMode.HEADER.viewType == viewType) {
            val view: View = inflater.inflate(R.layout.item_plate_header, parent, false)
            MenuHeaderViewHolder(view)
        } else  {
            val view: View = inflater.inflate(R.layout.item_menu_plate, parent, false)
            MenuPlateViewHolder(view, menuItems, clickListener)
        }

    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is MenuHeaderViewHolder -> {
                val item = menuItems[position] as MenuHeaderItemList
                holder.plateHeader.text = item.title
            }
            is MenuPlateViewHolder -> {
                val item = menuItems[position] as PlateItemList
                holder.plateName.text =  item.name
                holder.platePrice.text = item.price
                Picasso.get()
                    .load(item.image)
                    .placeholder(R.drawable.logo_morfando)
                    .into(holder.plateImage)

                holder.plateGroup.visibility = (item.isCeliac || item.isVegan).getVisibility()
                holder.veganImage.visibility = item.isVegan.getVisibility()
                holder.veganText.visibility = item.isVegan.getVisibility()
                holder.celiacImage.visibility = item.isCeliac.getVisibility()
                holder.celiacText.visibility = item.isCeliac.getVisibility()
            }
        }
    }

    override fun getItemCount(): Int = menuItems.size

    override fun getItemViewType(position: Int): Int {
        return if (menuItems[position] is MenuHeaderItemList)
             MenuViewMode.HEADER.viewType
        else
            MenuViewMode.PLATE.viewType;
    }

    fun setMenu(restaurants: List<MenuItemList>) {
        this.menuItems = restaurants.toMutableList()
        notifyDataSetChanged()
    }

    class MenuHeaderViewHolder(restaurantView: View) : RecyclerView.ViewHolder(restaurantView) {
        val view: View = restaurantView
        val plateHeader: TextView = view.findViewById(R.id.plate_header)
    }

    class MenuPlateViewHolder(restaurantView: View,
                              private val menuItems: List<MenuItemList>,
                              private val clickListener: ItemClickListener
    ) : RecyclerView.ViewHolder(restaurantView), View.OnClickListener {
        val view: View = restaurantView
        val plateName: TextView = view.findViewById(R.id.plate_name)
        val plateImage: ImageView = view.findViewById(R.id.plate_image)
        val platePrice: TextView = view.findViewById(R.id.plate_price)
        val plateGroup: ViewGroup = view.findViewById(R.id.plate_group)
        val veganImage: ImageView = view.findViewById(R.id.vegan_image)
        val veganText: TextView = view.findViewById(R.id.vegan_text)
        val celiacImage: ImageView = view.findViewById(R.id.celiac_image)
        val celiacText: TextView = view.findViewById(R.id.celiac_text)

        init {
            view.setOnClickListener(this)
        }

        override fun onClick(view: View) {
            clickListener.onItemClick(menuItems[adapterPosition])
        }
    }

    interface ItemClickListener {
        fun onItemClick(item: MenuItemList)
    }
}