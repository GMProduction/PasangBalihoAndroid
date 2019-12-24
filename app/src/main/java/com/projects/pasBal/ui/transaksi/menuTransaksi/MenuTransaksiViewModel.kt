package com.projects.pasBal.ui.transaksi.menuTransaksi

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.projects.pasBal.data.db.entity.Advertiser
import com.projects.pasBal.data.db.entity.PageTransaksi
import com.projects.pasBal.data.db.repository.AdvertiserRepository
import com.projects.pasBal.data.db.repository.TransaksiRepository
import com.projects.pasBal.utils.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import java.net.SocketTimeoutException

class MenuTransaksiViewModel(
    private val repository: TransaksiRepository,
    private val advertiserRepository: AdvertiserRepository
) : ViewModel() {

    var listener: CommonListener? = null
    var job: Job? = null

    suspend fun getUser(): MutableLiveData<Advertiser> = runBlocking{
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

    suspend fun getDataTransaksi(
        idAdvertiser: Int,
        status: String,
        page: Int
    ): MutableLiveData<PageTransaksi> {
        job = Job()
        val transaksi = MutableLiveData<PageTransaksi>()
        listener?.onStartJob()

        job = Coroutines.io {
            try {
                val transaksiResponse =
                    repository.getDataTransaksi(idAdvertiser, status, page)
                transaksiResponse.transaksi.let {
                    transaksi.postValue(it)
                    if (it.transaksi.isEmpty()) {
                        withContext(Dispatchers.Main) {
                            listener?.onEmpty()
                        }
                    } else {
                        withContext(Dispatchers.Main) {
                            listener?.onSuccessJob()
                        }
                    }
                }
            } catch (e: ApiException) {
                listener?.onFailure(ERROR_API)
            } catch (e: NoInternetException) {
                listener?.onFailure(ERROR_INTERNET)
            } catch (e: SocketTimeoutException) {
                listener?.onTimeOut("soket timeout, ulang job lagi")
                job?.cancel()
            }

        }
        return transaksi
    }
}