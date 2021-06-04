package com.demo.sampletest.utils

import com.demo.sampletest.api.ApiService
import com.demo.sampletest.data.repository.PhotosRepository
import com.demo.sampletest.data.repository.UsersRepository
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object InjectorUtils {

    private fun provideLoggingInterceptor(): HttpLoggingInterceptor = LoggingInterceptor.create()

    private fun provideOkHttpClient(httpLoggingInterceptor: HttpLoggingInterceptor): OkHttpClient {
        return HttpClient.setupOkHttpClient(httpLoggingInterceptor)
    }

    private fun provideRetrofit(): Retrofit =
        Retrofit.Builder()
            .baseUrl(ApiService.BASE_URL)
            .client(provideOkHttpClient(provideLoggingInterceptor()))
            .addConverterFactory(GsonConverterFactory.create())
            .build()

    private fun providesServiceApi(retrofit: Retrofit): ApiService =
        retrofit.create(ApiService::class.java)

    fun provideUserRepository(): UsersRepository = UsersRepository(
        providesServiceApi(provideRetrofit())
    )

    fun providePhotosRepository(): PhotosRepository = PhotosRepository(
        providesServiceApi(
            provideRetrofit()
        )
    )

}

object LoggingInterceptor {
    fun create(): HttpLoggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

}

object HttpClient {
    fun setupOkHttpClient(httpLoggingInterceptor: HttpLoggingInterceptor): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(httpLoggingInterceptor)
            .connectTimeout(5, TimeUnit.SECONDS)
            .readTimeout(5, TimeUnit.SECONDS)
            .build()
    }
}