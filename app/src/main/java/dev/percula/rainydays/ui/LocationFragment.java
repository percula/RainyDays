package dev.percula.rainydays.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import dev.percula.rainydays.R;
import dev.percula.rainydays.core.LCE;
import dev.percula.rainydays.viewmodel.LocationListViewModel;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * A fragment representing a list of Location items.
 */
public class LocationFragment extends Fragment {

    @Nullable private LocationListViewModel locationListViewModel = null;

    public LocationFragment() { }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getActivity() != null) {
            locationListViewModel = ViewModelProviders.of(getActivity()).get(LocationListViewModel.class);
        }
    }

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_location_list, container, false);

        RecyclerView recyclerView = view.findViewById(R.id.recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        LocationAdapter adapter = new LocationAdapter();
        recyclerView.setAdapter(adapter);

        ProgressBar progressBar = view.findViewById(R.id.progress_bar);

        if (locationListViewModel != null) {
            locationListViewModel.getFindWeatherStationLD().observe(this, listLCE -> {
                if (listLCE instanceof LCE.Error) {
                    progressBar.setVisibility(View.GONE);
                    Snackbar.make(view, ((LCE.Error) listLCE).getMessage(), Snackbar.LENGTH_SHORT).show();
                } else if (listLCE instanceof LCE.Loading) {
                    progressBar.setVisibility(View.VISIBLE);
                } else {
                    progressBar.setVisibility(View.GONE);
                }
            });

            locationListViewModel.getLocations().observe(this, locations -> {
                adapter.getItems().clear();
                adapter.getItems().addAll(locations); // In real app, use diff to only notify changed items
                adapter.notifyDataSetChanged();
            });
        }

        ExtendedFloatingActionButton fab = view.findViewById(R.id.fab);

        fab.setOnClickListener(v ->
                NavHostFragment.findNavController(LocationFragment.this)
                        .navigate(LocationFragmentDirections.actionLocationFragmentToLocationPickerFragment()));

        return view;
    }

}
