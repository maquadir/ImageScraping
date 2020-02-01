package com.maq.propertyapp.properties

import com.maq.propertyapp.network.SafeApiRequest
import okhttp3.Request

class ItemsRepository(
    private val api: Request
) : SafeApiRequest() {

//    suspend fun getItems() = apiRequest { api.re }

}