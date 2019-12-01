package com.genossys.pasangbaliho.ui.auth

import com.genossys.pasangbaliho.data.db.entity.Advertiser

interface AuthListener {

    fun onStarted()
    fun onSuccess(advertiser: Advertiser)
    fun onFailure(message: String)
}