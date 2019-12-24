package com.projects.pasBal.data.db.response

import com.projects.pasBal.data.db.entity.Transaksi

data class DetailTransaksiResponse(
    val respon: String?,
    val message: String?,
    val transaksi: Transaksi?
)