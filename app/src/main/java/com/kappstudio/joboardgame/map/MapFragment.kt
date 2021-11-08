package com.kappstudio.joboardgame.map

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.location.LocationManager
import androidx.fragment.app.Fragment
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.ViewModelProvider
import com.dylanc.activityresult.launcher.EnableLocationLauncher
import com.dylanc.activityresult.launcher.StartActivityLauncher
import com.google.android.gms.location.*
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.kappstudio.joboardgame.databinding.FragmentMapBinding
import com.kappstudio.joboardgame.party.PartyViewModel
import tech.gujin.toast.ToastUtil
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.libraries.places.api.model.TypeFilter
import com.google.android.libraries.places.widget.Autocomplete
import com.google.android.libraries.places.widget.AutocompleteActivity
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode
import com.kappstudio.joboardgame.R
import com.kappstudio.joboardgame.appInstance
import com.permissionx.guolindev.PermissionX
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import timber.log.Timber
import java.text.SimpleDateFormat
import java.util.*


class MapFragment : Fragment(), OnMapReadyCallback, GoogleMap.OnMarkerClickListener,
    GoogleMap.OnMapClickListener {

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
        partyViewModel.parties.observe(viewLifecycleOwner, {
            Timber.d("${partyViewModel.parties.value}")

        })
        enableLocationLauncher = EnableLocationLauncher(this)


        mLocationProviderClient = LocationServices.getFusedLocationProviderClient(appInstance)

        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)


        binding.btnGetLocation.setOnClickListener {
            getPermission()
        }

        return binding.root
    }

    private fun getPermission() {
        PermissionX.init(this)
            .permissions(Manifest.permission.ACCESS_FINE_LOCATION)
            .onExplainRequestReason { scope, deniedList ->
                scope.showRequestReasonDialog(deniedList, "定位功能需同意位置權限才可使用", "OK", "Cancel")
            }
            .onForwardToSettings { scope, deniedList ->
                scope.showForwardToSettingsDialog(
                    deniedList,
                    "若要使用定位功能，您需要手動在“設置”中允許位置權限",
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
            appInstance.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            AlertDialog.Builder(appInstance)
                .setTitle("GPS尚未開啟")
                .setMessage("使用定位功能需要開啟GPS")
                .setPositiveButton("前往開啟") { _, _ ->
                    enableLocationLauncher.launch { enabled ->
                        if (enabled) {
                            checkGPS()
                        }
                    }
                }
                .setNegativeButton("取消", null)
                .show()
        } else {
            getNowLocation()
        }
    }

    private fun getNowLocation() {
        try {
            if (locationPermissionOk) {
                ToastUtil.show("正在取得目前位置...")

                binding.btnGetLocation.visibility = View.GONE

                var nowLocationCount = 0  //定位次數

                // 藍色小點
                mMap?.isMyLocationEnabled = true

                val locationRequest = LocationRequest()
                locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
                //更新頻率
                locationRequest.interval = 1000 //1秒

                mLocationProviderClient?.requestLocationUpdates(
                    locationRequest,

                    object : LocationCallback() {
                        override fun onLocationResult(locationResult: LocationResult?) {

                            locationCallBack = this
                            locationIsUpdate = true

                            locationResult ?: return
                            nowLocationCount++
                            val nowLocation = LatLng(
                                locationResult.lastLocation.latitude, //緯度
                                locationResult.lastLocation.longitude //經度
                            )
                            Timber.d(
                                "第${nowLocationCount}次定位: $nowLocation"
                            )

                            //移動鏡頭到現在位置
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
        //鏡頭移到點擊的藥局
        mMap = googleMap
        addPartiesMark()


        val defaultLocation = LatLng(
            25.0426166, //緯度
            121.5651808  //經度
        )


        mMap.setOnMarkerClickListener(this)
        mMap.setOnMapClickListener(this)


        mMap.moveCamera(
            CameraUpdateFactory.newLatLngZoom(
                defaultLocation, 15f
            )
        )

        mMap?.setInfoWindowAdapter(MyInfoWindowAdapter(appInstance))


        // setInfoLayout()
        //  moveToPharmacy()

    }

    private fun addPartiesMark() {
        val formatter = SimpleDateFormat("yyyy年MM月dd日 hh:mm")

        GlobalScope.launch {
            partyViewModel.parties.value?.let {
                partyViewModel.parties.value?.forEach {
                    val partyLocation = LatLng(
                        it.location.lat, //緯度
                        it.location.lng  //經度
                    )

                    activity?.runOnUiThread {
                        mMap?.addMarker(
                            MarkerOptions()
                                .position(partyLocation)
                                .title(it.title)

                                .snippet("聚會時間: ${formatter.format(it.partyTime)}")


                        ).apply {
                            showInfoWindow()


                        }
                    }
                }

            }

        }
    }


    override fun onMarkerClick(marker: Marker): Boolean {
//        marker?.title?.let { title ->
//            val filterData = pharmacyInfo?.features?.filter {
//                it.property.name == (title) &&
//                        it.geometry.coordinates[1] == marker?.position.latitude
//                        &&
//                        it.geometry.coordinates[0] == marker?.position.longitude
//            }
//            if (filterData?.size!! > 0) {
//                selectData = filterData.first()
//                setInfoLayout()
//            } else {
//                Log.d(TAG, "查無資料")
//            }
//
//        }
        return false

    }

    override fun onMapClick(p0: LatLng) {
        Timber.d("onMapClick")
    }


}