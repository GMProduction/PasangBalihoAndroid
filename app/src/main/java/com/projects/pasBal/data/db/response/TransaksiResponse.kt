package com.projects.pasBal.data.db.response

import com.projects.pasBal.data.db.entity.PageTransaksi

data class TransaksiResponse(
    val respon: String?,
    val message: String?,
    val transaksi: PageTransaksi
)