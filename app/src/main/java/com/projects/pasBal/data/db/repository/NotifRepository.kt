package com.projects.pasBal.data.db.repository

import com.projects.pasBal.data.db.response.NotifikasiResponse
import com.projects.pasBal.data.network.api.ApiService
import com.projects.pasBal.data.network.api.SafeApiRequest

class NotifRepository(
    val api: ApiService
) : SafeApiRequest() {

    suspend fun getDataNotif(page: Int, idAdv: Int): NotifikasiResponse {

        return apiRequest {
            api.getDataNotifikasi(page, idAdv)
        }
    }

}