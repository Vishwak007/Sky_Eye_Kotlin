package com.vvcompany.myapplication


import android.graphics.Bitmap
import android.graphics.BitmapFactory

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.Toast
import com.mapbox.android.core.permissions.PermissionsListener
import com.mapbox.android.core.permissions.PermissionsManager
import com.mapbox.geojson.Point.fromLngLat
import com.mapbox.maps.CameraOptions
import com.mapbox.maps.MapView
import com.mapbox.maps.Style
import com.mapbox.maps.plugin.annotation.AnnotationPlugin
import com.mapbox.maps.plugin.annotation.annotations
import com.mapbox.maps.plugin.annotation.generated.*


class MainActivity : AppCompatActivity() {
    var mapView : MapView? = null
    var permissionManager:PermissionsManager? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mapView = findViewById(R.id.mapView)
        checkPermissionHere()

    }

    private fun checkPermissionHere() {
        if (PermissionsManager.areLocationPermissionsGranted(this)){
            onMapReady()
        }else{

            val permissionListener: PermissionsListener = object : PermissionsListener {
                override fun onExplanationNeeded(permissionsToExplain: MutableList<String>?) {
                    Toast.makeText(this@MainActivity,"This App is totally based on this Permission", Toast.LENGTH_SHORT).show()
                }

                override fun onPermissionResult(granted: Boolean) {
                    if (granted){
                        onMapReady()
                    }
                }

            }

            permissionManager = PermissionsManager(permissionListener)
            permissionManager!!.requestLocationPermissions(this)
        }
    }

    private fun onMapReady() {
        mapView?.getMapboxMap()?.setCamera(
            CameraOptions.Builder()
                .zoom(14.0)
                .build()
        )
        mapView?.getMapboxMap()?.loadStyleUri(
            Style.MAPBOX_STREETS
        ) { doAnnotations() }
    }

    private fun doAnnotations() {

        var bitmap : Bitmap? = BitmapFactory.decodeResource(resources, R.drawable.red_marker_png)

        val  annotationApi: AnnotationPlugin? = mapView?.annotations
//
        val pointAnnotationMan : PointAnnotationManager? =
            mapView?.let { annotationApi?.createPointAnnotationManager(it)}

        if (bitmap != null) {
            val pointAnnoOptions: PointAnnotationOptions =
                PointAnnotationOptions().withPoint(fromLngLat(18.06, 59.31))
                    .withIconImage(bitmap)
            pointAnnotationMan?.create(pointAnnoOptions)
//            Toast.makeText(this, "bitmap used", Toast.LENGTH_SHORT).show()
        }else{
            Toast.makeText(this, "bitmap is blank", Toast.LENGTH_SHORT).show()
        }

        // for circle annotation

        val circleAnnotationManager = mapView?.let { annotationApi?.createCircleAnnotationManager(it) }
        val circleAnnotationOptions: CircleAnnotationOptions = CircleAnnotationOptions()
            .withPoint(fromLngLat(18.06, 59.31))
            .withCircleRadius(8.0)
            .withCircleColor("#ee4e8b")
            .withCircleStrokeWidth(2.0)
            .withCircleStrokeColor("#F10707")
        circleAnnotationManager?.create(circleAnnotationOptions)

    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        permissionManager?.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
//        return super.onOptionsItemSelected(item)

        when(item.itemId){
            R.id.style1 -> changeStyle(Style.MAPBOX_STREETS)
            R.id.style2 -> changeStyle(Style.DARK)
            R.id.style3 -> changeStyle(Style.TRAFFIC_DAY)
            R.id.style4 -> changeStyle(Style.OUTDOORS)
            else -> return super.onOptionsItemSelected(item)
        }
        return true

    }

    private fun changeStyle(style: String) {
        mapView?.getMapboxMap()?.loadStyleUri(
            style
        )

    }

}
