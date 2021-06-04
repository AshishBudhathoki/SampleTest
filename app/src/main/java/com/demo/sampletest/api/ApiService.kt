package com.demo.sampletest.api

import com.demo.sampletest.data.model.UserInfo
import com.demo.sampletest.data.model.UserPhotos
import retrofit2.Response
import retrofit2.http.GET

interface ApiService {
    companion object {
        const val BASE_URL = "https://jsonplaceholder.typicode.com/"
    }

    @GET("users")
    suspend fun getAllUsers(): Response<List<UserInfo>>

    @GET("photos")
    suspend fun getAllPhotos(): Response<List<UserPhotos>>
}