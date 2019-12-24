package com.projects.pasBal.ui.pencarianglobal

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.projects.pasBal.data.db.entity.Kategori
import com.projects.pasBal.data.db.entity.Kota
import com.projects.pasBal.data.db.entity.PageBaliho
import com.projects.pasBal.data.db.repository.BalihoRepository
import com.projects.pasBal.data.db.repository.KategoriRepository
import com.projects.pasBal.data.db.repository.KotaRepository
import com.projects.pasBal.utils.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.withContext
import java.net.SocketTimeoutException

class PencarianGlobalViewModel(
    private val repository: BalihoRepository,
    private val repositoryKota: KotaRepository,
    private val repositoryKategori: KategoriRepository
) : ViewModel() {

    var pencarianListener: PencarianGlobalListener? = null
    lateinit var job: Job

    suspend fun getBaliho(
        kota: String,
        kategori: String,
        sortby: String,
        urutan: String,
        tambahan: String,
        page: Int,
        awal: Boolean
    ): MutableLiveData<PageBaliho> {
        job = Job()
        val baliho = MutableLiveData<PageBaliho>()
        if (awal) {
            pencarianListener?.onStarted()
        } else {
            pencarianListener?.onLoadMore()
        }

        job = Coroutines.io {
            try {
                val balihosResponse =
                    repository.getDaListBalihoSearchGlobal(kota, kategori, sortby, urutan, tambahan, page)
                balihosResponse.baliho.let {
                    baliho.postValue(it)
                    if (it.baliho.isEmpty()) {
                        withContext(Dispatchers.Main) {
                            pencarianListener?.onEmpty()
                        }
                    } else {
                        withContext(Dispatchers.Main) {
                            pencarianListener?.onSuccess()
                        }
                    }
                }
            } catch (e: ApiException) {
                pencarianListener?.onFailure(ERROR_API)
            } catch (e: NoInternetException) {
                pencarianListener?.onFailure(ERROR_INTERNET)
            }catch (e: SocketTimeoutException){
                pencarianListener?.onTimeOut("soket timeout, ulang job lagi")
                job.cancel()
            }

        }
        return baliho
    }

    suspend fun getKota(): MutableLiveData<List<Kota>> {
        val kota = MutableLiveData<List<Kota>>()
//        pencarianListener?.onStarted()
        Coroutines.io {
            try {
                val kotaResponse = repositoryKota.getDataAllKota()
                kotaResponse.kota.let {
                    kota.postValue(it)
                    withContext(Dispatchers.Main) {
                        //                        pencarianListener?.onSuccess()
                    }
                }
            } catch (e: ApiException) {
//                pencarianListener?.onFailure(e.message!!)
            } catch (e: NoInternetException) {
//                pencarianListener?.onFailure(e.message!!)
            }catch (e: SocketTimeoutException){

            }

        }
        return kota
    }

    suspend fun getKategori(): MutableLiveData<List<Kategori>> {
        val kategori = MutableLiveData<List<Kategori>>()
//        pencarianListener?.onStarted()
        Coroutines.io {
            try {
                val kategoriResponse = repositoryKategori.getDataAllKategori()
                kategoriResponse.kategori.let {
                    kategori.postValue(it)
                    withContext(Dispatchers.Main) {
                        //                        pencarianListener?.onSuccess()
                    }
                }
            } catch (e: ApiException) {
//                pencarianListener?.onFailure(e.message!!)
            } catch (e: NoInternetException) {
//                pencarianListener?.onFailure(e.message!!)
            }catch (e: SocketTimeoutException){

            }

        }
        return kategori
    }
}