package com.kappstudio.joboardgame.ui.map

import android.Manifest
import android.content.Context
import android.location.LocationManager
import androidx.fragment.app.Fragment
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.dylanc.activityresult.launcher.EnableLocationLauncher
import com.google.android.gms.location.*
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.kappstudio.joboardgame.databinding.FragmentMapBinding
import com.kappstudio.joboardgame.ui.party.PartyViewModel
import tech.gujin.toast.ToastUtil
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.kappstudio.joboardgame.R
import com.kappstudio.joboardgame.appInstance
import com.kappstudio.joboardgame.data.Party
import com.permissionx.guolindev.PermissionX
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import timber.log.Timber
import java.util.*


class MapFragment : Fragment(), OnMapReadyCallback, GoogleMap.OnInfoWindowClickListener {

    lateinit var partyViewModel: PartyViewModel
    lateinit var binding: FragmentMapBinding
    private var locationPermissionOk = false
    private lateinit var mMap: GoogleMap
    private lateinit var mLocationProviderClient: FusedLocationProviderClient
    private lateinit var locationCallBack: LocationCallback
    var locationIsUpdate = false

    lateinit var enableLocationLauncher: EnableLocationLauncher

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        partyViewModel = ViewModelProvider(requireParentFragment()).get(PartyViewModel::class.java)

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMapBinding.inflate(inflater)
        partyViewModel.parties?.observe(viewLifecycleOwner, {
            Timber.d("${partyViewModel.parties!!.value}")

        })
        enableLocationLauncher = EnableLocationLauncher(this)


        mLocationProviderClient =
            LocationServices.getFusedLocationProviderClient(context ?: appInstance)

        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        binding.ivBack.setOnClickListener {
            findNavController().popBackStack()
        }

        binding.btnGetLocation.setOnClickListener {
            getPermission()
        }

        return binding.root
    }

    private fun getPermission() {
        PermissionX.init(this)
            .permissions(Manifest.permission.ACCESS_FINE_LOCATION)
            .onExplainRequestReason { scope, deniedList ->
                scope.showRequestReasonDialog(deniedList, "?????????????????????????????????????????????", "OK", "Cancel")
            }
            .onForwardToSettings { scope, deniedList ->
                scope.showForwardToSettingsDialog(
                    deniedList,
                    "??????????????????????????????????????????????????????????????????????????????",
                    "OK",
                    "Cancel"
                )
            }
            .request { allGranted, grantedList, deniedList ->
                if (allGranted) {
                    locationPermissionOk = true
                    ToastUtil.show(getString(R.string.geting_location))
                    //gps
                    checkGPS()
                }
            }
    }

    private fun checkGPS() {

        val locationManager =
            context?.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            context?.let {
                AlertDialog.Builder(it)
                    .setTitle("GPS????????????")
                    .setMessage("??????????????????????????????GPS")
                    .setPositiveButton("????????????") { _, _ ->
                        enableLocationLauncher.launch { enabled ->
                            if (enabled) {
                                checkGPS()
                            }
                        }
                    }
                    .setNegativeButton("??????", null)
                    .show()
            }
        } else {
            getNowLocation()
        }
    }

    private fun getNowLocation() {
        try {
            if (locationPermissionOk) {
                ToastUtil.show("????????????????????????...")

                binding.btnGetLocation.visibility = View.GONE

                var nowLocationCount = 0  //????????????

                // ????????????
                mMap?.isMyLocationEnabled = true

                val locationRequest = LocationRequest()
                locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
                //????????????
                locationRequest.interval = 1000 //1???

                mLocationProviderClient?.requestLocationUpdates(
                    locationRequest,

                    object : LocationCallback() {
                        override fun onLocationResult(locationResult: LocationResult?) {

                            locationCallBack = this
                            locationIsUpdate = true

                            locationResult ?: return
                            nowLocationCount++
                            val nowLocation = LatLng(
                                locationResult.lastLocation.latitude, //??????
                                locationResult.lastLocation.longitude //??????
                            )
                            Timber.d(
                                "???${nowLocationCount}?????????: $nowLocation"
                            )

                            //???????????????????????????
                            if (nowLocationCount == 1) {
                                mMap?.moveCamera(
                                    CameraUpdateFactory.newLatLngZoom(
                                        nowLocation, 15f
                                    )
                                )

                            }
                        }
                    },
                    null
                )

            } else {
                getPermission()
            }
        } catch (e: SecurityException) {
            ToastUtil.show("Exception:$e")
        }

    }

    override fun onMapReady(googleMap: GoogleMap) {
        //???????????????????????????
        mMap = googleMap

        val selectedPartyId: String? = MapFragmentArgs.fromBundle(requireArguments()).partyId
        if (selectedPartyId == null) {
            addPartiesMark()

            moveToLocation(
                LatLng(
                    25.0426166, //??????
                    121.5651808  //??????
                )
            )

        } else {
            val party = partyViewModel.parties?.value?.filter { it.id == selectedPartyId }?.get(0)
            if (party != null) {
                addMark(party)
                moveToLocation(
                    LatLng(
                        party.location.lat, //??????
                        party.location.lng  //??????
                    )
                )
            }
        }

        mMap.setInfoWindowAdapter(context?.let { PartyInfoWindowAdapter(it, partyViewModel) })
        mMap.setOnInfoWindowClickListener(this)

    }

    private fun moveToLocation(latLng: LatLng) {
        mMap.moveCamera(
            CameraUpdateFactory.newLatLngZoom(
                latLng, 15f
            )
        )
    }

    private fun addPartiesMark() {

        partyViewModel.parties?.value?.forEach { party ->
            if (party.partyTime + 3600000 >= Calendar.getInstance().timeInMillis) {
                addMark(party)
            }

        }

    }

    private fun addMark(party: Party) {
        GlobalScope.launch {

            val partyLocation = LatLng(
                party.location.lat, //??????
                party.location.lng  //??????
            )
            activity?.runOnUiThread {
                mMap?.addMarker(
                    MarkerOptions()
                        .position(partyLocation)
                        .title(party.title)
                        .snippet(party.id)
                ).apply {
                    showInfoWindow()
                }
            }
        }
    }

    override fun onInfoWindowClick(p0: Marker) {
         findNavController().navigate(
            MapFragmentDirections.navToPartyDetailFragment(
                p0.snippet
            )
        )
    }


}