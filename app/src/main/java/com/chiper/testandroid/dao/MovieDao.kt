package com.chiper.testandroid.dao


import androidx.room.*
import com.chiper.testandroid.model.Movie


@Dao
interface MovieDao {

    @Query("SELECT * FROM movie")
     fun getAll(): MutableList<Movie>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
     fun insertAll(vararg movies: Movie)

    @Delete
     fun delete(movie: Movie)

}