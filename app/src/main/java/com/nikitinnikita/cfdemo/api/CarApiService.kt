package com.nikitinnikita.cfdemo.api

import com.nikitinnikita.cfdemo.model.Car
import com.nikitinnikita.cfdemo.model.CarListingsResponse
import retrofit2.Response
import retrofit2.http.GET

interface CarApiService {
    @GET("/assignment.json")
    suspend fun getAllCars(): Response<CarListingsResponse>
}