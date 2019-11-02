package com.genossys.pasangbaliho.ui.sign_in

import androidx.lifecycle.LiveData
import com.genossys.pasangbaliho.data.db.entity.Advertiser

interface AuthListener {

    fun onStarted()
    fun onSuccess(advertiser: Advertiser)
    fun onFailure(message: String)
}