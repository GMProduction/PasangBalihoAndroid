package com.genossys.pasangbaliho.ui.home

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.genossys.pasangbaliho.data.db.entity.PageBaliho
import com.genossys.pasangbaliho.data.db.repository.BalihoRepository
import com.genossys.pasangbaliho.utils.ApiException
import com.genossys.pasangbaliho.utils.Coroutines
import com.genossys.pasangbaliho.utils.NoInternetException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.withContext

class HomeViewModel(
    private val repository: BalihoRepository
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
                        job.isCompleted
                    }
                }
            } catch (e: ApiException) {
                homeListener?.onFailure(e.message!!)
            } catch (e: NoInternetException) {
                homeListener?.onFailure(e.message!!)
            }

        }
        return baliho
    }
}