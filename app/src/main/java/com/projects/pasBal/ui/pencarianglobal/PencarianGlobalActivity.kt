package com.projects.pasBal.ui.pencarianglobal

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.projects.pasBal.R
import com.projects.pasBal.data.db.entity.Baliho
import com.projects.pasBal.ui.adapter.AdapterBaliho
import com.projects.pasBal.ui.home.PencarianGlobalViewModelFactory
import com.projects.pasBal.ui.splashScreen.SplashScreen
import com.projects.pasBal.utils.Coroutines
import com.projects.pasBal.utils.customSnackBar.ChefSnackbar
import com.projects.pasBal.utils.firebaseServices.MyFirebaseMessagingService
import com.projects.pasBal.utils.snackbarError
import kotlinx.android.synthetic.main.activity_pencarian_global.*
import kotlinx.android.synthetic.main.loading_bottom_layout.*
import kotlinx.android.synthetic.main.shimmer_list.*
import org.kodein.di.KodeinAware
import org.kodein.di.android.kodein
import org.kodein.di.generic.instance
import java.util.*
import kotlin.concurrent.timerTask


class PencarianGlobalActivity : PencarianGlobalListener, KodeinAware, AppCompatActivity() {

    override val kodein by kodein()
    private val factory: PencarianGlobalViewModelFactory by instance()
    private lateinit var balihoAdapter: AdapterBaliho
    lateinit var viewModel: PencarianGlobalViewModel


    var timer = Timer()
    var kota: String? = ""
    var kategori: String? = ""
    var sortby = "nama_kota"
    var tambahan = ""
    var urutan = "ASC"
    var stateSelect = 0
    private var page: Int = 1
    private var totalPage: Int = 0
    private var readyToLoad = true
    private var btnReloadReady = false
    private var isLoadAwalOk = false

    val listKota = mutableListOf("Semua Kota")
    val listKategori = mutableListOf("Semua Kategori")
    var listBaliho: MutableList<Baliho> = mutableListOf()
    private val listSort = mutableListOf("Sort By", "kota", "kategori", "termurah", "terbesar", "terkecil")

    private lateinit var recycleviewPencarian: RecyclerView
    private lateinit var root: ConstraintLayout
    private lateinit var btnReload: ImageView

    private val broadCastReceiver = object : BroadcastReceiver() {
        override fun onReceive(contxt: Context?, intent: Intent?) {

            val tittle: String? = intent?.getStringExtra("tittle")
            val body: String? = intent?.getStringExtra("body")

            ChefSnackbar.make(root, R.mipmap.boyolali, tittle!!, body!!).show()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pencarian_global)
        text_kosong.visibility = View.INVISIBLE


        initComponent()
        initExtra()
        initViewModelDataAwal()
        initRecycleView()
        initSpinner()
        initSpinnerSelected()
        initEditText()
        spinner_kota.setSelection(0)
        spinner_kategori.setSelection(0)

        initButton()

        recycleViewOnBottom()
    }

    private fun initComponent() {
        root = findViewById(R.id.root_layout)
    }

    private fun initButton() {
        button_back.setOnClickListener {
            finish()
        }

        btnReload.setOnClickListener {
            if (btnReloadReady) {
                if (isLoadAwalOk) {
                    Coroutines.main {
                        val balihos =
                            viewModel.getBaliho(
                                kota!!,
                                kategori!!,
                                sortby,
                                urutan,
                                tambahan,
                                page + 1,
                                false
                            )
                        balihos.observe(this@PencarianGlobalActivity, Observer {
                            for (i in it.baliho) {
                                listBaliho.add(i!!)
                            }
                            balihoAdapter.sumitList(listBaliho)
                            page = it.currentPage!!
                        })
                    }
                } else {
                    Coroutines.main {
                        val balihos =
                            viewModel.getBaliho(
                                kota!!,
                                kategori!!,
                                sortby,
                                urutan,
                                tambahan,
                                1,
                                false
                            )
                        balihos.observe(this@PencarianGlobalActivity, Observer {
                            for (i in it.baliho) {
                                listBaliho.add(i!!)
                            }
                            balihoAdapter.sumitList(listBaliho)
                            page = it.currentPage!!
                            totalPage = it.lastPage!!
                            isLoadAwalOk = true
                        })
                    }
                }
            }

        }

    }

    private fun initViewModelDataAwal() {
        viewModel = ViewModelProviders.of(this, factory).get(PencarianGlobalViewModel::class.java)
        viewModel.pencarianListener = this
        btnReload = findViewById(R.id.reload_bottom)

        Coroutines.main {
            val balihos = viewModel.getBaliho(kota!!, kategori!!, sortby, urutan, tambahan, 1, true)
            balihos.observe(this@PencarianGlobalActivity, Observer {
                for (i in it.baliho) {
                    listBaliho.add(i!!)
                }

                balihoAdapter.sumitList(listBaliho)
                page = it.currentPage!!
                totalPage = it.lastPage!!
                isLoadAwalOk = true
            })
        }
    }

