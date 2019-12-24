package com.projects.pasBal.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.projects.pasBal.data.db.repository.AdvertiserRepository
import com.projects.pasBal.data.db.repository.TransaksiRepository
import com.projects.pasBal.ui.transaksi.detailTransaksi.DetailTransaksiViewModel

class DetailTransaksiViewModelFactory(
    private val repository: TransaksiRepository,
    private val advertiserRepository: AdvertiserRepository
) : ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return DetailTransaksiViewModel(repository, advertiserRepository) as T
    }
}