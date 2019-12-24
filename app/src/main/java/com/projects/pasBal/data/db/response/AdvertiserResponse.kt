package com.projects.pasBal.data.db.response

import com.projects.pasBal.data.db.entity.Advertiser

data class AdvertiserResponse(
    val respon: String?,
    val message: String?,
    val advertiser: Advertiser?
)