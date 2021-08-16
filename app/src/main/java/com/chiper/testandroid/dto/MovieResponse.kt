package com.chiper.testandroid.dto

import com.chiper.testandroid.model.Movie


data class MovieResponse (

	val average_rating : Double,
	val backdrop_path : String,
	val revenue : Long,
	val runtime : Long,
	val description : String,
	val id : Long,
	val iso_3166_1 : String,
	val iso_639_1 : String,
	val name : String,
	val page : Int,
	val poster_path : String,
	val public : Boolean,
	val results : List<Movie>,
	val sort_by : String,
	val total_pages : Int,
	val total_results : Int
)