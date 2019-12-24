package com.projects.pasBal.data.db.response

import com.projects.pasBal.data.db.entity.Kategori

data class KategoriResponse(
    val respon: String?,
    val message: String?,
    val kategori: List<Kategori>
)