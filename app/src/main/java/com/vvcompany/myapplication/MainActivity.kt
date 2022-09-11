package com.vvcompany.myapplication


import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.Toast
import androidx.annotation.DrawableRes
import androidx.appcompat.content.res.AppCompatResources
import com.mapbox.android.core.permissions.PermissionsListener
import com.mapbox.android.core.permissions.PermissionsManager
import com.mapbox.geojson.Point
import com.mapbox.geojson.Point.fromLngLat
import com.mapbox.maps.*
import com.mapbox.maps.plugin.annotation.AnnotationPlugin
import com.mapbox.maps.plugin.annotation.annotations
import com.mapbox.maps.plugin.annotation.generated.*
import com.mapbox.maps.plugin.locationcomponent.location
import com.vvcompany.myapplication.databinding.ActivityMainBinding
import com.vvcompany.myapplication.databinding.ActivityViewAnnotBinding
import java.util.*

class MainActivity : AppCompatActivity() {
    var mapView : MapView? = null
    var permissionManager:PermissionsManager? = null
    var annotationApi: AnnotationPlugin? = null
    var isPointAnno : Boolean = true
    var isCircleAnno : Boolean = false
    var isSwitchoff : Boolean = false
    var pointAnnotationMan : PointAnnotationManager? = null
    var circleAnnotationManager : CircleAnnotationManager? =null

    var center:Point? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

        mapView = findViewById(R.id.mapView)
        center = mapView?.getMapboxMap()?.cameraState?.center
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
        val cameraPosition = CameraOptions.Builder()
            .zoom(4.0)
            .center(center?.longitude()?.let { fromLngLat(it, center?.latitude()!!) })
            .build()
        // set camera position
        mapView?.getMapboxMap()?.setCamera(cameraPosition)
        annotationApi = mapView?.annotations
        mapView?.getMapboxMap()?.loadStyleUri(
            Style.MAPBOX_STREETS
        ) { doAnnotations() }
    }

    private fun doAnnotations() {

        if (isPointAnno){


            var bitmap : Bitmap? = BitmapFactory.decodeResource(resources, R.drawable.red_marker)

            pointAnnotationMan=
                mapView?.let { annotationApi?.createPointAnnotationManager(it)}

            if (bitmap != null) {
                val pointAnnoOptions: PointAnnotationOptions =
                        PointAnnotationOptions().withPoint(center!!)
                            .withIconImage(bitmap).withDraggable(true)

                pointAnnotationMan?.create(pointAnnoOptions)
//            Toast.makeText(this, "bitmap used", Toast.LENGTH_SHORT).show()
            }else{
                Toast.makeText(this, "bitmap is blank", Toast.LENGTH_SHORT).show()
            }

            Toast.makeText(this, "Red Marker Initialized", Toast.LENGTH_SHORT).show()
        }else{
            if (pointAnnotationMan != null){
                annotationApi?.removeAnnotationManager(pointAnnotationMan!!)
            }
        }
        if (isCircleAnno){

            // for circle annotation
            circleAnnotationManager = mapView?.let { annotationApi?.createCircleAnnotationManager(it) }
            val circleAnnotationOptions: CircleAnnotationOptions = CircleAnnotationOptions()
                .withPoint(center!!)
                .withCircleRadius(8.0)
                .withCircleColor("#ee4e8b")
                .withCircleStrokeWidth(2.0)
                .withCircleStrokeColor("#F10707")
            circleAnnotationManager?.create(circleAnnotationOptions)
            Toast.makeText(this, "Circle Marker Initialized", Toast.LENGTH_SHORT).show()

        }else{
            if (circleAnnotationManager != null){
                annotationApi?.removeAnnotationManager(circleAnnotationManager!!)
                isCircleAnno = false
            }
        }
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
            R.id.isPoint -> isPointSwitch()
            R.id.isCircle -> isCircleSwitch()
            R.id.viewAnno -> startActivity(Intent(this, ViewAnnotActivity::class.java))
            else -> return super.onOptionsItemSelected(item)
        }
        return true

    }
    private fun isPointSwitch() {
        isPointAnno = !isPointAnno
        isCircleAnno = false
        onMapReady()

    }

    private fun isCircleSwitch() {
        isCircleAnno = !isCircleAnno
        isPointAnno = false
        onMapReady()
    }

    private fun changeStyle(style: String) {
        mapView?.getMapboxMap()?.loadStyleUri(
            style
        )
        Toast.makeText(this, "Style Changed Successfully", Toast.LENGTH_SHORT).show()

    }

}

