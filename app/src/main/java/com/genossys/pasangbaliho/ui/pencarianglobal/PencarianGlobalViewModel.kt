package com.genossys.pasangbaliho.ui.pencarianglobal

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.genossys.pasangbaliho.data.db.entity.Kategori
import com.genossys.pasangbaliho.data.db.entity.Kota
import com.genossys.pasangbaliho.data.db.entity.PageBaliho
import com.genossys.pasangbaliho.data.db.repository.BalihoRepository
import com.genossys.pasangbaliho.data.db.repository.KategoriRepository
import com.genossys.pasangbaliho.data.db.repository.KotaRepository
import com.genossys.pasangbaliho.utils.ApiException
import com.genossys.pasangbaliho.utils.Coroutines
import com.genossys.pasangbaliho.utils.NoInternetException
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
                    repository.getDaListBalihoSearchGlobal(kota, kategori, sortby, tambahan, page)
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
                pencarianListener?.onFailure(e.message!!)
            } catch (e: NoInternetException) {
                pencarianListener?.onFailure(e.message!!)
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