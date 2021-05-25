package com.example.testrest

import android.location.Location
import android.location.LocationListener

class MyLocListener : LocationListener {
    //исправления для работы
    private lateinit var locListenerInterface: LocListenerInterface

    override fun onLocationChanged(location: Location) {
        locListenerInterface.onLocationChanged(location)
    }

    //сеттер прописывал вручную
    public fun setLocListenerInterface(locListenerInterface: LocListenerInterface) : Unit{
        this.locListenerInterface = locListenerInterface
    }

}