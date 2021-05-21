package screens.AnimalList

import Response.AnimalList
import Response.Position
import Response.QuestApi
import android.database.sqlite.SQLiteDatabase
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
//import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.example.testrest.AnimalApp
import com.example.testrest.R

class AnimalListFragment: Fragment(R.layout.activity_main) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }

    fun updateAnimal(position: Position, database: SQLiteDatabase) {
        val animalListViewModel = ViewModelProviders.of(this).get(AnimalListViewModel::class.java)
        animalListViewModel.fetchAnimalList((activity?.application as? AnimalApp)?.questApi, position, database)
    }

    fun sendAnimalList(animalList: AnimalList) {
        val animalListViewModel = ViewModelProviders.of(this).get(AnimalListViewModel::class.java)
        animalListViewModel.sendAnimalList((activity?.application as? AnimalApp)?.questApi, animalList)
    }
}