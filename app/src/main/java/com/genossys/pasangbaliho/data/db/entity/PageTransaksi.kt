package com.genossys.pasangbaliho.data.db.entity

import com.google.gson.annotations.SerializedName

data class PageTransaksi(

    @SerializedName("current_page")
    val currentPage: Int?,

    @SerializedName("data")
    val transaksi: List<Transaksi?>,

    val total: Int?,
    @SerializedName("last_page")
    val lastPage: Int?

)