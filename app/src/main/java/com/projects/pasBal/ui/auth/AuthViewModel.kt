package com.projects.pasBal.ui.auth

import android.util.Log
import androidx.lifecycle.ViewModel
import com.projects.pasBal.data.db.repository.AdvertiserRepository
import com.projects.pasBal.utils.*
import java.net.SocketTimeoutException

class AuthViewModel(
    private val repository: AdvertiserRepository
) : ViewModel() {

    var authListener: AuthListener? = null

    fun signIn(email: String, password: String, fcmToken: String) {
        Coroutines.main {
            try {
                authListener?.onStarted()
                if (email.isEmpty() || password.isEmpty()) {
                    authListener?.onFailure("Field email dan password harus di isi dulu...")
                } else {

                    repository.advertiserLogin(email, password).let {
                        Log.d("cek login", "nilai it pas input : ${it.respon}")
                        if (it.respon == "success") {
                            Coroutines.main {
                                repository.saveAdvertiser(it.advertiser!!)
                                authListener?.onSuccess(it.advertiser)
                                repository.insertFcmAdvertiser(it.advertiser.id!!, fcmToken)
                            }
                        } else {
                            authListener?.onFailure(it.message!!)
                        }

                    }
                }
            } catch (e: ApiException) {
                authListener?.onFailure("Email / Password tidak terdaftar..")
            } catch (e: NoInternetException) {
                authListener?.onFailure(ERROR_INTERNET)
            } catch (e: SocketTimeoutException) {
                authListener?.onFailure("terjadi kesalahan pada koneksi")
            }

        }

    }

    fun onLoginByGoogle(gEmail: String?, gNama: String?, fcmToken: String) {
        authListener?.onStarted()

        Coroutines.main {
            try {
                repository.loginByGoogle(gEmail!!, gNama!!).let {
                    if (it.respon == "belum") {
                        authListener!!.onBelumTerdaftar(gEmail, gNama)
                    } else if (it.respon == "success") {
                        authListener?.onSuccess(it.advertiser!!)
                        repository.saveAdvertiser(it.advertiser!!)
                        Coroutines.main {
                            repository.insertFcmAdvertiser(it.advertiser.id!!, fcmToken)
                        }
                    } else {
                        authListener?.onFailure(ERROR_API)
                    }

                    Log.d("cek login", "nilai it pas input : $it")
                    return@main
                }
            } catch (e: ApiException) {
                authListener?.onFailure(ERROR_API)
            } catch (e: NoInternetException) {
                authListener?.onFailure(ERROR_INTERNET)
            } catch (e: SocketTimeoutException) {
                authListener?.onFailure("terjadi kesalahan pada koneksi")
            }
        }

    }

    suspend fun registerAdvertiser(
        email: String,
        nama: String,
        namaInstansi: String,
        telp: String,
        password: String,
        alamat: String,
        fcmToken: String
    ) {
        authListener?.onStarted()

        Coroutines.main {
            try {
                val advertiserResponse =
                    repository.registerAdvertiser(email, nama, namaInstansi, telp, password, alamat)
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
                authListener?.onFailure(ERROR_API)
            } catch (e: NoInternetException) {
                authListener?.onFailure(ERROR_INTERNET)
            } catch (e: SocketTimeoutException) {
                authListener?.onFailure("terjadi kesalahan pada koneksi")
            }

        }

    }

}