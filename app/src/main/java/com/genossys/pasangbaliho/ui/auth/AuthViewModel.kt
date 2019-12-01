package com.genossys.pasangbaliho.ui.auth

import android.util.Log
import android.view.View
import androidx.lifecycle.ViewModel
import com.genossys.pasangbaliho.data.db.repository.AdvertiserRepository
import com.genossys.pasangbaliho.utils.ApiException
import com.genossys.pasangbaliho.utils.Coroutines
import com.genossys.pasangbaliho.utils.NoInternetException
import java.net.SocketTimeoutException

class AuthViewModel(
    private val repository: AdvertiserRepository
) : ViewModel() {

    var email: String? = null
    var password: String? = null

    var authListener: AuthListener? = null

    fun getLoggedInAdvertiser() = repository.getAdvertiser()


    fun onLoginButtonClick(view: View) {
        authListener?.onStarted()
        if (email.isNullOrEmpty() || password.isNullOrEmpty()) {
            authListener?.onFailure("failure: email $email, password $password")
            return
        }

        Coroutines.main {

            try {
                val advertiserResponse = repository.advertiserLogin(email!!, password!!)
                advertiserResponse.advertiser?.let {
                    authListener?.onSuccess(it)
                    repository.saveAdvertiser(it)
                    Log.d("cek login", "nilai it pas input : $it")
                    return@main
                }
            } catch (e: ApiException) {
                authListener?.onFailure(e.message!!)
            } catch (e: NoInternetException) {
                authListener?.onFailure(e.message!!)
            }

        }

    }

    fun onLoginByGoogle(gEmail: String?, gNama: String?, fcmToken: String) {
        authListener?.onStarted()

        Log.d("function jalan", "login by google")

        Coroutines.main {
            try {
                val advertiserResponse = repository.loginByGoogle(gEmail!!, gNama!!)
                advertiserResponse.advertiser?.let {
                    authListener?.onSuccess(it)
                    repository.saveAdvertiser(it)
                    Coroutines.main {
                        repository.insertFcmAdvertiser(it.id!!, fcmToken)
                    }
                    Log.d("cek login", "nilai it pas input : $it")
                    return@main
                }
            } catch (e: ApiException) {
                authListener?.onFailure(e.message!!)
            } catch (e: NoInternetException) {
                authListener?.onFailure(e.message!!)
            } catch (e: SocketTimeoutException) {
                authListener?.onFailure("terjadi kesalahan pada koneksi")
            }
        }

    }

    suspend fun registerAdvertiser(
        email: String,
        nama: String,
        telp: String,
        password: String,
        alamat: String,
        fcmToken: String
    ) {
        authListener?.onStarted()

        Coroutines.main {
            try {
                val advertiserResponse =
                    repository.registerAdvertiser(email, nama, telp, password, alamat)
                advertiserResponse.let {
                    if (advertiserResponse.respon == "failure") {
                        authListener?.onFailure(advertiserResponse.message!!)
                    } else {
                        authListener?.onSuccess(it.advertiser!!)
                        repository.saveAdvertiser(it.advertiser!!)
                        Coroutines.main {
                            repository.insertFcmAdvertiser(it.advertiser.id!!, fcmToken)
                        }
                        return@main
                    }
                }
            } catch (e: ApiException) {
                authListener?.onFailure(e.message!!)
            } catch (e: NoInternetException) {
                authListener?.onFailure(e.message!!)
            } catch (e: SocketTimeoutException) {
                authListener?.onFailure("terjadi kesalahan pada koneksi")
            }

        }

    }

}