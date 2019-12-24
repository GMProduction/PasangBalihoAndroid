package com.projects.pasBal.data.db.response

import com.projects.pasBal.data.db.entity.PageNews

data class NewsResponse(
    val respon: String?,
    val message: String?,
    val data: PageNews
)