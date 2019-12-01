package com.genossys.pasangbaliho.data.db.repository

import com.genossys.pasangbaliho.data.db.response.BalihoResponse
import com.genossys.pasangbaliho.data.db.response.DetailBalihoResponse
import com.genossys.pasangbaliho.data.db.response.SliderResponse
import com.genossys.pasangbaliho.data.network.api.ApiService
import com.genossys.pasangbaliho.data.network.api.SafeApiRequest

class BalihoRepository(
    val api: ApiService
) : SafeApiRequest() {

    suspend fun getDetail(id: Int): DetailBalihoResponse {

        return apiRequest {
            api.showDetailBaliho(id)
        }
    }

    suspend fun getBaliho(page: Int): BalihoResponse {

        return apiRequest {
            api.getAllDataBaliho(page)
        }
    }

    suspend fun getSlider(): SliderResponse {

        return apiRequest {
            api.getSlider()
        }
    }

    suspend fun getDaListBalihoSearchGlobal(
        kota: String,
        kategori: String,
        sortby: String,
        urutan: String,
        tambahan: String,
        page: Int
    ): BalihoResponse {

        return apiRequest {
            api.dataListBalihoSearchGlobal(kota, kategori, sortby, urutan, tambahan, page)
        }
    }
}