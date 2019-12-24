package com.projects.pasBal.data.db.repository

import com.projects.pasBal.data.db.PasangBalihoDb
import com.projects.pasBal.data.network.api.ApiService
import com.projects.pasBal.data.network.api.SafeApiRequest
import com.projects.pasBal.data.db.response.KotaResponse

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