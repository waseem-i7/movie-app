package com.mwi.movieapp.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.mwi.movieapp.domain.model.Movie

@Database(entities = [Movie::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun movieDao(): MovieDao
}