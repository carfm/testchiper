package com.chiper.testandroid.util

import android.app.Application
import androidx.room.Room

class MovieApp: Application() {
    companion object {
        lateinit var database: MovieDatabase
    }
    override fun onCreate() {
        super.onCreate()
        MovieApp.database =  Room.databaseBuilder(this, MovieDatabase::class.java, "movies-db").build()
    }
}