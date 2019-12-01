package com.genossys.pasangbaliho.ui.home

import android.content.*
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.widget.NestedScrollView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.ViewPager
import com.facebook.shimmer.ShimmerFrameLayout
import com.genossys.pasangbaliho.R
import com.genossys.pasangbaliho.data.db.entity.Baliho
import com.genossys.pasangbaliho.data.db.entity.Slider
import com.genossys.pasangbaliho.ui.adapter.AdapterBaliho
import com.genossys.pasangbaliho.ui.adapter.AdapterSlider
import com.genossys.pasangbaliho.ui.pencarianglobal.PencarianGlobalActivity
import com.genossys.pasangbaliho.ui.splashScreen.SplashScreen
import com.genossys.pasangbaliho.ui.splashScreen.SplashScreen.Companion.ID_ADVERTISER
import com.genossys.pasangbaliho.ui.transaksi.menuTransaksi.MenuTransaksi
import com.genossys.pasangbaliho.utils.Coroutines
import com.genossys.pasangbaliho.utils.customSnackBar.ChefSnackbar
import com.genossys.pasangbaliho.utils.firebaseServices.MyFirebaseMessagingService.Companion.NOTIF_TRANSAKSI
import com.genossys.pasangbaliho.utils.snackbar
import com.viewpagerindicator.CirclePageIndicator
import com.wang.avi.AVLoadingIndicatorView
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.fragment_home.view.*
import kotlinx.android.synthetic.main.kategori_layout.view.*
import kotlinx.android.synthetic.main.kota_layout.view.*
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.kodein
import org.kodein.di.generic.instance
import java.util.*


class HomeFragment : HomeListener, KodeinAware, Fragment() {


    override val kodein by kodein()
    private val factory: HomeViewModelFactory by instance()
    private lateinit var balihoAdapter: AdapterBaliho
    private lateinit var sliderAdapter: AdapterSlider
    private lateinit var homeViewModel: HomeViewModel
    private lateinit var root: View
    var idPref: SharedPreferences? = null

    var swiperTimer = Timer()
    private var page: Int = 1
    private var totalPage: Int = 0
    private var readyToLoad = false
    private var btnReloadReady = false
    private var isLoadAwalOk = false
    private var idAdvertiser = 0

    private var countNewTransaksi = 0

    var listBaliho: MutableList<Baliho> = mutableListOf()
    private var sliderList: MutableList<Slider> = mutableListOf()

    //Component
    private lateinit var shimerRekomendasiBaliho: ShimmerFrameLayout
    private lateinit var recyclerViewRekomendasi: RecyclerView
    private lateinit var scroller: NestedScrollView
    private lateinit var btnReload: ImageView
    private lateinit var cardLoading: CardView
    private lateinit var loadingMid: AVLoadingIndicatorView
    private lateinit var circleIndicator: CirclePageIndicator
    private lateinit var reload: ImageView
    private lateinit var badgeNotif: TextView

