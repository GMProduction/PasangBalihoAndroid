package com.projects.pasBal.data.db.repository

import com.projects.pasBal.data.db.PasangBalihoDb
import com.projects.pasBal.data.db.response.KategoriResponse
import com.projects.pasBal.data.network.api.ApiService
import com.projects.pasBal.data.network.api.SafeApiRequest

class KategoriRepository(
    val api: ApiService,
    val db: PasangBalihoDb
) : SafeApiRequest() {


    suspend fun getDataAllKategori(): KategoriResponse {

        return apiRequest {
            api.getDataAllKategori()
        }
    }

}