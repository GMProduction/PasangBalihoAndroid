package com.genossys.pasangbaliho.ui.home

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.genossys.pasangbaliho.data.db.entity.Advertiser
import com.genossys.pasangbaliho.data.db.repository.AdvertiserRepository
import com.genossys.pasangbaliho.data.db.repository.TransaksiRepository
import com.genossys.pasangbaliho.data.db.response.PostResponse
import com.genossys.pasangbaliho.utils.ApiException
import com.genossys.pasangbaliho.utils.CommonListener
import com.genossys.pasangbaliho.utils.Coroutines
import com.genossys.pasangbaliho.utils.NoInternetException
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
                listener?.onFailure(e.message!!)
            } catch (e: NoInternetException) {
                listener?.onFailure(e.message!!)
            }catch (e: SocketTimeoutException){
                listener?.onTimeOut("soket timeout, ulang job lagi")
                job.cancel()
            }

        }
        return responsePost
    }


}