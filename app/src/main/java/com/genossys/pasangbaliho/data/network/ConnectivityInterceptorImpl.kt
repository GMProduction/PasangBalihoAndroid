package com.genossys.pasangbaliho.data.network

import android.content.Context
import android.net.ConnectivityManager
import com.genossys.pasangbaliho.data.internal.NoConnectivityException
import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException

class ConnectivityInterceptorImpl(context: Context) : ConnectivityInterceptor {

    private val aplicationContext = context.applicationContext
    override fun intercept(chain: Interceptor.Chain): Response {
        if(!isOnline())
            throw NoConnectivityException()
        return chain.proceed(chain.request())
    }

    private fun isOnline(): Boolean{
        val connectivyManager = aplicationContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo = connectivyManager.activeNetworkInfo
        return networkInfo != null && networkInfo.isConnected
    }
}