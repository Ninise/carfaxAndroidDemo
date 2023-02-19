package com.nikitinnikita.cfdemo.model.repository

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.nikitinnikita.cfdemo.model.Car

@Dao
interface CarDao {

    @Query("SELECT * FROM Car")
    suspend fun getAllCars() : List<Car>

    @Query("SELECT * FROM Car WHERE vin = :vin")
    suspend fun getCar(vin: String) : Car

    @Insert
    suspend fun insertCar(vararg car: Car)

    @Query("DELETE FROM Car")
    suspend fun clearTable()
}