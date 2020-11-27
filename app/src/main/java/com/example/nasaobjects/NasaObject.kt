package com.example.nasaobjects

import android.graphics.Bitmap
import java.time.LocalDate
import java.util.*

class NasaObject() {
    private var name: String = "";
    private var mass: Double = 0.0;
    private var year: LocalDate = LocalDate.now();
    private var picture: Bitmap? = null;

    constructor(name: String, mass: Double, year: LocalDate, picture: Bitmap? = null): this() {
        this.picture = picture
        this.name = name
        this.mass = mass
        this.year = year
    }

    public fun getName(): String {return name}
    public fun geMass(): Double {return mass}
    public fun getYear(): LocalDate {return year}
    public fun getPicture(): Bitmap? {return picture}
}