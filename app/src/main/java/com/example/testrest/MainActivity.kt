package com.example.testrest

import Response.*
import android.content.ContentValues
import android.content.Intent

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log

import android.view.View
import android.widget.Button
import android.widget.FrameLayout
import androidx.lifecycle.ViewModel
import screens.AnimalList.AnimalListFragment
import java.lang.Exception
import java.net.HttpURLConnection
import java.net.URL
import java.util.*


class MainActivity : AppCompatActivity() {

    //val animals:AnimalList = AnimalList(mutableListOf<Animal>())
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val map_button = findViewById<Button>(R.id.buttonMap)
        val info_button = findViewById<Button>(R.id.buttonInfo)
        val about_button = findViewById<Button>(R.id.buttonAbout)
    }



    fun clickToMap(view: View) {
        val mapIntent = Intent(this, MapActivity::class.java)
        startActivity(mapIntent)
    }

    fun clickToInfo(view: View) {
        val infoIntent = Intent(this, InfoActivity::class.java)
        startActivity(infoIntent)
    }

    fun clickToAbout(view: View) {
        val aboutIntent = Intent(this, AboutActivity::class.java)
        startActivity(aboutIntent)
    }
}
