package com.genossys.pasangbaliho.data.db.repository

import com.genossys.pasangbaliho.data.db.PasangBalihoDb
import com.genossys.pasangbaliho.data.db.response.KategoriResponse
import com.genossys.pasangbaliho.data.network.api.ApiService
import com.genossys.pasangbaliho.data.network.api.SafeApiRequest

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