package dev.percula.rainydays.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import dev.percula.rainydays.R
import dev.percula.rainydays.viewmodel.RainDataViewModel
import dev.percula.rainydays.viewmodel.RainDataViewModelFactory

class DetailFragment: Fragment() {

    val args by navArgs<DetailFragmentArgs>()

    val viewModel by lazy {
        val factory = RainDataViewModelFactory(
            application = activity!!.application,
            location = args.location
        )
        ViewModelProviders.of(this, factory).get(RainDataViewModel::class.java)
    }

    private val adapter: RainDataAdapter = RainDataAdapter()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_location_list, container, false)

        val recyclerView = view.findViewById<RecyclerView>(R.id.recyclerview)
        recyclerView.layoutManager = LinearLayoutManager(view.context)
        recyclerView.adapter = adapter

        return view
    }
}