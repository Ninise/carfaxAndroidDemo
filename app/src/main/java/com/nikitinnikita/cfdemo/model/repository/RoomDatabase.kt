package com.nikitinnikita.cfdemo.model.repository

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.nikitinnikita.cfdemo.model.Car

object CarRoomDatabase {

    fun getRoom(context: Context) : CarDao {
        val db = Room.databaseBuilder(
            context,
            AppDatabase::class.java, "room-cars"
        ).build()

        return db.carDao()
    }

}

@Database(entities = [Car::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun carDao(): CarDao
}