package com.genossys.pasangbaliho.data.db.repository

import com.genossys.pasangbaliho.data.db.response.CountResponse
import com.genossys.pasangbaliho.data.db.response.DetailTransaksiResponse
import com.genossys.pasangbaliho.data.db.response.PostResponse
import com.genossys.pasangbaliho.data.db.response.TransaksiResponse
import com.genossys.pasangbaliho.data.network.api.ApiService
import com.genossys.pasangbaliho.data.network.api.SafeApiRequest

class TransaksiRepository(
    val api: ApiService
) : SafeApiRequest() {


    suspend fun postAjukanPenawaran(
        id_baliho: Int,
        id_advertiser: Int,
        tgl_awal: String,
        tgl_akhir: String,
        apiToken: String
    ): PostResponse {

        return apiRequest {
            api.postAjukanPenawaran(id_baliho, id_advertiser, tgl_awal, tgl_akhir, apiToken)
        }
    }

    suspend fun postSetujuiHarga(
        idAdvertiser: Int,
        apiToken: String,
        idTransaksi: Int
    ): PostResponse {

        return apiRequest {
            api.postSetujuiHarga(idAdvertiser, apiToken, idTransaksi)
        }
    }

    suspend fun getDataTransaksi(id_advertiser: Int, status: String, page: Int): TransaksiResponse {
        return apiRequest {
            api.dataTransaksi(id_advertiser, status, page)
        }
    }

    suspend fun getDetailTransaksi(id_transaksi: Int): DetailTransaksiResponse {
        return apiRequest {
            api.showDetailTransaksi(id_transaksi)
        }
    }

    suspend fun getCountNewTransaksi(idAdvertiser: Int): CountResponse {
        return apiRequest {
            api.getCountNewTransaksi(idAdvertiser)
        }
    }

    suspend fun setReadAdvertiser(
        idAdvertiser: Int
    ): PostResponse {

        return apiRequest {
            api.setReadAdvertiser(idAdvertiser)
        }
    }
}