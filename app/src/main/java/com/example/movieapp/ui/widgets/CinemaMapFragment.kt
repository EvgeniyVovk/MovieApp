package com.example.movieapp.ui.widgets

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.PointF
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.movieapp.R
import com.example.movieapp.appComponent
import com.example.movieapp.data.model.cinemamodel.Cinema
import com.example.movieapp.databinding.FragmentCinemaMapBinding
import com.example.movieapp.utils.Constants.SEARCH_FILTER
import com.example.movieapp.viewModel.CinemaViewModel
import com.example.movieapp.viewModel.ViewModelFactory
import com.google.android.material.snackbar.Snackbar
import com.yandex.mapkit.Animation
import com.yandex.mapkit.GeoObjectCollection
import com.yandex.mapkit.MapKitFactory
import com.yandex.mapkit.geometry.Point
import com.yandex.mapkit.layers.ObjectEvent
import com.yandex.mapkit.map.*
import com.yandex.mapkit.map.Map
import com.yandex.mapkit.mapview.MapView
import com.yandex.mapkit.search.*
import com.yandex.mapkit.user_location.UserLocationLayer
import com.yandex.mapkit.user_location.UserLocationObjectListener
import com.yandex.mapkit.user_location.UserLocationView
import com.yandex.runtime.Error
import com.yandex.runtime.image.ImageProvider
import com.yandex.runtime.network.NetworkError
import com.yandex.runtime.network.RemoteError
import java.lang.Math.*
import javax.inject.Inject

