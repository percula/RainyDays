package dev.percula.rainydays.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.sdsmdg.harjot.vectormaster.VectorMasterView
import dev.percula.rainydays.R
import dev.percula.rainydays.model.RainData

class RainDataAdapter(val metricUnits: LiveData<Boolean>) : PagedListAdapter<RainData, RainDataAdapter.RainDataViewHolder>(object : DiffUtil.ItemCallback<RainData>() {

    override fun areContentsTheSame(oldItem: RainData, newItem: RainData): Boolean {
        return oldItem == newItem
    }

    override fun areItemsTheSame(oldItem: RainData, newItem: RainData): Boolean {
        return oldItem.station == newItem.station && oldItem.date == newItem.date
    }
}) {

    private val maximumRainGaugeValue: Double = 3.0
    private val minTrimPath = 0.05f // To get rounded start

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)

        (recyclerView as? LifecycleOwner)?.let {
            metricUnits.observe(it, Observer {
                notifyDataSetChanged()
            })
        }
    }

    override fun onBindViewHolder(holder: RainDataViewHolder, position: Int) {
        val obj = getItem(position)
        obj?.let {
            holder.bind(it)

            it.precipitation?.let {
                val pathName = holder.itemView.context?.getString(R.string.rain_gauge)
                val path = holder.rainGauge.getPathModelByName(pathName)
                val trimPathEnd = ((Math.min(it, maximumRainGaugeValue)*(1f-minTrimPath))/maximumRainGaugeValue).toFloat() + minTrimPath

                path?.trimPathStart = 0.05f
                path?.trimPathEnd = trimPathEnd
                holder.rainGauge.update()
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RainDataViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = DataBindingUtil.inflate<ViewDataBinding>(
            layoutInflater, R.layout.item_rain_data, parent, false)
        return RainDataViewHolder(binding)
    }

    class RainDataViewHolder(binding: ViewDataBinding) : BaseViewHolder<RainData>(binding) {
        val rainGauge = itemView.findViewById<VectorMasterView>(R.id.rain_gauge)
    }
}