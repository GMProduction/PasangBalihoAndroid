package com.projects.pasBal.ui.splashScreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.projects.pasBal.data.db.repository.AdvertiserRepository

class SplashViewModelFactory(
    private val advertiserRepository: AdvertiserRepository
) : ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return SplashViewModel(advertiserRepository) as T
    }
}