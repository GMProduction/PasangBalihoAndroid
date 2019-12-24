package com.projects.pasBal.ui.notifications

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.projects.pasBal.data.db.entity.PageNotifikasi
import com.projects.pasBal.data.db.repository.NotifRepository
import com.projects.pasBal.utils.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.withContext
import java.net.SocketTimeoutException

class NotificationsViewModel(
    private val notifRepository: NotifRepository
) : ViewModel() {

    var commonListener: CommonListener? = null
    lateinit var job: Job

    suspend fun getDataNotif(
        page: Int,
        idAdv: Int,
        awal: Boolean
    ): MutableLiveData<PageNotifikasi> {

        if (awal) {
            commonListener?.onStarted()
        } else {
            commonListener?.onStartJob()
        }
        val notif = MutableLiveData<PageNotifikasi>()
        job = Job()
        job = Coroutines.io {
            try {
                val notifResponse = notifRepository.getDataNotif(page, idAdv)
                notifResponse.data.let {
                    notif.postValue(it)
                    withContext(Dispatchers.Main) {
                        if (it.notif.isEmpty()) {
                            withContext(Dispatchers.Main) {
                                commonListener?.onEmpty()
                            }
                        } else {
                            withContext(Dispatchers.Main) {
                                commonListener?.onSuccess()
                            }
                        }
                    }
                }
            } catch (e: ApiException) {
                commonListener?.onFailure(ERROR_API)
                job.cancel()
            } catch (e: NoInternetException) {
                commonListener?.onFailure(ERROR_INTERNET)
                job.cancel()
            } catch (e: SocketTimeoutException) {
                commonListener?.onTimeOut(ERROR_INTERNET)
                job.cancel()
            }

        }
        return notif
    }
}