class CinemaMapFragment : Fragment(R.layout.fragment_cinema_map), UserLocationObjectListener,
    Session.SearchListener, CameraListener {

    private lateinit var mapView: MapView
    private val TARGET_LOCATION: Point = Point(59.945933, 30.320045)
    private lateinit var userLocationLayer: UserLocationLayer
    private lateinit var locationPermissionLauncher: ActivityResultLauncher<String>
    private lateinit var searchManager: SearchManager
    private lateinit var map: Map
    private lateinit var searchSession: Session
    private lateinit var userLocationView: UserLocationView
    private lateinit var context: Context

    private var _binding: FragmentCinemaMapBinding? = null
    private val binding get() = _binding!!

    private val viewModel: CinemaViewModel by activityViewModels {
        factory.create()
    }

    @Inject
    lateinit var factory: ViewModelFactory.Factory

    override fun onAttach(context: Context) {
        MapKitFactory.initialize(context)
        context.appComponent.inject(this)
        this.context = context
        super.onAttach(context)
    }

    override fun onStart() {
        mapView.onStart()
        MapKitFactory.getInstance().onStart()
        super.onStart()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        locationPermissionLauncher =
            registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
                if (isGranted) {
                    setupMapKit()
                } else {
                    Toast.makeText(
                        requireContext(),
                        "Невозможно получить местоположение",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCinemaMapBinding.inflate(inflater, container, false)
        mapView = binding.mapview
        if (ContextCompat.checkSelfPermission(requireActivity(), Manifest.permission.ACCESS_FINE_LOCATION)
            != PackageManager.PERMISSION_GRANTED) {
            locationPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        } else {
            setupMapKit()
        }
        return binding.root
    }

    private fun setupMapKit() {
        map = mapView.map
        map.move(CameraPosition(Point(0.0,0.0),25.0f, 0.0f, 0.0f),
            Animation(Animation.Type.SMOOTH, 8.0f), null)

        map.isRotateGesturesEnabled = false
        map.isTiltGesturesEnabled = false
        map.isZoomGesturesEnabled = true

        val mapKit = MapKitFactory.getInstance()
        userLocationLayer = mapKit.createUserLocationLayer(mapView.mapWindow)
        userLocationLayer.isVisible = true
        userLocationLayer.isHeadingEnabled = true
        userLocationLayer.setObjectListener(this)

        SearchFactory.initialize(context)
        searchManager = SearchFactory.getInstance().createSearchManager(SearchManagerType.COMBINED)

        map.addCameraListener(this)
        //submitSearch()
    }

    private fun submitSearch() {
        searchSession = searchManager.submit(SEARCH_FILTER, VisibleRegionUtils.toPolygon(
            map.visibleRegion), SearchOptions(), this)
    }

    private fun showSnackBar(message: String) {
        val snackBar = Snackbar.make(binding.root, message, Snackbar.LENGTH_SHORT)
        snackBar.setBackgroundTint(Color.parseColor("#F44336"))
        snackBar.setTextColor(Color.WHITE)
        snackBar.show()
    }

    private fun getDistanceToCinemas(point: Point): Double {
        return calculateDistance(
            userLocationView.pin.geometry.latitude,
            userLocationView.pin.geometry.longitude,
            point.latitude, point.longitude)
    }

    private fun calculateDistance(lat1: Double, lon1: Double, lat2: Double, lon2: Double): Double {
        val r = 6371
        val latDistance = toRadians(lat2 - lat1)
        val lonDistance = toRadians(lon2 - lon1)
        val a = kotlin.math.sin(latDistance / 2) * kotlin.math.sin(latDistance / 2) +
                kotlin.math.cos(toRadians(lat1)) * kotlin.math.cos(toRadians(lat2)) *
                kotlin.math.sin(lonDistance / 2) * kotlin.math.sin(lonDistance / 2)
        val c = 2 * kotlin.math.atan2(kotlin.math.sqrt(a), kotlin.math.sqrt(1 - a))

        return r * c
    }

    override fun onStop() {
        mapView.onStop()
        MapKitFactory.getInstance().onStop()
        super.onStop()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onObjectAdded(userLocationView: UserLocationView) {
        this.userLocationView = userLocationView
        userLocationLayer.setAnchor(PointF(mapView.width * 0.5f, mapView.height * 0.5f),
            PointF(mapView.width * 0.5f, mapView.height * 0.83f))
        userLocationView.arrow.setIcon(ImageProvider.fromResource(context.applicationContext, R.drawable.user_position_icon),
            IconStyle().setAnchor(PointF(0f,0f)).setRotationType(RotationType.NO_ROTATION))
        val pinIcon = userLocationView.pin.useCompositeIcon()

        pinIcon.setIcon("icon", ImageProvider.fromResource(context.applicationContext, R.drawable.map_search_icon),
            IconStyle().setAnchor(PointF(0f, 0f)).setRotationType(RotationType.NO_ROTATION)
                .setZIndex(0f)
                .setScale(1f)
        )
    }

    override fun onObjectRemoved(p0: UserLocationView) {
    }

    override fun onObjectUpdated(p0: UserLocationView, p1: ObjectEvent) {
    }

    override fun onSearchResponse(response: Response) {
        val mapObjectCollection = map.mapObjects
        val cinemas: MutableList<Cinema> = mutableListOf()
        for (result in response.collection.children) {
            val resultLocation = result.obj?.geometry?.get(0)?.point
            if (resultLocation != null) {
                val placeMark = mapObjectCollection.addPlacemark(
                    resultLocation,
                    ImageProvider.fromResource(context.applicationContext, R.drawable.map_search_icon)
                )
                placeMark.userData = result
                cinemas.add(
                    Cinema(
                        0,
                        result.obj?.name, result.obj?.descriptionText,
                        getDistanceToCinemas(resultLocation)
                    )
                )
            }
        }
        viewModel.addCinemaToShow(cinemas)
    }

    override fun onSearchError(error: Error) {
        var errorMessage = "Неизвестная ошибка"
        if (error is RemoteError) {
            errorMessage = "Ошибка сервера"
        } else if (error is NetworkError) {
            errorMessage = "Нет интернет соединения"
        }
        showSnackBar(errorMessage)
    }

    override fun onCameraPositionChanged(
        map: Map,
        cameraPosition: CameraPosition,
        cameraUpdateReason: CameraUpdateReason,
        finished: Boolean
    ) {
        if (finished) {
            submitSearch()
        }
    }
}