package com.genossys.pasangbaliho.ui.sign_in

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.genossys.pasangbaliho.data.db.repository.AdvertiserRepository

class AuthViewModelFactory(
    private val repository: AdvertiserRepository
) : ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return AuthViewModel(repository) as T
    }
}