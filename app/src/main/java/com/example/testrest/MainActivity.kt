package com.example.testrest

import Response.Animal
import Response.AnimalList
import Response.Position
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.FrameLayout
import androidx.lifecycle.ViewModel
import screens.AnimalList.AnimalListFragment
import java.lang.Exception
import java.util.*


class MainActivity : AppCompatActivity() {

    private lateinit var animalListFragment: AnimalListFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        var button: Button = findViewById(R.id.getData)
        animalListFragment = AnimalListFragment()
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainer, animalListFragment)
            .commit()
    }

    fun getData(view: View) {
        animalListFragment.updateAnimal(Position(40.0, 12.0))
    }

    fun sendData(view: View) {
        var time = System.currentTimeMillis()
        var animalList = AnimalList(listOf(Animal(UUID.randomUUID().toString(), time.toUInt(), 12.12, 42.23)))
        animalListFragment.sendAnimalList(animalList)
    }
}
