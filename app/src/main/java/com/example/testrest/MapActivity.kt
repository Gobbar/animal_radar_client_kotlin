package com.example.testrest

//alert

//location

//timer

import Response.*
import android.Manifest.*
import android.Manifest.permission.*
import android.app.Dialog
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Color.parseColor
import android.location.Location
import android.location.LocationManager
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.app.ActivityCompat.*
import androidx.fragment.app.DialogFragment
import screens.AnimalList.AnimalListFragment
import java.util.*
import java.util.jar.Manifest.*


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
    private var myLatitude: Double = 4242424242.0
    private var myLongitude: Double = 4242424242.0

    private lateinit var forLog: TextView
    private lateinit var forLat: TextView

    private lateinit var getLocButton: Button



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
        getLocButton.setBackgroundColor(parseColor("#808080"))
        getLocButton.text = "Подключение к GPS спутнику"
        getLocButton.isClickable=false
        checkPermissions()
    }

    private fun init() : Unit {
        //forLat = findViewById(R.id.forLat)
        //forLog = findViewById(R.id.forLog)
        locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        myLocListener = MyLocListener()
        myLocListener.setLocListenerInterface(this)
        getLocButton = findViewById(R.id.getMyLoc)
    }
    // timer
    private val repeat = object : Runnable{override fun run(){
        getData()
        if (myLatitude == 4242424242.0 || myLongitude == 4242424242.0) {
            getLocButton.isClickable = false
            getLocButton.text = "Подключение к GPS спутнику"
            getLocButton.setBackgroundColor(parseColor("#808080"))
        }
        else {
            getLocButton.isClickable = true
            getLocButton.text = "gotcha!"
            getLocButton.setBackgroundColor(parseColor("#4ba64b"))
        }
        handler.postDelayed(this, 5000)
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
        //forLat.setText("lat =  $myLatitude")
        //forLog.setText("log = $myLongitude")

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
            val timer = Timer()
            timer.schedule(object : TimerTask() {
                override fun run() {
                    myDialogFragment.dismiss() // when the task active then close the dialog
                    timer.cancel() // also just top the timer thread, otherwise,
                    // you may receive a crash report
                }
            }, 5000)
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

