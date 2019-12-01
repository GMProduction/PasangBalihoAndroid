package com.genossys.pasangbaliho.ui.home

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.genossys.pasangbaliho.data.db.entity.PageBaliho
import com.genossys.pasangbaliho.data.db.repository.AdvertiserRepository
import com.genossys.pasangbaliho.data.db.repository.BalihoRepository
import com.genossys.pasangbaliho.data.db.repository.TransaksiRepository
import com.genossys.pasangbaliho.data.db.response.CountResponse
import com.genossys.pasangbaliho.data.db.response.PostResponse
import com.genossys.pasangbaliho.data.db.response.SliderResponse
import com.genossys.pasangbaliho.utils.ApiException
import com.genossys.pasangbaliho.utils.Coroutines
import com.genossys.pasangbaliho.utils.NoInternetException
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
                homeListener?.onFailure(e.message!!)
                job.cancel()
            } catch (e: NoInternetException) {
                homeListener?.onFailure(e.message!!)
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