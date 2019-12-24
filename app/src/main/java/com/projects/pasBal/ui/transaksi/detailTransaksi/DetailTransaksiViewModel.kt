package com.projects.pasBal.ui.transaksi.detailTransaksi

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.projects.pasBal.data.db.entity.Advertiser
import com.projects.pasBal.data.db.repository.AdvertiserRepository
import com.projects.pasBal.data.db.repository.TransaksiRepository
import com.projects.pasBal.data.db.response.DetailTransaksiResponse
import com.projects.pasBal.data.db.response.PostResponse
import com.projects.pasBal.utils.*
import kotlinx.coroutines.*
import java.net.SocketTimeoutException

class DetailTransaksiViewModel(
    private val repository: TransaksiRepository,
    private val advertiserRepository: AdvertiserRepository
) : ViewModel() {

    var listener: CommonListener? = null
    var job: Job? = null

    suspend fun getUser(): MutableLiveData<Advertiser> = runBlocking {
        val advertiser = MutableLiveData<Advertiser>()
        Coroutines.io {
            try {
                advertiser.postValue(advertiserRepository.getAdvertiser2())
                withContext(Dispatchers.Main) {

                }
            } catch (e: NullPointerException) {
                job?.cancel()
            }
        }
        return@runBlocking advertiser
    }

    suspend fun postSetujuiHarga(
        idAdvertiser: Int,
        apiToken: String,
        idTransaksi: Int
    ): MutableLiveData<PostResponse> {
        job = Job()
        val responsePost = MutableLiveData<PostResponse>()
        listener?.onStartJob()
        job = Coroutines.io {
            try {
                responsePost.postValue(
                    repository.postSetujuiHarga(
                        idAdvertiser,
                        apiToken,
                        idTransaksi
                    )
                )
                withContext(Dispatchers.Main) {
                    listener?.onSuccessJob()
                }

            } catch (e: ApiException) {
                listener?.onFailure(ERROR_API)
            } catch (e: NoInternetException) {
                listener?.onFailure(ERROR_API)
            } catch (e: SocketTimeoutException) {
                listener?.onTimeOut("soket timeout, ulang job lagi")
                (job as CompletableJob).cancel()
            }

        }
        return responsePost
    }

    suspend fun getDetailTransaksi(
        idTransaksi: Int
    ): MutableLiveData<DetailTransaksiResponse> {
        job = Job()
        val transaksi = MutableLiveData<DetailTransaksiResponse>()
        listener?.onStartJob()

        job = Coroutines.io {
            try {
                val transaksiResponse = repository.getDetailTransaksi(idTransaksi)
                transaksiResponse.let {
                    transaksi.postValue(it)
                    withContext(Dispatchers.Main) {
                        listener?.onSuccessJob()
                    }
                }

            } catch (e: ApiException) {
                listener?.onFailure(ERROR_API)
                job?.cancel()
            } catch (e: NoInternetException) {
                listener?.onFailure(ERROR_INTERNET)
                job?.cancel()
            } catch (e: SocketTimeoutException) {
                listener?.onTimeOut("soket timeout")
                job?.cancel()
            }

        }
        return transaksi
    }
}