    private fun initExtra() {
        kota = intent.getStringExtra("kota")
        kategori = intent.getStringExtra("kategori")
    }


    private fun initSpinner() {

        Coroutines.main {
            listKategori.clear()
            listKota.clear()
            if (kategori != "") {
                listKategori.add(kategori!!)
                listKategori.add("Semua Kategori")
            } else {
                listKategori.add("Semua Kategori")
            }

            if (kota != "") {
                listKota.add(kota!!)
                listKota.add("Semua Kota")
            } else {
                listKota.add("Semua Kota")
            }

            val kotas = viewModel.getKota()
            val kategoris = viewModel.getKategori()
            kotas.observe(this, Observer {
                for (element in it) {
                    listKota.add(element.namaKota.toString())
                }
            })

            kategoris.observe(this, Observer {
                for (element in it) {
                    listKategori.add(element.kategori.toString())
                }
            })

            val arrayAdapterKota =
                ArrayAdapter(this@PencarianGlobalActivity, R.layout.layout_spinner_item, listKota)
            spinner_kota.adapter = arrayAdapterKota

            val arrayAdapterKategori = ArrayAdapter(
                this@PencarianGlobalActivity,
                R.layout.layout_spinner_item,
                listKategori
            )
            spinner_kategori.adapter = arrayAdapterKategori

            val arrayAdapterSort = ArrayAdapter(
                this@PencarianGlobalActivity,
                R.layout.layout_spinner_item,
                listSort
            )
            spinner_sort.adapter = arrayAdapterSort

        }
    }

