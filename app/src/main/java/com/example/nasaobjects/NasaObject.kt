package com.example.nasaobjects

import java.time.LocalDate
import java.util.*
import kotlin.properties.Delegates

class NasaObject {
    private var name: String = "";
    private var mass: Double = 0.0;
    private var year: LocalDate = LocalDate.now();

    public fun getName(): String {return name}
    public fun geMass(): Double {return mass}
    public fun getYear(): LocalDate {return year}


}