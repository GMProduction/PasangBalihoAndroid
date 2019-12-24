package com.projects.pasBal.data.db.entity

import com.google.gson.annotations.SerializedName

data class Kota(

    @SerializedName("id_kota")
    val idKota: String?,

    @SerializedName("id_provinsi")
    val idProvinsi: String?,

    @SerializedName("nama_kota")
    val namaKota: String?

)