package dev.percula.rainydays.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import dev.percula.rainydays.R
import dev.percula.rainydays.model.RainData

class RainDataAdapter() : PagedListAdapter<RainData, RainDataViewHolder>(object : DiffUtil.ItemCallback<RainData>() {

    override fun areContentsTheSame(oldItem: RainData, newItem: RainData): Boolean {
        return oldItem == newItem
    }

    override fun areItemsTheSame(oldItem: RainData, newItem: RainData): Boolean {
        return oldItem.station == newItem.station && oldItem.date == newItem.date
    }
}) {

    override fun onBindViewHolder(holder: RainDataViewHolder, position: Int) {
        holder.name.text = getItem(position)?.precipitation.toString()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RainDataViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_rain_data, parent, false)
        return RainDataViewHolder(view)
    }
}