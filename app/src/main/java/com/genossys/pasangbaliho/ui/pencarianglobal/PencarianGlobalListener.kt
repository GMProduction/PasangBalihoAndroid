package com.genossys.pasangbaliho.ui.pencarianglobal

interface PencarianGlobalListener {

    fun onStarted()
    fun onLoadMore()
    fun onSuccess()
    fun onEmpty()
    fun onFailure(message: String)
}