package com.projects.pasBal.ui.transaksi.menuTransaksi


import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.projects.pasBal.R
import com.projects.pasBal.data.db.entity.Transaksi
import com.projects.pasBal.ui.MainActivity
import com.projects.pasBal.ui.adapter.AdapterTransaksi
import com.projects.pasBal.ui.home.MenuTransaksiViewModelFactory
import com.projects.pasBal.ui.splashScreen.SplashScreen
import com.projects.pasBal.utils.*
import com.wang.avi.AVLoadingIndicatorView
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.kodein
import org.kodein.di.generic.instance

/**
 * A simple [Fragment] subclass.
 */
class MenuTransaksiFragment(private val isi: String) : Fragment(), KodeinAware, CommonListener,
    GenosDialog {


    override val kodein by kodein()
    lateinit var root: View
    private lateinit var viewModel: MenuTransaksiViewModel
    private val factory: MenuTransaksiViewModelFactory by instance()
    private lateinit var transaksiAdapter: AdapterTransaksi
    var idPref: SharedPreferences? = null

    var listTransaksi: MutableList<Transaksi> = mutableListOf()
    private var page: Int = 1
    private var totalPage: Int = 0
    private var readyToLoad = true
    private var isAwal = true
    private var btnReloadReady = false

    private lateinit var recycleViewTransaksi: RecyclerView
    private lateinit var btnBack: ImageView
    private lateinit var btnReload: ImageView
    private lateinit var cardLoading: CardView
    private lateinit var layoutKosong: ConstraintLayout
    private lateinit var progressLoading: AVLoadingIndicatorView


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        viewModel =
            ViewModelProviders.of(this, factory).get(MenuTransaksiViewModel::class.java)
        viewModel.listener = this
        root = inflater.inflate(R.layout.fragment_menu_transaksi, container, false)

        appContext = this.activity!!

        initComponent()
        initButton()
        recycleViewOnBottom()

        return root
    }

    override fun onResume() {
        super.onResume()
        loadDataAwal()
        SplashScreen.STATE_ACTIVITY = "MenuTransaksi"
        cekActive = true
    }

    private fun initComponent() {
        idPref = androidx.preference.PreferenceManager
            .getDefaultSharedPreferences(activity?.applicationContext)

        recycleViewTransaksi = root.findViewById(R.id.recycle_view_menu_transaksi)
        cardLoading = root.findViewById(R.id.card_loading)
        layoutKosong = root.findViewById(R.id.layout_kosong)
        btnReload = root.findViewById(R.id.reload)
        progressLoading = root.findViewById(R.id.progress_loading_mid)
        btnBack = activity!!.findViewById(R.id.button_back)

        btnReload.visibility = View.GONE
        recycleViewTransaksi.visibility = View.GONE
        layoutKosong.visibility = View.GONE

    }


    private fun initButton() {
        btnBack.setOnClickListener { btnback ->
            Intent(activity, MainActivity::class.java).also {
                it.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(it)
            }
        }

        btnReload.setOnClickListener {
            if (isAwal) {
                loadDataAwal()
            } else {
                loadDataNext()
            }
        }
    }


    private fun initRecycleView() {
        recycleViewTransaksi.apply {
            layoutManager = LinearLayoutManager(activity?.applicationContext)
            transaksiAdapter = AdapterTransaksi()
            adapter = transaksiAdapter
            isNestedScrollingEnabled = false
        }
    }

    private fun recycleViewOnBottom() {
        recycleViewTransaksi.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)

                if (!recyclerView.canScrollVertically(1)) {
                    if (page != totalPage) {
                        if (readyToLoad) {
                            loadDataNext()
                        }
                    }
                }
            }
        })
    }

    private fun loadDataAwal() {

        if (idPref?.getInt(SplashScreen.ID_ADVERTISER, 0)!! != 0) {
            try {
                Coroutines.main {
                    viewModel.getDataTransaksi(
                        idPref?.getInt(SplashScreen.ID_ADVERTISER, 0)!!,
                        isi,
                        page
                    )
                        .observe(this@MenuTransaksiFragment, Observer {
                            listTransaksi.clear()
                            initRecycleView()

                            for (i in it.transaksi) {
                                listTransaksi.add(i!!)
                            }

                            transaksiAdapter.sumitList(listTransaksi)
                            page = it.currentPage!!
                            totalPage = it.lastPage!!
                            isAwal = false
                        })
                }
            } catch (e: NullPointerException) {
                activity?.applicationContext!!.gotoLoginFormBelumLogin()
            }
        } else {
            activity?.applicationContext!!.gotoLoginFormBelumLogin()
        }
    }

    private fun loadDataNext() {
        readyToLoad = false
        Coroutines.main {
            viewModel.getDataTransaksi(
                idPref?.getInt(SplashScreen.ID_ADVERTISER, 0)!!,
                isi,
                page + 1
            )
                .observe(this@MenuTransaksiFragment, Observer {

                    for (i in it.transaksi) {
                        listTransaksi.add(i!!)
                    }

                    transaksiAdapter.sumitList(listTransaksi)
                    page = it.currentPage!!
                    totalPage = it.lastPage!!

                })
        }
    }

    override fun onPause() {
        super.onPause()
        viewModel.job?.cancel()
        page = 1
        cekActive = false
    }

    override fun onDestroy() {
        super.onDestroy()
        cekActive = false
    }

    override fun onStarted() {

    }

    override fun onSuccess() {

    }

    override fun onStartJob() {
        Coroutines.main {
            cardLoading.visibility = View.VISIBLE
            progressLoading.visibility = View.VISIBLE
            layoutKosong.visibility = View.GONE
            btnReload.visibility = View.GONE
        }
    }

    override fun onSuccessJob() {
        readyToLoad = true
        Coroutines.main {
            recycleViewTransaksi.visibility = View.VISIBLE
            cardLoading.visibility = View.GONE
            layoutKosong.visibility = View.GONE
        }
    }

    override fun onTimeOut(message: String) {
        btnReloadReady = true
        Coroutines.main {
            cardLoading.visibility = View.VISIBLE
            btnReload.visibility = View.VISIBLE
            progressLoading.visibility = View.GONE
            layoutKosong.visibility = View.GONE

            dialogHandler(
                activity!!,
                "Internet Error",
                ERROR_INTERNET,
                R.drawable.ic_nointernet,
                "pesan"
            )
        }
    }

    override fun onFailure(message: String) {
        btnReloadReady = true
        Coroutines.main {
            cardLoading.visibility = View.VISIBLE
            btnReload.visibility = View.VISIBLE
            progressLoading.visibility = View.GONE
            layoutKosong.visibility = View.GONE

            if (message == ERROR_INTERNET) {
                dialogHandler(
                    activity!!,
                    "Internet Error",
                    ERROR_INTERNET,
                    R.drawable.ic_nointernet,
                    "pesan"
                )
            } else {
                dialogHandler(activity!!, "Error", ERROR_API, R.drawable.ic_nointernet, "pesan")
            }
        }
    }

    override fun onEmpty() {
        Coroutines.main {
            recycleViewTransaksi.visibility = View.GONE
            cardLoading.visibility = View.GONE
            layoutKosong.visibility = View.VISIBLE
        }
    }


    companion object {
        var appContext: Context? = null
        var cekActive: Boolean = false
    }

    override fun action() {

    }
}
