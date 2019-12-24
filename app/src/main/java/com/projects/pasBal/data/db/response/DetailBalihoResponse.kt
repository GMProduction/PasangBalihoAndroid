package com.projects.pasBal.data.db.response

import com.projects.pasBal.data.db.entity.Baliho
import com.projects.pasBal.data.db.entity.FotoBaliho
import com.projects.pasBal.data.db.entity.Transaksi

data class DetailBalihoResponse(
    val respon: String?,
    val message: String?,
    val baliho: Baliho?,
    val foto: List<FotoBaliho>,
    val transaksi: List<Transaksi>
)