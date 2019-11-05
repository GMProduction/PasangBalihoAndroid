package com.genossys.pasangbaliho.utils

interface CommonListener {

    fun onStarted()
    fun onSuccess()
    fun onStartJob()
    fun onSuccessJob()
    fun onTimeOut(message: String)
    fun onFailure(message: String)
}