package com.genossys.pasangbaliho.data.db.repository

import com.genossys.pasangbaliho.data.db.response.NotifikasiResponse
import com.genossys.pasangbaliho.data.network.api.ApiService
import com.genossys.pasangbaliho.data.network.api.SafeApiRequest

class NotifRepository(
    val api: ApiService
) : SafeApiRequest() {

    suspend fun getDataNotif(page: Int, idAdv: Int): NotifikasiResponse {

        return apiRequest {
            api.getDataNotifikasi(page, idAdv)
        }
    }

}