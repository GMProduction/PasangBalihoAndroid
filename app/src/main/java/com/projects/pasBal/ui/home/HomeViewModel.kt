package com.projects.pasBal.ui.home

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.projects.pasBal.data.db.entity.PageBaliho
import com.projects.pasBal.data.db.repository.AdvertiserRepository
import com.projects.pasBal.data.db.repository.BalihoRepository
import com.projects.pasBal.data.db.repository.TransaksiRepository
import com.projects.pasBal.data.db.response.CountResponse
import com.projects.pasBal.data.db.response.PostResponse
import com.projects.pasBal.data.db.response.SliderResponse
import com.projects.pasBal.utils.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.withContext
import java.net.SocketTimeoutException

class HomeViewModel(
    private val repository: BalihoRepository,
    private val repositoryTRansaksi: TransaksiRepository,
    private val advertiserRepository: AdvertiserRepository

) : ViewModel() {

    var homeListener: HomeListener? = null
    lateinit var job: Job

    suspend fun getBaliho(page: Int, awal: Boolean): MutableLiveData<PageBaliho> {

        if (awal) {
            homeListener?.onStarted()
        } else {
            homeListener?.onLoadMore()
        }
        val baliho = MutableLiveData<PageBaliho>()
        job = Job()
        job = Coroutines.io {
            try {
                val balihosResponse = repository.getBaliho(page)
                balihosResponse.baliho.let {
                    baliho.postValue(it)
                    withContext(Dispatchers.Main) {
                        homeListener?.onRekomendasiLoaded()
                    }
                }
            } catch (e: ApiException) {
                homeListener?.onFailure(ERROR_API)
                job.cancel()
            } catch (e: NoInternetException) {
                homeListener?.onFailure(ERROR_INTERNET)
                job.cancel()
            } catch (e: SocketTimeoutException) {
                homeListener?.onTimeOut("soket timeout, ulang job lagi")
                job.cancel()
            }

        }
        return baliho
    }


    suspend fun getSlider(): MutableLiveData<SliderResponse> {

        val slider = MutableLiveData<SliderResponse>()
        job = Job()
        job = Coroutines.io {
            try {
                val sliderResponse = repository.getSlider()
                sliderResponse.let {
                    slider.postValue(it)
                }
            } catch (e: ApiException) {
                job.cancel()
            } catch (e: NoInternetException) {
                job.cancel()
            } catch (e: SocketTimeoutException) {
                job.cancel()
            }

        }
        return slider
    }

    suspend fun getCountNewTransaksi(idAdv: Int): MutableLiveData<CountResponse> {

        val count = MutableLiveData<CountResponse>()
        job = Job()
        job = Coroutines.io {
            try {
                val countResponse = repositoryTRansaksi.getCountNewTransaksi(idAdv)
                countResponse.let {
                    count.postValue(it)
                }
            } catch (e: ApiException) {
                job.cancel()
            } catch (e: NoInternetException) {
                job.cancel()
            } catch (e: SocketTimeoutException) {
                job.cancel()
            }

        }
        return count
    }

    suspend fun setReadAdvertiser(idAdv: Int): MutableLiveData<PostResponse> {

        val postResponse = MutableLiveData<PostResponse>()
        job = Job()
        job = Coroutines.io {
            try {
                val countResponse = repositoryTRansaksi.setReadAdvertiser(idAdv)
                countResponse.let {
                    postResponse.postValue(it)
                }
            } catch (e: ApiException) {
                job.cancel()
            } catch (e: NoInternetException) {
                job.cancel()
            } catch (e: SocketTimeoutException) {
                job.cancel()
            }

        }
        return postResponse
    }

//    suspend fun getUser(): MutableLiveData<Advertiser> = runBlocking {
//        val advertiser = MutableLiveData<Advertiser>()
//        Coroutines.io {
//            try {
//                advertiser.postValue(advertiserRepository.getAdvertiser2())
//                withContext(Dispatchers.Main) {
//
//                }
//            } catch (e: NullPointerException) {
//                job.cancel()
//            }
//        }
//        return@runBlocking advertiser
//    }

    fun getLoggedInAdvertiser() = advertiserRepository.getAdvertiser()
}