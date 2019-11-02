package com.genossys.pasangbaliho.ui.home

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.genossys.pasangbaliho.data.db.repository.BalihoRepository
import com.genossys.pasangbaliho.data.db.response.DetailBalihoResponse
import com.genossys.pasangbaliho.ui.detail.DetailListener
import com.genossys.pasangbaliho.utils.ApiException
import com.genossys.pasangbaliho.utils.Coroutines
import com.genossys.pasangbaliho.utils.NoInternetException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class DetailBalihoViewModel(
    private val repository: BalihoRepository
) : ViewModel() {

    var detailListener: DetailListener? = null

//    private var id = 1

//    fun setId(idnya: Int) {
//        id = idnya
//    }
//
//    val fotos by lazyDeferred {
//        getFoto(id)
//    }


    fun getDetailBaliho(id: Int): MutableLiveData<DetailBalihoResponse> {
        var detail = MutableLiveData<DetailBalihoResponse>()
        detailListener?.onStarted()
        Coroutines.io {
            try {
                detail.postValue(repository.getDetail(id))
                withContext(Dispatchers.Main){
                    detailListener?.onDetailLoaded()
                }

            } catch (e: ApiException) {
                detailListener?.onFailure(e.message!!)
            } catch (e: NoInternetException) {
                detailListener?.onFailure(e.message!!)
            }

        }
        return detail
    }


}