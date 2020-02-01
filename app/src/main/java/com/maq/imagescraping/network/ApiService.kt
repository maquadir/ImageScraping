package com.maq.imagescraping.network

import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory
import retrofit2.http.GET

const val BASE_URl = "https://imgur.com/"

interface ApiService {

//    @get:GET("gallery")
//    @GET("gallery")
//    suspend fun getItems() : Response<String?>?
//
//
//    companion object {
//
//        operator fun invoke(): ApiService {
//
//            val okHttpClient = OkHttpClient().newBuilder()
//                .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
//                .build()
//
//            return Retrofit.Builder()
//                .baseUrl(BASE_URl)
//                .addConverterFactory(ScalarsConverterFactory.create())
//                .client(okHttpClient)
//                .build().create(ApiService::class.java)
//        }
//    }

    @GET("gallery")
    suspend fun getItems() : Request


    companion object {

        operator fun invoke(): Request {

            val okHttpClient = OkHttpClient().newBuilder()
                .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
                .build()

            return Request.Builder()
                .url("https://api.imgur.com/3/gallery//top/viral/week")
                .header("Authorization", "Client-ID c25479844675ee7")
                .header("User-Agent", "My Little App")
                .build()
        }
    }

}
