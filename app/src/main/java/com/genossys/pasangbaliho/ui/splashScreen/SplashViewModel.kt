package com.genossys.pasangbaliho.ui.splashScreen

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.genossys.pasangbaliho.data.db.entity.Advertiser
import com.genossys.pasangbaliho.data.db.repository.AdvertiserRepository
import com.genossys.pasangbaliho.utils.Coroutines
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext

class SplashViewModel(
    private val advertiserRepository: AdvertiserRepository

) : ViewModel() {

    lateinit var job: Job
    
    suspend fun getUser(): MutableLiveData<Advertiser> = runBlocking {
        val advertiser = MutableLiveData<Advertiser>()
        Coroutines.io {
            try {
                advertiser.postValue(advertiserRepository.getAdvertiser2())
                withContext(Dispatchers.Main) {

                }
            } catch (e: NullPointerException) {
                job.cancel()
            }
        }
        return@runBlocking advertiser
    }
}