package com.projects.pasBal.ui.account

interface AccountListener {

    fun onStartedSignOut()
    fun onSuccessSignOut()
    fun onFailureSignOut(message: String)
}