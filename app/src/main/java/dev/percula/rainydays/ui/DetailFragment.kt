package dev.percula.rainydays.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.navArgs
import androidx.preference.PreferenceManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.perculacreative.preferenceutils.library.booleanLiveData
import dev.percula.rainydays.R
import dev.percula.rainydays.core.mutableLiveDataOf
import dev.percula.rainydays.viewmodel.RainDataViewModel
import dev.percula.rainydays.viewmodel.RainDataViewModelFactory

class DetailFragment: Fragment() {

    private val args by navArgs<DetailFragmentArgs>()

    private val viewModel by lazy {
        val factory = RainDataViewModelFactory(
            application = activity!!.application,
            location = args.location
        )
        ViewModelProviders.of(this, factory).get(RainDataViewModel::class.java)
    }

    private val adapter: RainDataAdapter by lazy {
        RainDataAdapter(context?.let {
            PreferenceManager.getDefaultSharedPreferences(it).booleanLiveData(it.getString(R.string.units_key), false)
        }
            ?: mutableLiveDataOf(false))
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_detail, container, false)

        val recyclerView = view.findViewById<RecyclerView>(R.id.recyclerview)
        recyclerView.layoutManager = LinearLayoutManager(view.context)
        recyclerView.adapter = adapter

        viewModel.rainList.observe(viewLifecycleOwner, Observer {
            adapter.submitList(it)
        })

        return view
    }

    override fun onResume() {
        super.onResume()
        view?.rootView?.findViewById<Toolbar>(R.id.toolbar)?.title = args.location.name
    }
}