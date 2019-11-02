package com.genossys.pasangbaliho.data.db.response

import com.genossys.pasangbaliho.data.db.entity.Kota

data class KotaResponse(
    val respon: String?,
    val message: String?,
    val kota: List<Kota>
)