package com.devalkasundra.recipesearch.apis

import com.devalkasundra.recipesearch.config.BuildVariantFactory
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object ApiClient {

    private var retrofit: Retrofit? = null
    private val okHttpClient = OkHttpClient()

    val client: Retrofit
        get() {
            if (retrofit == null) {
                val gson = GsonBuilder().setLenient().create()
                retrofit = Retrofit.Builder()
                        .baseUrl(BuildVariantFactory.getBaseURL())
                        .addConverterFactory(GsonConverterFactory.create(gson))
                        .client(okHttpClient.newBuilder().connectTimeout(5, TimeUnit.SECONDS).readTimeout(5, TimeUnit.SECONDS).writeTimeout(5, TimeUnit.SECONDS).build())
                        .build()
            }
            return this.retrofit!!
        }

}
