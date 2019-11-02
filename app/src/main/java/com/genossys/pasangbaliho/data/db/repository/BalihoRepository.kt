package com.genossys.pasangbaliho.data.db.repository

import com.genossys.pasangbaliho.data.db.PasangBalihoDb
import com.genossys.pasangbaliho.data.db.response.BalihoResponse
import com.genossys.pasangbaliho.data.db.response.DetailBalihoResponse
import com.genossys.pasangbaliho.data.network.Api.ApiService
import com.genossys.pasangbaliho.data.network.Api.SafeApiRequest

class BalihoRepository(
    val api: ApiService,
    val db: PasangBalihoDb
) : SafeApiRequest() {
//    private val balihos = MutableLiveData<List<Baliho>>()
//    private val fotos = MutableLiveData<List<FotoBaliho>>()

//    suspend fun getBaliho(): LiveData<List<Baliho>> {
//        return withContext(Dispatchers.IO) {
//            fetchBaliho()
//        }
//    }
//
//    suspend fun getDetailBalihoFoto(id: Int): LiveData<List<FotoBaliho>> {
//        return withContext(Dispatchers.IO) {
//            fetchDetailBalihoFoto(id)
//        }
//    }
//
//    private suspend fun fetchBaliho(): LiveData<List<Baliho>> {
//        val response = apiRequest { api.getAllDataBaliho() }
//        balihos.postValue(response.baliho)
//        return balihos
//    }
//
//    private suspend fun fetchDetailBalihoFoto(id: Int): LiveData<List<FotoBaliho>> {
//        val response = apiRequest { api.showDetailBaliho(id) }
//        fotos.postValue(response.foto)
//        return fotos
//
//    }

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

    suspend fun getDaListBalihoSearchGlobal(kota: String, kategori:String, sortby: String, tambahan: String, page: Int): BalihoResponse {

        return apiRequest {
            api.dataListBalihoSearchGlobal(kota, kategori, sortby, tambahan, page)
        }
    }
}