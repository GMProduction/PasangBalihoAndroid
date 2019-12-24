package com.projects.pasBal.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.projects.pasBal.data.db.repository.BalihoRepository
import com.projects.pasBal.data.db.repository.KategoriRepository
import com.projects.pasBal.data.db.repository.KotaRepository
import com.projects.pasBal.ui.pencarianglobal.PencarianGlobalViewModel

class PencarianGlobalViewModelFactory(
    private val repository: BalihoRepository,
    private val repositoryKota: KotaRepository,
    private val repositoryKategori: KategoriRepository

) : ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return PencarianGlobalViewModel(repository, repositoryKota, repositoryKategori) as T
    }
}