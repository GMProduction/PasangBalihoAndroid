package com.genossys.pasangbaliho.data.db.response

import com.genossys.pasangbaliho.data.db.entity.PageBaliho

data class BalihoResponse(
    val respon: String?,
    val message: String?,
    val baliho: PageBaliho
)