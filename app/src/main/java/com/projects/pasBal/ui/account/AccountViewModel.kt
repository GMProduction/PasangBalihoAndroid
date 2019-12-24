package com.projects.pasBal.ui.account

import androidx.lifecycle.ViewModel
import com.projects.pasBal.data.db.repository.AdvertiserRepository
import com.projects.pasBal.utils.*
import java.net.SocketTimeoutException

class AccountViewModel(
    val repository: AdvertiserRepository
) : ViewModel() {

    var accountListener: AccountListener? = null

    fun getLoggedInAdvertiser() = repository.getAdvertiser()

    suspend fun signOut(fcmToken: String) {
        accountListener?.onStartedSignOut()

        Coroutines.io {
            try {
                repository.deleteFcmAdvertiser(fcmToken).let {
                    if (it.respon == "success") {
                        repository.deleteAdvertiser()
                        accountListener?.onSuccessSignOut()
                    } else {
                        accountListener?.onFailureSignOut(ERROR_API)
                    }
                }
            } catch (e: ApiException) {
                accountListener?.onFailureSignOut(ERROR_API)
            } catch (e: NoInternetException) {
                accountListener?.onFailureSignOut(ERROR_INTERNET)
            } catch (e: SocketTimeoutException) {
                accountListener?.onFailureSignOut("terjadi kesalahan pada koneksi")
            }
        }
    }
}