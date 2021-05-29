package screens.AnimalList

import Response.AnimalList
import Response.Position
import Response.QuestApi
import Response.setdata
import android.app.Application
import android.app.Dialog
import android.database.sqlite.SQLiteDatabase
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.AndroidViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class AnimalListViewModel(application: Application): AndroidViewModel(application) {

    private val compositeDisposable = CompositeDisposable()

    override fun onCleared() {
        compositeDisposable.dispose()
        super.onCleared()
    }

    fun fetchAnimalList(questApi: QuestApi?, position: Position, database: SQLiteDatabase){

        questApi?.let {
            compositeDisposable.add(questApi.getAnimals(position)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    database.setdata(it)

                    println("Get response")

                }, {
                    println("Gson error")
                }))
        }


    }

    fun sendAnimalList(questApi: QuestApi?, animalList: AnimalList) {
        questApi?.let {
            compositeDisposable.add(questApi.postAnimals(animalList)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({

                    println("Get response")
                }, {
                    println("Gson error")
                }))
        }
        //compositeDisposable.add(questApi.getAnimals(Position(67.0, 96.0))
        //questApi?.let {
        //    compositeDisposable.add(questApi.postAnimals(animalList))
        //
        //}
    }

}

class MyDialogFragment : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val builder = AlertDialog.Builder(it)
            builder.setTitle("WARNING!")
                .setMessage("ANIMAL!!!!!!")
                .setPositiveButton("OK") {
                        dialog, id ->  dialog.cancel()
                }
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }
}