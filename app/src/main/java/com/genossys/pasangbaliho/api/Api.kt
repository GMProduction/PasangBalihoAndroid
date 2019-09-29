package com.genossys.pasangbaliho.api

import com.genossys.pasangbaliho.models.defaultResponse
import com.genossys.pasangbaliho.models.loginResponse
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface Api {

    @FormUrlEncoded
    @POST("createAdvertiser")
    fun createAdvertiser(
        @Field("id") id: Int,
        @Field("email") email: String,
        @Field("nama") nama: String,
        @Field("password") password: String,
        @Field("telp") telp: String,
        @Field("verifikasi") verifikasi: String,
        @Field("api_token") api_token: String,
        @Field("alamat") alamat: String
    ): Call<defaultResponse>

    @FormUrlEncoded
    @POST("advertiserLogin")
    fun advertiserLogin(
        @Field("email") email: String,
        @Field("password") password: String
    ):Call<loginResponse>

}