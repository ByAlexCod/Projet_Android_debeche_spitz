package com.example.nasaobjects

import java.time.LocalDate

class NasaObject() {
    private var name: String = "";
    private var mass: Double = 0.0;
    private var year: LocalDate = LocalDate.now();

    constructor(name: String, mass: Double, year: LocalDate): this() {

        this.name = name
        this.mass = mass
        this.year = year
    }

    public fun getName(): String {return name}
    public fun geMass(): Double {return mass}
    public fun getYear(): LocalDate {return year}
}