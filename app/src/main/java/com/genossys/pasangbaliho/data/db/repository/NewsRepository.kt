package com.genossys.pasangbaliho.data.db.repository

import com.genossys.pasangbaliho.data.db.response.NewsResponse
import com.genossys.pasangbaliho.data.network.api.ApiService
import com.genossys.pasangbaliho.data.network.api.SafeApiRequest

class NewsRepository(
    val api: ApiService
) : SafeApiRequest() {

    suspend fun getDataNews(page: Int): NewsResponse {

        return apiRequest {
            api.getDataNews(page)
        }
    }

}