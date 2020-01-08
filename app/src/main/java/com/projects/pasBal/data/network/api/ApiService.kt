package com.projects.pasBal.data.network.api

import com.projects.pasBal.data.db.response.*
import com.projects.pasBal.data.network.NetworkConnectionInterceptor
import okhttp3.OkHttpClient
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*

//const val URL_API = "http://genossys.site/"
const val URL_API = "https://www.pasangbaliho.com/"
const val BASE_URL: String = "$URL_API/api/v1/"
const val URL_FOTO: String = "$URL_API/assets/original/"
const val URL_THUMBNAIL: String = "$URL_API/assets/thumbnails/"
const val URL_SLIDER: String = "$URL_API/assets/slider/"

const val URL_RESPONSE: String = "$URL_API/payment/"
const val URL_BACKEND: String = "$URL_API/payment/"
const val LANG: String = "UTF-8"
const val CURRENCY: String = "IDR"
const val MERCHANT_CODE: String = "ID01270"
const val MERCHANT_KEY: String = "5Z1cr9UxDk"

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

    @FormUrlEncoded
    @POST("registerAdvertiser")
    suspend fun registerAdvertiser(
        @Field("email") email: String,
        @Field("nama") nama: String,
        @Field("namaInstansi") namaInstansi: String,
        @Field("telp") telp: String,
        @Field("password") password: String,
        @Field("alamat") alamat: String
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
        @Query("urutan") urutan: String,
        @Query("tambahan") tambahan: String,
        @Query("page") page: Int
    ): Response<BalihoResponse>

    //DATA SLIDER
    @GET("getSlider")
    suspend fun getSlider(): Response<SliderResponse>

    //DATA NEWS
    @GET("getDataNews")
    suspend fun getDataNews(
        @Query("page") page: Int
    ): Response<NewsResponse>

    //DATA NOTIFIKASI
    @GET("getDataNotifikasi")
    suspend fun getDataNotifikasi(
        @Query("page") page: Int,
        @Query("idAdv") idAdv: Int
    ): Response<NotifikasiResponse>

    //FCM
    @FormUrlEncoded
    @POST("insertFcmAdvertiser")
    suspend fun insertFcmAdvertiser(
        @Field("idAdv") idAdvertiser: Int,
        @Field("fcmToken") apiToken: String
    ): Response<PostResponse>

    @FormUrlEncoded
    @POST("deleteFcmAdvertiser")
    suspend fun deleteFcmAdvertiser(
        @Field("fcmToken") apiToken: String
    ): Response<PostResponse>

    //TRANSAKSI
    @FormUrlEncoded
    @POST("ajukanPenawaran")
    suspend fun postAjukanPenawaran(
        @Field("id_baliho") id_baliho: Int,
        @Field("id_advertiser") id_advertiser: Int,
        @Field("tanggal_awal") tanggal_awal: String,
        @Field("tanggal_akhir") tanggal_akhir: String,
        @Field("apiToken") apiToken: String
    ): Response<PostResponse>

    @FormUrlEncoded
    @POST("setujuiHarga")
    suspend fun postSetujuiHarga(
        @Field("idAdvertiser") idAdvertiser: Int,
        @Field("apiToken") apiToken: String,
        @Field("idTransaksi") idTransaksi: Int
    ): Response<PostResponse>

    @GET("dataTransaksi")
    suspend fun dataTransaksi(
        @Query("id_advertiser") kota: Int,
        @Query("status") kategori: String,
        @Query("page") sortby: Int
    ): Response<TransaksiResponse>

    @GET("detailTransaksi/{id}")
    suspend fun showDetailTransaksi(
        @Path("id") id: Int
    ): Response<DetailTransaksiResponse>


    @GET("countNewTransaksi")
    suspend fun getCountNewTransaksi(
        @Query("idAdv") idAdv: Int
    ): Response<CountResponse>

    @FormUrlEncoded
    @POST("setReadAdvertiser")
    suspend fun setReadAdvertiser(
        @Field("idAdv") idAdvertiser: Int
    ): Response<PostResponse>

    //    etc
    @GET("dataAllKota")
    suspend fun getDataAllKota(): Response<KotaResponse>

    @GET("dataAllKategori")
    suspend fun getDataAllKategori(): Response<KategoriResponse>

    @FormUrlEncoded
    @POST("https://sandbox.ipay88.co.id/epayment/entry.asp")
    suspend fun pembayaran(
        @Field("MerchantCode") MerchantCode: String,
        @Field("PaymentId") PaymentId: Int,
        @Field("RefNo") RefNo: String,
        @Field("Amount") Amount: Int,
        @Field("Currency") Currency: String,
        @Field("ProdDesc") ProdDesc: String,
        @Field("UserName") UserName: String,
        @Field("UserEmail") UserEmail: String,
        @Field("UserContact") UserContact: String,
        @Field("Remark") Remark: String,
        @Field("Lang") Lang: String,
        @Field("Signature") Signature: String,
        @Field("ResponseURL") ResponseURL: String,
        @Field("BackendURL") BackendURL: String
    ): Response<PostResponse>

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

