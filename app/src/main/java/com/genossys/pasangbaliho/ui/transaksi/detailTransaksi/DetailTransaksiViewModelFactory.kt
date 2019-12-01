package com.genossys.pasangbaliho.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.genossys.pasangbaliho.data.db.repository.AdvertiserRepository
import com.genossys.pasangbaliho.data.db.repository.TransaksiRepository
import com.genossys.pasangbaliho.ui.transaksi.detailTransaksi.DetailTransaksiViewModel

class DetailTransaksiViewModelFactory(
    private val repository: TransaksiRepository,
    private val advertiserRepository: AdvertiserRepository
) : ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return DetailTransaksiViewModel(repository, advertiserRepository) as T
    }
}