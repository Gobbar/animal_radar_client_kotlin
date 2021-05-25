package com.example.testrest

import android.location.Location

interface LocListenerInterface {
    public fun onLocationChanged(loc: Location) : Unit
}