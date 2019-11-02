package com.genossys.pasangbaliho.data.db.repository

import com.genossys.pasangbaliho.data.db.response.PostResponse
import com.genossys.pasangbaliho.data.network.Api.ApiService
import com.genossys.pasangbaliho.data.network.Api.SafeApiRequest

class TransaksiRepository(
    val api: ApiService
) : SafeApiRequest() {


    suspend fun postAjukanPenawaran(
        id_baliho: Int,
        id_advertiser: Int,
        tgl_awal: String,
        tgl_akhir: String
    ): PostResponse {

        return apiRequest {
            api.postAjukanPenawaran(id_baliho, id_advertiser, tgl_awal, tgl_akhir)
        }
    }

}