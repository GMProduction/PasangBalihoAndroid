package com.genossys.pasangbaliho.ui.account

import android.util.Log
import androidx.lifecycle.ViewModel
import com.genossys.pasangbaliho.data.db.repository.AdvertiserRepository
import com.genossys.pasangbaliho.ui.auth.AuthListener
import com.genossys.pasangbaliho.utils.ApiException
import com.genossys.pasangbaliho.utils.Coroutines
import com.genossys.pasangbaliho.utils.NoInternetException

class AccountViewModel(
    val repository: AdvertiserRepository
) : ViewModel() {

    var authListener: AuthListener? = null
    val user = repository.getAdvertiser()

    fun getLoggedInAdvertiser() = repository.getAdvertiser()

    fun deleteAdvertiser() {


        Coroutines.main {

            try {
                repository.deleteAdvertiser()
                Log.d("on sign out", "coba sign out")
                authListener?.onFailure("Anda berhasil keluar")
                return@main

            } catch (e: ApiException) {
                authListener?.onFailure(e.message!!)
            } catch (e: NoInternetException) {
                authListener?.onFailure(e.message!!)
            }

        }

    }

}