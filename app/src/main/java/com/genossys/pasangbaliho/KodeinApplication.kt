package com.genossys.pasangbaliho

import android.app.Application
import com.genossys.pasangbaliho.data.db.PasangBalihoDb
import com.genossys.pasangbaliho.data.db.repository.*
import com.genossys.pasangbaliho.data.network.Api.ApiService
import com.genossys.pasangbaliho.data.network.NetworkConnectionInterceptor
import com.genossys.pasangbaliho.data.preferences.PreferenceProvider
import com.genossys.pasangbaliho.ui.account.AccountViewModelFactory
import com.genossys.pasangbaliho.ui.home.*
import com.genossys.pasangbaliho.ui.sign_in.AuthViewModelFactory
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.androidXModule
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.provider
import org.kodein.di.generic.singleton

class KodeinApplication : Application(), KodeinAware {


    override val kodein = Kodein.lazy {

        import(androidXModule(this@KodeinApplication))
        bind() from singleton { NetworkConnectionInterceptor(instance()) }
        bind() from singleton { ApiService(instance()) }
        bind() from singleton { PasangBalihoDb(instance()) }
        bind() from singleton { PreferenceProvider(instance()) }
        bind() from singleton { AdvertiserRepository(instance(), instance()) }
        bind() from singleton { TransaksiRepository(instance()) }
        bind() from singleton { BalihoRepository(instance(), instance()) }
        bind() from singleton { KotaRepository(instance(), instance()) }
        bind() from singleton { KategoriRepository(instance(), instance()) }
        bind() from provider { AuthViewModelFactory(instance()) }
        bind() from provider { AjukanPenawaranViewModelFactory(instance(), instance()) }
        bind() from provider { MenuTransaksiViewModelFactory(instance(), instance()) }
        bind() from provider { AccountViewModelFactory(instance()) }
        bind() from provider { DetailBalihoViewModelFactory(instance()) }
        bind() from provider { PencarianGlobalViewModelFactory(instance(), instance(), instance()) }
        bind() from provider { HomeViewModelFactory(instance()) }
    }


}