package com.projects.pasBal.ui.auth

import com.projects.pasBal.data.db.entity.Advertiser

interface AuthListener {

    fun onStarted()
    fun onSuccess(advertiser: Advertiser)
    fun onFailure(message: String)
    fun onBelumTerdaftar(email: String, nama: String)
}