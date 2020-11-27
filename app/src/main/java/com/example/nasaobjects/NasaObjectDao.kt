package com.example.nasaobjects

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface NasaObjectDao {
    @Query("select * from nasaobjectentity")
    fun findAll(): List<NasaObjectEntity>

    @Insert
    fun insertAll(vararg nasaObjectEntity: NasaObjectEntity)
}