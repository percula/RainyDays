package dev.percula.rainydays.ui

import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import dev.percula.rainydays.BR

/**
 * Note: I a copy/pasted this from Planter, one of my other projects. It was originally based on https://medium.com/androiddevelopers/android-data-binding-recyclerview-db7c40d9f0e4
 */
class BaseViewHolder<T>(private val binding: ViewDataBinding) : RecyclerView.ViewHolder(binding.root) {

    fun bind(obj: T) {
        binding.setVariable(BR.obj, obj)
        binding.executePendingBindings()
    }

}