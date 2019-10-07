package com.genossys.pasangbaliho.data.db.repository

import android.annotation.SuppressLint
import android.os.Build
import androidx.lifecycle.LiveData
import com.genossys.pasangbaliho.data.db.AdvertiserDao
import com.genossys.pasangbaliho.data.db.unitlocalized.SpesifikAdvertiserData
import com.genossys.pasangbaliho.data.network.NetworkDataSource
import com.genossys.pasangbaliho.data.network.response.ResponseAdvertiser
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.Dispatcher
import java.time.ZonedDateTime

class AdvertiserRepositoryImpl(
    private val advertiserDao: AdvertiserDao,
    private val networkDataSource: NetworkDataSource
) : AdvertiserRepository {

    init {
        networkDataSource.downloadedAdvertiserData.observeForever { newDataAdvertiser ->
            presistFetchAdvertiser(newDataAdvertiser)
        }
    }

    override suspend fun getAdvertiser(metric: Boolean): LiveData<out SpesifikAdvertiserData> {
        return withContext(Dispatchers.IO){
            return@withContext advertiserDao.getAdvertiser()
        }
    }

    private fun presistFetchAdvertiser(fetcherAdvertiser: ResponseAdvertiser) {
        GlobalScope.launch(Dispatchers.IO) { }
    }

    private suspend fun initAdvertiserData(){

    }

    private suspend fun fetchCurrentAdvertiser(){

    }
    private fun isFecthCurrentNeeded(lasFecthTime: ZonedDateTime):Boolean{
        val thirtyMinuteAgo = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            ZonedDateTime.now().minusMinutes(30)
        } else {
            TODO("VERSION.SDK_INT < O")
        }
        return lasFecthTime.isBefore(thirtyMinuteAgo)
    }
}