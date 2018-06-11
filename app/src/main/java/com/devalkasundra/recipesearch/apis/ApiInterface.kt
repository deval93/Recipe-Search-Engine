package com.devalkasundra.recipesearch.apis

import com.devalkasundra.recipesearch.models.DataResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

interface ApiInterface {

    @Headers("Accept:application/json", "Content-Type:application/json")

    @GET("/api/?")
    fun getRecipes(@Query("p") page: String, @Query(encoded = true, value = "q") ingredients: String): Call<DataResponse>

}
