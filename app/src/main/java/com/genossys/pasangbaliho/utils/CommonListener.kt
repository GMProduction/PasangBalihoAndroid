package com.genossys.pasangbaliho.utils

interface CommonListener {

    fun onStarted()
    fun onSuccess()
    fun onStartJob()
    fun onSuccessJob()
    fun onFailure(message: String)
}