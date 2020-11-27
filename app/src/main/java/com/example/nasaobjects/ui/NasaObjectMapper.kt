package com.example.nasaobjects.ui

import android.os.Build
import androidx.annotation.RequiresApi
import com.example.nasaobjects.NasaObject
import com.example.nasaobjects.NasaObjectEntity
import java.time.LocalDate

class NasaObjectMapper {
    companion object {
        @RequiresApi(Build.VERSION_CODES.O)
        fun entityToObject(entity: NasaObjectEntity): NasaObject {
            return NasaObject(entity.name, entity.mass, LocalDate.parse(entity.year))
        }
    }

}