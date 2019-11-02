package com.genossys.pasangbaliho.data.db.response

import com.genossys.pasangbaliho.data.db.entity.Advertiser

data class AdvertiserResponse(
    val respon: String?,
    val message: String?,
    val advertiser: Advertiser?
)