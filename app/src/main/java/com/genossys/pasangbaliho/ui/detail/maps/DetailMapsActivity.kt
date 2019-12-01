package com.genossys.pasangbaliho.ui.detail.maps

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.genossys.pasangbaliho.R
import com.genossys.pasangbaliho.ui.splashScreen.SplashScreen
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.android.synthetic.main.activity_detail_maps.*


class DetailMapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private var lat: String? = ""
    private var lng: String? = ""
    private val zoomLevel = 16.0f //This goes up to 21
    private lateinit var markerBaliho: Marker
    private lateinit var lokasiBaliho: LatLng

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_maps)
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.

        lat = intent.getStringExtra("lat")
        lng = intent.getStringExtra("lng")
        text_nama.text = intent.getStringExtra("alamat")

        lokasiBaliho = LatLng(lat!!.toDouble(), lng!!.toDouble())
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        button_go_to_location.setOnClickListener {
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(lokasiBaliho, zoomLevel))
            markerBaliho.showInfoWindow()
        }

        button_back.setOnClickListener {
            finish()
        }
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        // Add a marker in Sydney and move the camera

        markerBaliho = mMap.addMarker(MarkerOptions().position(lokasiBaliho).title("Lokasi Baliho"))
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(lokasiBaliho, zoomLevel))
        markerBaliho.showInfoWindow()

    }

    override fun onResume() {
        super.onResume()
        SplashScreen.STATE_ACTIVITY = "DetailMapsActivity"

    }
}
