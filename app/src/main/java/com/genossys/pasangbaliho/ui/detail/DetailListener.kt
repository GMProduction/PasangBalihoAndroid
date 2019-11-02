package com.genossys.pasangbaliho.ui.detail

interface DetailListener {

    fun onStarted()
    fun onFotoLoaded()
    fun onDetailLoaded()
    fun onSuccess()
    fun onFailure(message: String)
}