    private val broadCastReceiver = object : BroadcastReceiver() {
        override fun onReceive(contxt: Context?, intent: Intent?) {

            val tittle: String? = intent?.getStringExtra("tittle")
            val body: String? = intent?.getStringExtra("body")

            ChefSnackbar.make(root, R.mipmap.boyolali, tittle!!, body!!).show()
            getCountNewTransaksi()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        //ViewModel
        homeViewModel =
            ViewModelProviders.of(this, factory).get(HomeViewModel::class.java)
        homeViewModel.homeListener = this

        root = inflater.inflate(R.layout.fragment_home, container, false)


        initComponent()
        loadDataAwal()
        initSortCut()
        recycleViewOnBottom()
        return root
    }


    fun getCountNewTransaksi() {

        Coroutines.main {
            homeViewModel.getCountNewTransaksi(idAdvertiser).observe(this, Observer {

                countNewTransaksi = it.count!!
                if (countNewTransaksi > 0) {
                    badgeNotif.text = countNewTransaksi.toString()
                    badgeNotif.visibility = View.VISIBLE
                } else {
                    badgeNotif.visibility = View.GONE
                }

            })
        }
    }

    fun loadDataAwal() {
        Coroutines.main {
            homeViewModel.getSlider().observe(this, Observer {
                sliderList.clear()



                for (i in it.slider) {
                    sliderList.add(i)
                }

                initSlider(activity!!.applicationContext, sliderList)

            })


            homeViewModel.getBaliho(page, true).observe(this, Observer {
                listBaliho.clear()
                initRecycleView()

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


    private fun initComponent() {

        idPref = androidx.preference.PreferenceManager
            .getDefaultSharedPreferences(activity?.applicationContext)

        setPref()

        shimerRekomendasiBaliho = root.findViewById(R.id.shimmer_rekomendasi)
        recyclerViewRekomendasi = root.findViewById(R.id.recycle_view_rekomendasi)
        btnReload = root.findViewById(R.id.reload)
        cardLoading = root.findViewById(R.id.card_loading)
        loadingMid = root.findViewById(R.id.progress_loading_mid)
        reload = root.findViewById(R.id.reload)
        circleIndicator = root.findViewById(R.id.indicator)
        badgeNotif = root.findViewById(R.id.badge_transaksi)
        circleIndicator.visibility = View.GONE
        scroller = root.findViewById(R.id.nested_home)
        scroller.visibility = View.GONE

        badgeNotif.visibility = View.GONE
    }



    private fun initSortCut() {
        //PENCARIAN UTAMA
        root.global_pencarian.setOnClickListener {
            setButton("", "")
        }

        //KATEGORI
        root.kategori_billboard.setOnClickListener {
            setButton("", "Billboard")
        }



//        root.kategori_wall_branding.setOnClickListener {
//            setButton("", "Wall Branding")
//        }

//        root.kategori_flag.setOnClickListener {
//            setButton("", "Flag")
//        }

        root.kategori_neon_box.setOnClickListener {
            setButton("", "Neon Box")
        }

        root.kategori_parking_spot.setOnClickListener {
            setButton("", "Parking Spot")
        }


        root.kategori_videotron.setOnClickListener {
            setButton("", "Videotron")
        }

        root.kategori_website.setOnClickListener {
            setButton("", "Website")
        }

        //KOTA
        root.kota_surakarta.setOnClickListener {
            setButton("Surakarta", "")
        }

        root.kota_karanganyar.setOnClickListener {
            setButton("Karanganyar", "")
        }

        root.kota_boyolali.setOnClickListener {
            setButton("Boyolali", "")
        }

        root.kota_klaten.setOnClickListener {
            setButton("Klaten", "")
        }

        root.kota_semarang.setOnClickListener {
            setButton("Semarang", "")
        }

        root.kota_sukoharjo.setOnClickListener {
            setButton("Sukoharjo", "")
        }

        root.kota_wonogiri.setOnClickListener {
            setButton("Wonogiri", "")
        }

        root.kota_sragen.setOnClickListener {
            setButton("Sragen", "")
        }

        root.btn_pesanan.setOnClickListener {
            val i = Intent(activity, MenuTransaksi::class.java)
            startActivity(i)
        }


        btnReload.setOnClickListener {
            if (btnReloadReady) {
                if (isLoadAwalOk) {
                    Coroutines.main {
                        val balihos = homeViewModel.getBaliho(page + 1, false)
                        balihos.observe(this, Observer {
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
                } else {
                    Coroutines.main {
                        page = 1
                        val balihos = homeViewModel.getBaliho(page, false)
                        balihos.observe(this, Observer {
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
        }
    }

    private fun initRecycleView() {
        recyclerViewRekomendasi.apply {
            layoutManager = LinearLayoutManager(activity?.applicationContext)
            balihoAdapter = AdapterBaliho()
            adapter = balihoAdapter
            isNestedScrollingEnabled = false
        }
    }

    private fun initSlider(context: Context, sliderList: List<Slider>) {

        sliderAdapter = AdapterSlider(context, sliderList)
        mPager = root.findViewById(R.id.slider_home)
        mPager!!.adapter = sliderAdapter

        val indicator = root.findViewById<CirclePageIndicator>(R.id.indicator)

        indicator.setViewPager(mPager)

        NUM_PAGES = sliderList.size

        // Auto start of viewpager
        val handler = Handler()
        val update = Runnable {
            if (currentPage == NUM_PAGES) {
                currentPage = 0
            }
            mPager!!.setCurrentItem(currentPage++, true)
        }

        swiperTimer = Timer()
        swiperTimer.schedule(object : TimerTask() {
            override fun run() {
                handler.post(update)
            }
        }, 6000, 6000)

        // Pager listener over indicator
        indicator.setOnPageChangeListener(object : ViewPager.OnPageChangeListener {

            override fun onPageSelected(position: Int) {
                currentPage = position

            }

            override fun onPageScrolled(pos: Int, arg1: Float, arg2: Int) {

            }

            override fun onPageScrollStateChanged(pos: Int) {

            }
        })

    }

    override fun onStarted() {
        Log.d("state", "onstart")

        Coroutines.main {
            shimerRekomendasiBaliho.visibility = View.VISIBLE
            shimerRekomendasiBaliho.startShimmer()
            recyclerViewRekomendasi.visibility = View.INVISIBLE
            cardLoading.visibility = View.GONE
        }
    }

    override fun onLoadMore() {
        btnReloadReady = false
        Coroutines.main {
            cardLoading.visibility = View.VISIBLE
            loadingMid.visibility = View.VISIBLE
            reload.visibility = View.GONE
        }
    }

    override fun onSuccess() {
        Log.d("state", "success")

    }

    override fun onFailure(message: String) {
        root_layout.snackbar(message)
        btnReloadReady = true
        Coroutines.main {
            shimerRekomendasiBaliho.stopShimmer()
            shimerRekomendasiBaliho.visibility = View.GONE
            cardLoading.visibility = View.VISIBLE
            loadingMid.visibility = View.GONE
            reload.visibility = View.VISIBLE
            btnReloadReady = true
            scroller.visibility = View.VISIBLE
        }
    }

    override fun onTimeOut(s: String) {
        btnReloadReady = true
        Coroutines.main {
            shimerRekomendasiBaliho.stopShimmer()
            shimerRekomendasiBaliho.visibility = View.GONE
            cardLoading.visibility = View.VISIBLE
            loadingMid.visibility = View.GONE
            reload.visibility = View.VISIBLE
            scroller.visibility = View.VISIBLE

        }
    }

    override fun onRekomendasiLoaded() {
        Coroutines.main {
            shimerRekomendasiBaliho.stopShimmer()
            shimerRekomendasiBaliho.visibility = View.GONE
            cardLoading.visibility = View.GONE
            loadingMid.visibility = View.VISIBLE
            reload.visibility = View.GONE
            recyclerViewRekomendasi.visibility = View.VISIBLE
            readyToLoad = true
            scroller.visibility = View.VISIBLE

        }
    }

    private fun setButton(kota: String, kategori: String) {
        val i = Intent(activity, PencarianGlobalActivity::class.java)
        i.putExtra("kota", kota)
        i.putExtra("kategori", kategori)
        startActivity(i)
    }

    override fun onPause() {
        super.onPause()
        homeViewModel.job.cancel()
        swiperTimer.cancel()

        LocalBroadcastManager.getInstance(activity?.applicationContext!!)
            .unregisterReceiver(broadCastReceiver)

    }

    override fun onResume() {
        super.onResume()
        appContext = this.activity!!
        SplashScreen.STATE_ACTIVITY = "HomeFragment"

        LocalBroadcastManager.getInstance(activity?.applicationContext!!)
            .registerReceiver(broadCastReceiver, IntentFilter(NOTIF_TRANSAKSI))

        getCountNewTransaksi()

    }

    private fun recycleViewOnBottom() {

        scroller.setOnScrollChangeListener(NestedScrollView.OnScrollChangeListener { v, scrollX, scrollY, oldScrollX, oldScrollY ->

            if (scrollY == v.getChildAt(0).measuredHeight - v.measuredHeight) {
                if (page != totalPage) {
                    if (readyToLoad) {
                        readyToLoad = false
                        Coroutines.main {
                            val balihos =
                                homeViewModel.getBaliho(
                                    page + 1,
                                    false
                                )
                            balihos.observe(this@HomeFragment, Observer {
                                initRecycleView()
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
        })
    }

    private fun setPref() {
        Coroutines.main {
            homeViewModel.getLoggedInAdvertiser().observe(this, Observer {
                try {
                    idPref!!.edit().putInt(ID_ADVERTISER, it.id!!).apply()
                    idPref!!.edit().putString(SplashScreen.API_TOKEN, it.apiToken!!).apply()
                } catch (e: NullPointerException) {
                    idPref!!.edit().putInt(ID_ADVERTISER, 0).apply()
                    idPref!!.edit().putString(SplashScreen.API_TOKEN, "").apply()
                }
            })
            idAdvertiser = idPref?.getInt(ID_ADVERTISER, 0)!!
        }
    }


    companion object {

        private var mPager: ViewPager? = null
        private var currentPage = 0
        private var NUM_PAGES = 0

        var appContext: Context? = null


    }
}