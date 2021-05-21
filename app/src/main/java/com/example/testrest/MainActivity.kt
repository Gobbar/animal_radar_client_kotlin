package com.example.testrest

import Response.*
import android.content.ContentValues
import android.database.Cursor
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.FrameLayout
import androidx.lifecycle.ViewModel
import screens.AnimalList.AnimalListFragment
import java.lang.Exception
import java.util.*


class MainActivity : AppCompatActivity() {

    private lateinit var animalListFragment: AnimalListFragment
    lateinit var dbhelper: DBHelper
    val animals:AnimalList = AnimalList(mutableListOf<Animal>())
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        dbhelper = DBHelper(this)

        var button: Button = findViewById(R.id.getData)
        animalListFragment = AnimalListFragment()
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainer, animalListFragment)
            .commit()
    }

    fun getData(view: View) {
        val database = dbhelper.writableDatabase

        animalListFragment.updateAnimal(Position(40.0, 12.0), database)
        animals.items = database.getdata()
        Log.d("mLog", animals.items.get(animals.items.size-1).latitude.toString()+" "+animals.items.get(animals.items.size-1).time)


    }

    fun sendData(view: View) {
        val database = dbhelper.writableDatabase
        val a = Animal(UUID.randomUUID().toString(), 0.toUInt(), 4.0, 4.0)
        a.set_cur_time()
        database.setdata(a)
        //var time = System.currentTimeMillis()
        //var animalList = AnimalList(listOf(a))
        //animalListFragment.sendAnimalList(animalList)
    }
}
