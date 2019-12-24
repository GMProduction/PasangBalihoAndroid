package com.projects.pasBal.data.db.response

import com.projects.pasBal.data.db.entity.PageBaliho

data class BalihoResponse(
    val respon: String?,
    val message: String?,
    val baliho: PageBaliho
)