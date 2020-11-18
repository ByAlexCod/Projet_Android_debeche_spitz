package com.example.nasaobjects

import kotlin.properties.Delegates

class NasaObject {
    private var name: String = "";
    private var mass: Double = 0.0;

    public fun getName(): String {return name}
    public fun geMass(): Double {return mass}

}