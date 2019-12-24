package com.projects.pasBal.ui.home

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.projects.pasBal.data.db.repository.BalihoRepository
import com.projects.pasBal.data.db.response.DetailBalihoResponse
import com.projects.pasBal.ui.detail.DetailListener
import com.projects.pasBal.utils.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.withContext
import java.net.SocketTimeoutException

class DetailBalihoViewModel(
    private val repository: BalihoRepository
) : ViewModel() {

    var detailListener: DetailListener? = null
    lateinit var job: Job
//    private var id = 1

//    fun setId(idnya: Int) {
//        id = idnya
//    }
//
//    val fotos by lazyDeferred {
//        getFoto(id)
//    }


    fun getDetailBaliho(id: Int): MutableLiveData<DetailBalihoResponse> {
        job = Job()
        var detail = MutableLiveData<DetailBalihoResponse>()
        detailListener?.onStarted()
        job = Coroutines.io {
            try {
                detail.postValue(repository.getDetail(id))
                withContext(Dispatchers.Main){
                    detailListener?.onDetailLoaded()
                }

            } catch (e: ApiException) {
                detailListener?.onFailure(ERROR_API)
            } catch (e: NoInternetException) {
                detailListener?.onFailure(ERROR_INTERNET)
            } catch (e: SocketTimeoutException){
                detailListener?.onTimeOut("soket timeout, ulang job lagi")
                job.cancel()
            }

        }
        return detail
    }


}