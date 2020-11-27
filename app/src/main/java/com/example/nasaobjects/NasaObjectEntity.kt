package com.example.nasaobjects

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDate
import java.util.*
import kotlin.properties.Delegates

@Entity
data class NasaObjectEntity(@PrimaryKey val uid: String = UUID.randomUUID().toString(),
                            @ColumnInfo(name = "object_name") val name: String,
                            @ColumnInfo(name = "object_year") val year: String,
                            @ColumnInfo(name = "object_mass") val mass: Double)
