package com.genossys.pasangbaliho.data.network

import androidx.lifecycle.LiveData
import com.genossys.pasangbaliho.data.db.entity.Advertiser
import com.genossys.pasangbaliho.data.network.response.ResponseAdvertiser

interface NetworkDataSource {

    val downloadedAdvertiserData: LiveData<ResponseAdvertiser>

    suspend fun fetchAdvertiserData(
        id: String
    )
}