package com.genossys.pasangbaliho.data.network

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.genossys.pasangbaliho.data.internal.NoConnectivityException
import com.genossys.pasangbaliho.data.network.response.ResponseAdvertiser

class NetworkDataSourceImpl(
    private val apiService: APIService
) : NetworkDataSource {
    private val _downloadedAdvertiserdata = MutableLiveData<ResponseAdvertiser>()
    override val downloadedAdvertiserData: LiveData<ResponseAdvertiser>
        get() = _downloadedAdvertiserdata

    override suspend fun fetchAdvertiserData(id: String) {
        try {
            val fetchAdvertiser = apiService.getAdvertiserData(id).await()
            _downloadedAdvertiserdata.postValue(fetchAdvertiser)
        }
        catch (e: NoConnectivityException){
            Log.e("Connectivity", "No Internet connection..", e)
        }
    }
}