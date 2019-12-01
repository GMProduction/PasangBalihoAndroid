package com.genossys.pasangbaliho.data.db.response

import com.genossys.pasangbaliho.data.db.entity.Baliho
import com.genossys.pasangbaliho.data.db.entity.FotoBaliho
import com.genossys.pasangbaliho.data.db.entity.Transaksi

data class DetailBalihoResponse(
    val respon: String?,
    val message: String?,
    val baliho: Baliho?,
    val foto: List<FotoBaliho>,
    val transaksi: List<Transaksi>
)