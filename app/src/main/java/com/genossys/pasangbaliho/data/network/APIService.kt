package com.genossys.pasangbaliho.data.network


import com.genossys.pasangbaliho.data.network.response.ResponseAdvertiser
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import kotlinx.coroutines.Deferred
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query


//localhost:8000/api/getAdvertiser?id=5

interface APIService {

    @GET("getAdvertiser")
    fun getAdvertiserData(
        @Query("id") id: String
    ): Deferred<ResponseAdvertiser>

    companion object {
        operator fun invoke(
            connectivityInterceptor: ConnectivityInterceptor
        ): APIService {

            val requestInterceptor = Interceptor { chain ->
                val url = chain.request()
                    .newBuilder()
                    .build()

                return@Interceptor chain.proceed(url)
            }

            val okHttpClient = OkHttpClient.Builder()
                .addInterceptor(requestInterceptor)
                .addInterceptor(connectivityInterceptor)
                .build()

            return Retrofit.Builder()
                .client(okHttpClient)
                .baseUrl("http://192.168.100.7:8000/api/")
                .addCallAdapterFactory(CoroutineCallAdapterFactory())
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(APIService::class.java)
        }
    }
}