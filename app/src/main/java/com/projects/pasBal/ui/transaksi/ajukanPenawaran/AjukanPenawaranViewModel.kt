package com.projects.pasBal.ui.transaksi.ajukanPenawaran

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.projects.pasBal.data.db.entity.Advertiser
import com.projects.pasBal.data.db.repository.AdvertiserRepository
import com.projects.pasBal.data.db.repository.TransaksiRepository
import com.projects.pasBal.data.db.response.PostResponse
import com.projects.pasBal.utils.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.withContext
import java.net.SocketTimeoutException

class AjukanPenawaranViewModel(
    private val repository: TransaksiRepository,
    private val advertiserRepository: AdvertiserRepository
) : ViewModel() {

    var listener: CommonListener? = null
    lateinit var job: Job

    suspend fun getUser(): MutableLiveData<Advertiser>{
        val advertiser = MutableLiveData<Advertiser>()
        Coroutines.io {
            try {
                advertiser.postValue(advertiserRepository.getAdvertiser2())
                withContext(Dispatchers.Main) {
                    listener?.onSuccess()
                }
            }catch (e: NullPointerException){
                listener?.onFailure("gagal tarik data advertiser")
            }
        }
        return advertiser
    }

    suspend fun postAjukanTransaksi(
        id_baliho: Int,
        id_advertiser: Int,
        tgl_awal: String,
        tgl_akhir: String,
        apiToken: String
    ): MutableLiveData<PostResponse> {
        job = Job()
        val responsePost = MutableLiveData<PostResponse>()
        listener?.onStartJob()
        job = Coroutines.io {
            try {
                responsePost.postValue(
                    repository.postAjukanPenawaran(
                        id_baliho,
                        id_advertiser,
                        tgl_awal,
                        tgl_akhir,
                        apiToken
                    )
                )
                withContext(Dispatchers.Main) {
                    listener?.onSuccessJob()
                }

            } catch (e: ApiException) {
                listener?.onFailure(ERROR_API)
            } catch (e: NoInternetException) {
                listener?.onFailure(ERROR_INTERNET)
            }catch (e: SocketTimeoutException){
                listener?.onTimeOut("soket timeout, ulang job lagi")
                job.cancel()
            }

        }
        return responsePost
    }


}