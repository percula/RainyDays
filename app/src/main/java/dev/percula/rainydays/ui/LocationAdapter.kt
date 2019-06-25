package dev.percula.rainydays.ui

import dev.percula.rainydays.R
import dev.percula.rainydays.model.Location

class LocationAdapter: BaseAdapter<Location>() {

    val items: MutableList<Location> = mutableListOf()

    override fun getObjForPosition(position: Int): Location {
        return items[position]
    }

    override fun getLayoutIdForPosition(position: Int): Int {
        return R.layout.item_location
    }

    override fun getItemCount(): Int {
        return items.size
    }

}