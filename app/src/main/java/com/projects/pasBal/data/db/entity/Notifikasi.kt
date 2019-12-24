package com.projects.pasBal.data.db.entity

import com.google.gson.annotations.SerializedName

data class Notifikasi(

    @SerializedName("id_notif")
    val idNotif: Int?,

    @SerializedName("id_advertiser")
    val idAdvertiser: Int?,


    val judul: String?,

    val isi: String?,

    @SerializedName("id_transaksi")
    val idTransaksi: String?,

    val terlihat: Int?,

    @SerializedName("created_at")
    val createdAt: String?,

    @SerializedName("updated_at")
    val updateAt: String?
    )