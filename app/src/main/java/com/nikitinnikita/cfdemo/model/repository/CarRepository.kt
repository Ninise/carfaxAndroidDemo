package com.nikitinnikita.cfdemo.model.repository


import android.content.Context
import com.nikitinnikita.cfdemo.model.Car
import com.nikitinnikita.cfdemo.utils.Utils
import com.nikitinnikita.cfdemo.api.CarApiClient
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

object CarRepository {

    suspend fun getAllCars(context: Context) : List<Car> {

    return withContext(Dispatchers.IO) {

        if (Utils.isNetworkConnected(context)) {
            val response = CarApiClient.getAllCars().body()

            CarRoomDatabase.getRoom(context).clearTable()

            response?.car?.forEach {
                CarRoomDatabase.getRoom(context).insertCar(it)
            }

            return@withContext CarRoomDatabase.getRoom(context).getAllCars()
        } else {
            return@withContext CarRoomDatabase.getRoom(context).getAllCars()
        }


    }

    }

    suspend fun getCar(context: Context, vin: String) : Car {
        return CarRoomDatabase.getRoom(context).getCar(vin)
    }

}