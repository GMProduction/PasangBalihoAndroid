package com.projects.pasBal.data.db.repository

import com.projects.pasBal.data.db.response.NewsResponse
import com.projects.pasBal.data.network.api.ApiService
import com.projects.pasBal.data.network.api.SafeApiRequest

class NewsRepository(
    val api: ApiService
) : SafeApiRequest() {

    suspend fun getDataNews(page: Int): NewsResponse {

        return apiRequest {
            api.getDataNews(page)
        }
    }

}