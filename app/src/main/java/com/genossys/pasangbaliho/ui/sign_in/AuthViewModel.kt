package com.genossys.pasangbaliho.ui.sign_in

import android.util.Log
import android.view.View
import androidx.lifecycle.ViewModel
import com.genossys.pasangbaliho.data.db.repository.AdvertiserRepository
import com.genossys.pasangbaliho.utils.ApiException
import com.genossys.pasangbaliho.utils.Coroutines
import com.genossys.pasangbaliho.utils.NoInternetException

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

    fun onLoginByGoogle(gEmail: String?, gNama: String?) {
        authListener?.onStarted()

        Log.d("function jalan", "login by google")

        Coroutines.main {
            try {
                val advertiserResponse = repository.loginByGoogle(gEmail!!, gNama!!)
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

}