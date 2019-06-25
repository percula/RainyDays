package dev.percula.rainydays.ui

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import dev.percula.rainydays.R

class RainDataViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

    val name = itemView.findViewById<TextView>(R.id.name)

}