    private fun initSpinnerSelected() {
        if (spinner_kota != null) {

            spinner_kota.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>,
                    view: View,
                    position: Int,
                    id: Long
                ) {
                    kota = if (listKota[position] == "Semua Kota") {
                        ""
                    } else {
                        listKota[position]
                    }

                    if (stateSelect == 1) {
                        Coroutines.main {
                            val balihos =
                                viewModel.getBaliho(
                                    kota!!,
                                    kategori!!,
                                    sortby,
                                    urutan,
                                    tambahan,
                                    1,
                                    true
                                )
                            balihos.observe(this@PencarianGlobalActivity, Observer {
                                listBaliho.clear()
                                initRecycleView()
                                for (i in it.baliho) {
                                    listBaliho.add(i!!)
                                }
                                balihoAdapter.sumitList(listBaliho)
                                page = it.currentPage!!
                                totalPage = it.lastPage!!
                            })
                        }
                    }
                }

                override fun onNothingSelected(parent: AdapterView<*>) {
                    // Code to perform some action when nothing is selected
                }
            }
        }

        if (spinner_kategori != null) {
            spinner_kategori.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>,
                    view: View,
                    position: Int,
                    id: Long
                ) {
                    kategori = if (listKategori[position] == "Semua Kategori") {
                        ""
                    } else {
                        listKategori[position]
                    }

                    if (stateSelect == 1) {
                        Coroutines.main {
                            val balihos =
                                viewModel.getBaliho(
                                    kota!!,
                                    kategori!!,
                                    sortby,
                                    urutan,
                                    tambahan,
                                    1,
                                    true
                                )
                            balihos.observe(this@PencarianGlobalActivity, Observer {
                                listBaliho.clear()
                                initRecycleView()
                                for (i in it.baliho) {
                                    listBaliho.add(i!!)
                                }
                                balihoAdapter.sumitList(listBaliho)
                                page = it.currentPage!!
                                totalPage = it.lastPage!!
                            })
                        }
                    }
                }

                override fun onNothingSelected(parent: AdapterView<*>) {
                    // Code to perform some action when nothing is selected
                }
            }
        }

        if (spinner_sort != null) {

            spinner_sort.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>,
                    view: View,
                    position: Int,
                    id: Long
                ) {
                    when (listSort[position]) {
                        "Sort By" -> {
                            urutan = "ASC"
                            sortby = "nama_kota"
//                            root.snackbar(listSort[position])

                        }
                        "kota" -> {
                            urutan = "ASC"
                            sortby = "nama_kota"
                        }
                        "kategori" -> {
                            urutan = "ASC"
                            sortby = "kategori"
                        }
                        "termurah" -> {
                            urutan = "ASC"
                            sortby = "harga_market"
                        }
                        "terbesar" -> {
                            urutan = "DESC"
                            sortby = "luas"
                        }
                        "terkecil" -> {
                            urutan = "ASC"
                            sortby = "luas"
                        }
                        else -> {
                            urutan = "ASC"
                            sortby = "nama_kota"
                        }

                    }

                    if (stateSelect == 1) {
                        Coroutines.main {
                            val balihos =
                                viewModel.getBaliho(
                                    kota!!,
                                    kategori!!,
                                    sortby,
                                    urutan,
                                    tambahan,
                                    1,
                                    true
                                )
                            balihos.observe(this@PencarianGlobalActivity, Observer {
                                listBaliho.clear()
                                initRecycleView()
                                for (i in it.baliho) {
                                    listBaliho.add(i!!)
                                }
                                balihoAdapter.sumitList(listBaliho)
                                page = it.currentPage!!
                                totalPage = it.lastPage!!

                            })
                        }
                    }
                }

                override fun onNothingSelected(parent: AdapterView<*>) {
                    // Code to perform some action when nothing is selected
                }
            }
        }


    }

    private fun initEditText() {
        edittext_pencarian_global.requestFocus()
        edittext_pencarian_global.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }

            override fun afterTextChanged(s: Editable?) {
                timer.cancel()
                timer = Timer()
                timer.schedule(timerTask {
                    tambahan = edittext_pencarian_global.text.toString()
                    Coroutines.main {
                        val balihos =
                            viewModel.getBaliho(
                                kota!!,
                                kategori!!,
                                sortby,
                                urutan,
                                tambahan,
                                1,
                                true
                            )
                        balihos.observe(this@PencarianGlobalActivity, Observer {
                            listBaliho.clear()
                            for (i in it.baliho) {
                                listBaliho.add(i!!)
                            }
                            balihoAdapter.sumitList(listBaliho)
                            page = it.currentPage!!
                            totalPage = it.lastPage!!

                        })
                    }
                }, 700)
            }
        })
    }


    private fun initRecycleView() {
        recycleviewPencarian = findViewById(R.id.recycle_view_pencarian)

        recycleviewPencarian.apply {
            layoutManager = LinearLayoutManager(this@PencarianGlobalActivity)
            balihoAdapter = AdapterBaliho()
            adapter = balihoAdapter
            isNestedScrollingEnabled = false
        }
    }

    override fun onStarted() {
        text_kosong.visibility = View.INVISIBLE
        Coroutines.main {
            recycle_view_pencarian.visibility = View.INVISIBLE
            shimmer_layout2.visibility = View.INVISIBLE
            shimmer_layout.visibility = View.VISIBLE
            shimmer_list.startShimmer()
        }
    }

    override fun onLoadMore() {
        btnReloadReady = false
        Coroutines.main {
            shimmer_layout2.visibility = View.VISIBLE
            progress_loading_bottom.visibility = View.VISIBLE
            reload_bottom.visibility = View.GONE
            shimmer_list.startShimmer()
        }

    }

    override fun onSuccess() {
        Coroutines.main {
//            root.snackbar("$urutan $sortby")
            recycle_view_pencarian.visibility = View.VISIBLE
            shimmer_layout.visibility = View.GONE
            shimmer_layout2.visibility = View.GONE
            shimmer_list.stopShimmer()
            stateSelect = 1
            readyToLoad = true
        }
    }

    override fun onTimeOut(message: String) {
        btnReloadReady = true
        Coroutines.main {
            shimmer_layout.visibility = View.GONE
            shimmer_layout2.visibility = View.VISIBLE
            progress_loading_bottom.visibility = View.INVISIBLE
            reload_bottom.visibility = View.VISIBLE
        }
    }

    override fun onFailure(message: String) {
        btnReloadReady = true
        Coroutines.main {
            root.snackbarError(message)
            shimmer_layout.visibility = View.GONE
            recycle_view_pencarian.visibility = View.VISIBLE
            shimmer_layout.visibility = View.GONE
            shimmer_list.stopShimmer()

            shimmer_layout2.visibility = View.VISIBLE
            progress_loading_bottom.visibility = View.INVISIBLE
            reload_bottom.visibility = View.VISIBLE
        }
    }

    override fun onEmpty() {
        Coroutines.main {
            shimmer_layout.visibility = View.GONE
            shimmer_list.stopShimmer()
            recycle_view_pencarian.visibility = View.INVISIBLE
            text_kosong.visibility = View.VISIBLE
        }
    }

    private fun recycleViewOnBottom() {
        recycle_view_pencarian.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)

                if (!recyclerView.canScrollVertically(1)) {
                    if (page != totalPage) {
                        if (readyToLoad) {
                            readyToLoad = false
                            Coroutines.main {
                                val balihos =
                                    viewModel.getBaliho(
                                        kota!!,
                                        kategori!!,
                                        sortby,
                                        urutan,
                                        tambahan,
                                        page + 1,
                                        false
                                    )
                                balihos.observe(this@PencarianGlobalActivity, Observer {
                                    for (i in it.baliho) {
                                        listBaliho.add(i!!)
                                    }
                                    balihoAdapter.sumitList(listBaliho)
                                    page = it.currentPage!!
                                })
                            }
                        }
                    }
                }
            }
        })
    }

    override fun onResume() {
        super.onResume()
        appContext = this
        SplashScreen.STATE_ACTIVITY = "PencarianGlobalActivity"

        LocalBroadcastManager.getInstance(this)
            .registerReceiver(
                broadCastReceiver,
                IntentFilter(MyFirebaseMessagingService.NOTIF_TRANSAKSI)
            )
    }

    override fun onPause() {
        super.onPause()
        LocalBroadcastManager.getInstance(this)
            .unregisterReceiver(broadCastReceiver)

    }

    companion object {

        var appContext: Context? = null

    }
}

