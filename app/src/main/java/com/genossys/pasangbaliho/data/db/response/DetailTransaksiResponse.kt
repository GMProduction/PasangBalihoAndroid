package com.genossys.pasangbaliho.data.db.response

import com.genossys.pasangbaliho.data.db.entity.Transaksi

data class DetailTransaksiResponse(
    val respon: String?,
    val message: String?,
    val transaksi: Transaksi?
)