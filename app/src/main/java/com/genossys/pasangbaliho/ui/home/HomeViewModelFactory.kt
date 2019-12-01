package com.genossys.pasangbaliho.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.genossys.pasangbaliho.data.db.repository.AdvertiserRepository
import com.genossys.pasangbaliho.data.db.repository.BalihoRepository
import com.genossys.pasangbaliho.data.db.repository.TransaksiRepository

class HomeViewModelFactory(
    private val repository: BalihoRepository,
    private val repositoryTransaksi: TransaksiRepository,
    private val advertiserRepository: AdvertiserRepository
) : ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return HomeViewModel(repository, repositoryTransaksi, advertiserRepository) as T
    }
}