package com.projects.pasBal.data.db.response

import com.projects.pasBal.data.db.entity.Kota

data class KotaResponse(
    val respon: String?,
    val message: String?,
    val kota: List<Kota>
)