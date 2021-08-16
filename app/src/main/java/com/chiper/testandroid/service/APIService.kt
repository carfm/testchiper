package com.chiper.testandroid.service

import com.chiper.testandroid.dto.MovieResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface APIService {

    @GET(value = "list/1")
    suspend fun getMovies(
        @Header("Authorization") authHeader: String, @Query("page") page: Int
    ): Response<MovieResponse>

}