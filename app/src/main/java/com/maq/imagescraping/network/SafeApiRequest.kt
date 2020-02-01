package com.maq.propertyapp.network

import okhttp3.Request
import retrofit2.Call
import retrofit2.Response
import java.io.IOException

//class to return response from api request

abstract class SafeApiRequest {

//    suspend fun<String: Any> apiRequest(call: suspend () -> Response<String?>?) : String{
//        val response = call.invoke()
//        if(response?.isSuccessful!!){
//            return response.body()!!
//        }else{
//            //@todo handle api exception
//            throw ApiException(response.code().toString())
//        }
//    }

    suspend fun apiRequest(call: suspend () -> Request) : String {
        val request = call.invoke()
        if(request.isHttps){
            return request.body().toString()
        }else{
            //@todo handle api exception
            throw ApiException(request.toString())
        }
    }

}

class ApiException(message: String): IOException(message)