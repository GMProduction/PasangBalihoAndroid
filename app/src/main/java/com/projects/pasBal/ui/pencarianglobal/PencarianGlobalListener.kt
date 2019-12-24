package com.projects.pasBal.ui.pencarianglobal

interface PencarianGlobalListener {

    fun onStarted()
    fun onLoadMore()
    fun onSuccess()
    fun onEmpty()
    fun onTimeOut(message: String)
    fun onFailure(message: String)
}