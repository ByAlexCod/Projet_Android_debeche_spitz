package com.example.nasaobjects.ui

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Build
import android.util.Base64
import androidx.annotation.RequiresApi
import com.example.nasaobjects.NasaObject
import com.example.nasaobjects.NasaObjectEntity
import java.time.LocalDate

class NasaObjectMapper {
    companion object {
        @RequiresApi(Build.VERSION_CODES.O)
        fun entityToObject(entity: NasaObjectEntity): NasaObject {
            val imageBytes= Base64.decode(entity.picture, 0)
            val image : Bitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size) // convert base64 string to bitmap
            return NasaObject(entity.name, entity.mass, LocalDate.parse(entity.year), image)
        }
    }

}