package com.genossys.pasangbaliho.data.db.response

import com.genossys.pasangbaliho.data.db.entity.PageNotifikasi

data class NotifikasiResponse(
    val respon: String?,
    val message: String?,
    val data: PageNotifikasi
)