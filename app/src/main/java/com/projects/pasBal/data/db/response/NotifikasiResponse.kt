package com.projects.pasBal.data.db.response

import com.projects.pasBal.data.db.entity.PageNotifikasi

data class NotifikasiResponse(
    val respon: String?,
    val message: String?,
    val data: PageNotifikasi
)