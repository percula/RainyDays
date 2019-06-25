package dev.percula.rainydays.ui

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.location.Location
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.lifecycle.ViewModelProviders
import androidx.lifecycle.lifecycleScope
import com.github.florent37.inlineactivityresult.kotlin.InlineActivityResultException
import com.github.florent37.inlineactivityresult.kotlin.coroutines.startForResult
import com.github.florent37.runtimepermission.kotlin.PermissionException
import com.github.florent37.runtimepermission.kotlin.coroutines.experimental.askPermission
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.model.LatLng
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.snackbar.Snackbar
import com.rtchagas.pingplacepicker.PingPlacePicker
import dev.percula.rainydays.R
import dev.percula.rainydays.viewmodel.LocationListViewModel
import kotlinx.coroutines.launch
import timber.log.Timber

class LocationPickerFragment: BottomSheetDialogFragment() {

    private var locationListVM: LocationListViewModel? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        parentFragment?.activity?.let { locationListVM = ViewModelProviders.of(it).get(LocationListViewModel::class.java) }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        App.app.appComponent.inject(this)
    }

    override fun setupDialog(dialog: Dialog, style: Int) {
        super.setupDialog(dialog, style)

        val contentView = View.inflate(context, R.layout.fragment_location_picker, null)

        contentView.findViewById<TextView>(R.id.location_picker_my_location)?.setOnClickListener {
            useMyLocation(dialog)
        }

        contentView.findViewById<TextView>(R.id.location_picker_map)?.setOnClickListener {
            usePlacePicker(dialog)
        }

        dialog.setContentView(contentView)
    }

    @SuppressLint("MissingPermission")
    private fun useMyLocation(dialog: Dialog) {
        this@LocationPickerFragment.lifecycleScope.launch {
            try {
                askPermission(Manifest.permission.ACCESS_FINE_LOCATION)

                // Now get the location
                val fusedLocationClient =
                    this@LocationPickerFragment.activity?.let { LocationServices.getFusedLocationProviderClient(it) }

                // See if the device has a location cached:
                fusedLocationClient?.lastLocation?.addOnSuccessListener { location: Location? ->
                    // Handle no location found
                    if (location == null) {
                        // No previous location stored, get a new one:
                        val locationRequest = LocationRequest().apply {
                            numUpdates = 1
                            priority = LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY
                        }
                        fusedLocationClient.requestLocationUpdates(
                            locationRequest,
                            object : LocationCallback() {
                                override fun onLocationResult(locationResult: LocationResult?) {
                                    super.onLocationResult(locationResult)
                                    fusedLocationClient.removeLocationUpdates(this)
                                    locationResult?.lastLocation?.let {
                                        locationListVM?.findWeatherStation(LatLng(it.latitude, it.longitude))
                                        dialog.dismiss()
                                    } ?: kotlin.run {
                                        parentFragment?.view?.let {
                                            Snackbar.make(it, getString(R.string.warning_no_location_found), Snackbar.LENGTH_SHORT).show()
                                        }
                                        dialog.dismiss()
                                    }
                                }
                            },
                            null
                        )
                        return@addOnSuccessListener
                    } else {
                        locationListVM?.findWeatherStation(
                            LatLng(
                                location.latitude,
                                location.longitude
                            )
                        )
                        dialog.dismiss()
                    }
                }
            } catch (e: PermissionException) {
                if (e.hasDenied()) {
                    dialog.dismiss()
                }

                if (e.hasForeverDenied()) {
                    dialog.dismiss()
                }
            }
        }
    }

    private fun usePlacePicker(dialog: Dialog) {
        lifecycleScope.launch {
            try {
                parentFragment?.activity?.let { activity ->
                    val pingBuilder = PingPlacePicker.IntentBuilder()
                    pingBuilder.setAndroidApiKey(getString(R.string.location_api_android))
                    pingBuilder.setGeolocationApiKey(getString(R.string.location_api_geolocation))
                    val placeIntent = pingBuilder.build(activity)

                    val result = startForResult(placeIntent)

                    if (result.resultCode == Activity.RESULT_OK) {
                        val place = result.data?.let { PingPlacePicker.getPlace(it) } ?: return@let
                        place.latLng?.let { locationListVM?.findWeatherStation(it) }
                        dialog.dismiss()
                    }
                }
            } catch (e: InlineActivityResultException) {
                Timber.e(e)
            }
        }
    }

}