package com.genossys.pasangbaliho.data.network.Api

import com.genossys.pasangbaliho.data.db.response.*
import com.genossys.pasangbaliho.data.network.NetworkConnectionInterceptor
import okhttp3.OkHttpClient
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*

const val BASE_URL: String = "http://genossys.site/api/"
const val URL_FOTO: String = "http://genossys.site/assets/"

interface ApiService {

    //DATA USER
    @FormUrlEncoded
    @POST("loginAdvertiser")
    suspend fun loginUser(
        @Field("email") email: String,
        @Field("password") password: String
    ): Response<AdvertiserResponse>

    @FormUrlEncoded
    @POST("loginByGoogle")
    suspend fun loginByGoogle(
        @Field("email") email: String,
        @Field("nama") nama: String
    ): Response<AdvertiserResponse>


    //DATA BALIHO
    @GET("dataListAllBaliho")
    suspend fun getAllDataBaliho(
        @Query("page") page: Int
    ): Response<BalihoResponse>

    @GET("showDetailBaliho/{id}")
    suspend fun showDetailBaliho(
        @Path("id") id: Int
    ): Response<DetailBalihoResponse>

    @GET("dataListBalihoSearchGlobal")
    suspend fun dataListBalihoSearchGlobal(
        @Query("kota") kota: String,
        @Query("kategori") kategori: String,
        @Query("sortby") sortby: String,
        @Query("tambahan") tambahan: String,
        @Query("page") page: Int
    ): Response<BalihoResponse>

    //TRANSAKSI
    @FormUrlEncoded
    @POST("ajukanPenawaran")
    suspend fun postAjukanPenawaran(
        @Field("id_baliho") id_baliho: Int,
        @Field("id_advertiser") id_advertiser: Int,
        @Field("tanggal_awal") tanggal_awal: String,
        @Field("tanggal_akhir") tanggal_akhir: String
    ): Response<PostResponse>

    //    etc
    @GET("dataAllKota")
    suspend fun getDataAllKota(): Response<KotaResponse>

    @GET("dataAllKategori")
    suspend fun getDataAllKategori(): Response<KategoriResponse>

    companion object {
        operator fun invoke(
            networkConnectionInterceptor: NetworkConnectionInterceptor
        ): ApiService {

            val okHttpClient = OkHttpClient.Builder().addInterceptor(networkConnectionInterceptor)
                .build()

            return Retrofit.Builder()
                .client(okHttpClient)
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(ApiService::class.java)
        }
    }


}

