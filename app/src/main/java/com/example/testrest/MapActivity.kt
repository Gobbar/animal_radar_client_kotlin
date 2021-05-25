package com.example.testrest

import Response.*
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

import android.database.Cursor
import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.widget.TextView
import androidx.core.app.ActivityCompat
import android.Manifest.permission
import android.Manifest.*
import android.Manifest.permission.*
import android.companion.CompanionDeviceManager
import android.os.Build
import android.view.View
import android.widget.Toast
import androidx.core.app.ActivityCompat.*
import androidx.core.content.ContextCompat
import java.security.Permission
import java.util.*
import java.util.jar.Manifest.*

import com.example.testrest.MainActivity
import screens.AnimalList.AnimalListFragment

class MapActivity : AppCompatActivity(), LocListenerInterface {
    //map

    private lateinit var animalListFragment: AnimalListFragment
    lateinit var dbhelper: DBHelper

    private lateinit var forLog: TextView
    private lateinit var forLat: TextView
    private lateinit var myLocListener: MyLocListener
    private lateinit var locationManager: LocationManager
    private lateinit var myLocation: Location
    private var myLatitude: Double = -1.1
    private var myLongitude: Double = -1.1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_map)

        dbhelper = DBHelper(this)

        animalListFragment = AnimalListFragment()
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainer, animalListFragment)
            .commit()

        init()
        checkPermissions()
    }

    private fun init() : Unit {
        forLat = findViewById(R.id.forLat)
        forLog = findViewById(R.id.forLog)
        locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        myLocListener = MyLocListener()
        myLocListener.setLocListenerInterface(this)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if(requestCode == 100 && grantResults[0] == RESULT_OK ){
            checkPermissions()
        }

    }

    private fun checkPermissions() : Unit {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M &&
            (ActivityCompat.checkSelfPermission(this,android.Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_DENIED) &&
            (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_DENIED)) {
                requestPermissions(arrayOf(android.Manifest.permission.ACCESS_COARSE_LOCATION, android.Manifest.permission.ACCESS_FINE_LOCATION),100)
        }
        else {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1500, 15f, myLocListener)
        }
    }

    override fun onLocationChanged(loc: Location) {
        myLatitude = loc.latitude
        myLongitude = loc.longitude
        forLat.setText("lat =  $myLatitude")
        forLog.setText("log = $myLongitude")

    }

    fun getMyLocation(view: View) {
        checkPermissions()

        //if (checkSelfPermission(this, ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && checkSelfPermission(this, ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
        //) {
        //    return
        //}
        //if (myLocation != null) {
        //    myLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)!!
        //    onLocationChanged(myLocation)
        //}
    }

    fun getData(view: View) {
        val database = dbhelper.writableDatabase


        animalListFragment.updateAnimal(Position(40.0, 12.0), database)
        //animals.items = database.getdata()
        //Log.d("mLog", animals.items.get(animals.items.size-1).latitude.toString()+" "+animals.items.get(animals.items.size-1).time)


    }

    fun sendData(view: View) {
        val database = dbhelper.writableDatabase
        val a = Animal(UUID.randomUUID().toString(), 0.toUInt(), myLatitude, myLongitude)
        a.set_cur_time()
        database.setdata(a)
        var animalList = AnimalList(listOf(a))
        animalListFragment.sendAnimalList(animalList)
    }
}