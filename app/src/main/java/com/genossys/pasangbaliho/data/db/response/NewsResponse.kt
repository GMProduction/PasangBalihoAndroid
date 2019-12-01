package com.genossys.pasangbaliho.data.db.response

import com.genossys.pasangbaliho.data.db.entity.PageNews

data class NewsResponse(
    val respon: String?,
    val message: String?,
    val data: PageNews
)