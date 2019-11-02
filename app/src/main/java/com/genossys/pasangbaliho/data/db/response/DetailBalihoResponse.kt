package com.genossys.pasangbaliho.data.db.response

import com.genossys.pasangbaliho.data.db.entity.Baliho
import com.genossys.pasangbaliho.data.db.entity.FotoBaliho

data class DetailBalihoResponse(
    val respon: String?,
    val message: String?,
    val baliho: Baliho?,
    val foto: List<FotoBaliho>
)