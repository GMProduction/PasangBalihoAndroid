package com.genossys.pasangbaliho.data.db.response

import com.genossys.pasangbaliho.data.db.entity.PageTransaksi

data class TransaksiResponse(
    val respon: String?,
    val message: String?,
    val transaksi: PageTransaksi
)