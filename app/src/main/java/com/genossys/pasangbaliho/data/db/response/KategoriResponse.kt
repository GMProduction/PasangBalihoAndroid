package com.genossys.pasangbaliho.data.db.response

import com.genossys.pasangbaliho.data.db.entity.Kategori

data class KategoriResponse(
    val respon: String?,
    val message: String?,
    val kategori: List<Kategori>
)