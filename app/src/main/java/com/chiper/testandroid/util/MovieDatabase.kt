package com.chiper.testandroid.util

import androidx.room.Database
import androidx.room.RoomDatabase
import com.chiper.testandroid.dao.MovieDao
import com.chiper.testandroid.model.Movie

@Database(
    entities = [Movie::class], version = 1
)
abstract class MovieDatabase : RoomDatabase() {
    abstract fun getMovieDao(): MovieDao
}