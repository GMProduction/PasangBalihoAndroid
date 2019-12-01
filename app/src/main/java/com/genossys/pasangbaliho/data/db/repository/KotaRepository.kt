package com.genossys.pasangbaliho.data.db.repository

import com.genossys.pasangbaliho.data.db.PasangBalihoDb
import com.genossys.pasangbaliho.data.network.api.ApiService
import com.genossys.pasangbaliho.data.network.api.SafeApiRequest
import com.genossys.pasangbaliho.data.db.response.KotaResponse

class KotaRepository(
    val api: ApiService,
    val db: PasangBalihoDb
) : SafeApiRequest() {


    suspend fun getDataAllKota(): KotaResponse {

        return apiRequest {
            api.getDataAllKota()
        }
    }

}