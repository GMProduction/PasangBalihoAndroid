package com.genossys.pasangbaliho.ui.detail.maps

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.genossys.pasangbaliho.R
import com.genossys.pasangbaliho.ui.splashScreen.SplashScreen
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnStreetViewPanoramaReadyCallback
import com.google.android.gms.maps.StreetViewPanorama
import com.google.android.gms.maps.SupportStreetViewPanoramaFragment

class StreetViewDetailActivity : AppCompatActivity(), OnStreetViewPanoramaReadyCallback {


    private lateinit var mMap: GoogleMap

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_street_view_detail)
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportStreetViewPanoramaFragment
        mapFragment.getStreetViewPanoramaAsync(this)
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


    override fun onStreetViewPanoramaReady(p0: StreetViewPanorama?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onResume() {
        super.onResume()
        SplashScreen.STATE_ACTIVITY = "StreetViewDetailActivity"

    }
}
