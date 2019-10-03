package com.genossys.pasangbaliho.ui.account

import android.app.Application
import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.gms.auth.api.signin.GoogleSignIn

class AccountViewModel : ViewModel() {

    lateinit var fullname:String;
    lateinit var email:String;
    lateinit var urlFoto:String;
    private lateinit var context: Context




    private val _text = MutableLiveData<String>().apply {
        value = "This is notifications Fragment"
    }
    val text: LiveData<String> = _text
}