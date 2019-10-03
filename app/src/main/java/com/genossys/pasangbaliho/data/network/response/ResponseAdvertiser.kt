package com.genossys.pasangbaliho.data.network.response

import com.genossys.pasangbaliho.data.db.entity.Advertiser


data class ResponseAdvertiser(
    val advertiser: Advertiser,
    val status: String
)