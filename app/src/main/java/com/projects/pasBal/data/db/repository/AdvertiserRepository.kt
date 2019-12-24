package com.projects.pasBal.data.db.repository

import com.projects.pasBal.data.db.PasangBalihoDb
import com.projects.pasBal.data.db.entity.Advertiser
import com.projects.pasBal.data.db.response.AdvertiserResponse
import com.projects.pasBal.data.db.response.PostResponse
import com.projects.pasBal.data.network.api.ApiService
import com.projects.pasBal.data.network.api.SafeApiRequest

class AdvertiserRepository(

    val api: ApiService,
    val db: PasangBalihoDb
) : SafeApiRequest() {

    suspend fun advertiserLogin(email: String, password: String): AdvertiserResponse {

        return apiRequest {
            api.loginUser(email, password)
        }
    }

    suspend fun loginByGoogle(email: String, nama: String): AdvertiserResponse {

        return apiRequest {
            api.loginByGoogle(email, nama)
        }
    }


    suspend fun registerAdvertiser(email: String, nama: String, namaInstansi: String,  telp: String, password: String ,alamat: String): AdvertiserResponse {

        return apiRequest {
            api.registerAdvertiser(email, nama, namaInstansi, telp, password, alamat)
        }
    }

    suspend fun saveAdvertiser(advertiser: Advertiser) = db.advertiserDao().insert(advertiser)
    suspend fun deleteAdvertiser() = db.advertiserDao().delete()

    fun getAdvertiser() = db.advertiserDao().checkAdvertiser()

    suspend fun getAdvertiser2() = db.advertiserDao().checkAdvertiser2()



    suspend fun insertFcmAdvertiser(idAdv: Int, fcmToken: String): PostResponse {

        return apiRequest {
            api.insertFcmAdvertiser(idAdv, fcmToken)
        }
    }


    suspend fun deleteFcmAdvertiser(fcmToken: String): PostResponse {

        return apiRequest {
            api.deleteFcmAdvertiser(fcmToken)
        }
    }
}
















