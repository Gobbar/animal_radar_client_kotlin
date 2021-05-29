package com.example.testrest

import Response.*

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.content.Context
import android.os.Build
import android.view.View
import java.util.*

//alert
import android.app.Dialog
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment

import android.content.pm.PackageManager
//location
import android.location.Location
import android.location.LocationManager

import android.widget.TextView
//timer
import androidx.core.app.ActivityCompat
import android.os.Handler
import android.os.Looper

import screens.AnimalList.AnimalListFragment

import android.Manifest.permission
import android.Manifest.*
import android.Manifest.permission.*
import android.companion.CompanionDeviceManager
import java.util.jar.Manifest.*
import android.database.Cursor
import android.Manifest
import com.example.testrest.MainActivity
import android.widget.Toast
import androidx.core.app.ActivityCompat.*
import androidx.core.content.ContextCompat
import java.security.Permission
import android.util.Log

class MapActivity : AppCompatActivity(), LocListenerInterface {
    //map
    //timer
    lateinit var handler: Handler

    //work with db and server
    private lateinit var animalListFragment: AnimalListFragment
    lateinit var dbhelper: DBHelper
    var animals = mutableListOf<Animal>()

    //location
    private lateinit var myLocListener: MyLocListener
    private lateinit var locationManager: LocationManager
    private lateinit var myLocation: Location
    private var myLatitude: Double = -1.1
    private var myLongitude: Double = -1.1

    private lateinit var forLog: TextView
    private lateinit var forLat: TextView





    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_map)
        //init db
        dbhelper = DBHelper(this)

        //clear db
        val database = dbhelper.writableDatabase
        database.clear()

        //database.getdata()
        //timer
        handler = Handler(Looper.getMainLooper())

        //for server
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
    // timer
    private val repeat = object : Runnable{override fun run(){
        getData()
        handler.postDelayed(this, 10000)
        }
    }
    override fun onResume(){
        super.onResume()
        handler.post(repeat)
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

    fun getData() {

        val database = dbhelper.writableDatabase

        animalListFragment.updateAnimal(Position(myLongitude, myLatitude), database)

        if(database.getdata(animals, myLatitude, myLongitude, (System.currentTimeMillis()/1000).toUInt())){
            val myDialogFragment = MyDialogFragment()
            val manager = supportFragmentManager
            myDialogFragment.show(manager, "myDialog")
        }



    }

    fun sendData(view: View) {
        val database = dbhelper.writableDatabase
        val a = Animal(UUID.randomUUID().toString(), 0.toUInt(), myLatitude, myLongitude)
        a.set_cur_time()
        database.setdata(a)
        animals.add(a)
        var animalList = AnimalList(listOf(a))
        animalListFragment.sendAnimalList(animalList)
    }
}

class MyDialogFragment : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val builder = AlertDialog.Builder(it)
            builder.setTitle("WARNING!!!")
                .setMessage("ANIMAL!!!")

                .setPositiveButton("OK") { dialog, id ->
                    dialog.cancel()
                }
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }
}

