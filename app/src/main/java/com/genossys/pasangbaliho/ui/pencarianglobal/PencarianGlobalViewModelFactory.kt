package com.genossys.pasangbaliho.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.genossys.pasangbaliho.data.db.repository.BalihoRepository
import com.genossys.pasangbaliho.data.db.repository.KategoriRepository
import com.genossys.pasangbaliho.data.db.repository.KotaRepository
import com.genossys.pasangbaliho.ui.pencarianglobal.PencarianGlobalViewModel

class PencarianGlobalViewModelFactory(
    private val repository: BalihoRepository,
    private val repositoryKota: KotaRepository,
    private val repositoryKategori: KategoriRepository

) : ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return PencarianGlobalViewModel(repository, repositoryKota, repositoryKategori) as T
    }
}