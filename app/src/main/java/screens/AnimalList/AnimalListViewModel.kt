package screens.AnimalList

import Response.AnimalList
import Response.Position
import Response.QuestApi
import android.app.Application
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

    fun fetchAnimalList(questApi: QuestApi?, position: Position) {
        questApi?.let {
            compositeDisposable.add(questApi.getAnimals(position)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    //TODO("Запись данных в бд")
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
                    //TODO("Запись данных в бд")
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