package com.genossys.pasangbaliho.ui.home

interface HomeListener {

    fun onStarted()
    fun onLoadMore()
    fun onRekomendasiLoaded()
    fun onSuccess()
    fun onFailure(message: String)
}