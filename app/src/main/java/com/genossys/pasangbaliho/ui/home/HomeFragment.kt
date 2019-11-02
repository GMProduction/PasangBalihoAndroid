package com.genossys.pasangbaliho.ui.home

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.NestedScrollView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.ViewPager
import com.facebook.shimmer.ShimmerFrameLayout
import com.genossys.pasangbaliho.R
import com.genossys.pasangbaliho.data.db.entity.Baliho
import com.genossys.pasangbaliho.ui.detail.AdapterImageSlider
import com.genossys.pasangbaliho.ui.pencarianglobal.PencarianGlobalActivity
import com.genossys.pasangbaliho.utils.Coroutines
import com.genossys.pasangbaliho.utils.ImageSlider
import com.genossys.pasangbaliho.utils.snackbar
import com.viewpagerindicator.CirclePageIndicator
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.fragment_home.view.*
import kotlinx.android.synthetic.main.kategori_layout.view.*
import kotlinx.android.synthetic.main.kota_layout.view.*
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.kodein
import org.kodein.di.generic.instance
import java.util.*
import kotlin.collections.ArrayList


class HomeFragment : Fragment(), HomeListener, KodeinAware {

    override val kodein by kodein()
    lateinit var root: View
    private lateinit var balihoAdapter: AdapterRekomendasiBaliho
    private lateinit var homeViewModel: HomeViewModel
    private var imageModelArrayList: ArrayList<ImageSlider>? = null
    private var myImageList = mutableListOf(
        "fotobaliho1.jpg",
        "fotobaliho2.jpg",
        "fotobaliho3.jpg",
        "fotobaliho4.jpg",
        "fotobaliho5.jpg"
    )

    private var page: Int = 1
    private var totalPage: Int = 0
    private var readyToLoad = true

    private val factory: HomeViewModelFactory by instance()
    var listBaliho: MutableList<Baliho> = mutableListOf()
    private lateinit var shimerRekomendasiBaliho: ShimmerFrameLayout
    private lateinit var recyclerViewRekomendasi: RecyclerView
    private lateinit var scroller: NestedScrollView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        homeViewModel =
            ViewModelProviders.of(this, factory).get(HomeViewModel::class.java)
        homeViewModel.homeListener = this
        root = inflater.inflate(R.layout.fragment_home, container, false)

        shimerRekomendasiBaliho = root.findViewById(R.id.shimmer_rekomendasi)
        recyclerViewRekomendasi = root.findViewById(R.id.recycle_view_rekomendasi)
        scroller = root.findViewById(R.id.nested_home)

        Coroutines.main {
            val balihos = homeViewModel.getBaliho(page, true)
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

        imageModelArrayList = populateList()
        init(activity!!.applicationContext)
        initSortCut()

        recycleViewOnBottom(page)
        return root
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

        root.kategori_banner.setOnClickListener {
            setButton("", "Banner")
        }

        root.kategori_digital_display.setOnClickListener {
            setButton("", "Digital Display")
        }

        root.kategori_wall_branding.setOnClickListener {
            setButton("", "Wall Branding")
        }

        root.kategori_flag.setOnClickListener {
            setButton("", "Flag")
        }

        root.kategori_neon_box.setOnClickListener {
            setButton("", "Neon Box")
        }

        root.kategori_parking_spot.setOnClickListener {
            setButton("", "Parking Spot")
        }

        root.kategori_service.setOnClickListener {
            setButton("", "Service")
        }

        root.kategori_videotron.setOnClickListener {
            setButton("", "Videotron")
        }

        //KOTA
        root.kota_surakarta.setOnClickListener {
            setButton("Surakarta", "")
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
    }

    private fun initRecycleView() {
        recyclerViewRekomendasi.apply {
            layoutManager = LinearLayoutManager(activity?.applicationContext)
            balihoAdapter = AdapterRekomendasiBaliho()
            adapter = balihoAdapter
            isNestedScrollingEnabled = false
        }
    }

    private fun populateList(): ArrayList<ImageSlider> {

        val list = ArrayList<ImageSlider>()

        for (i in myImageList.indices) {
            val imageModel = ImageSlider()
            imageModel.setImageDrawable(myImageList[i])
            list.add(imageModel)
        }

        return list
    }

    private fun init(context: Context) {

        mPager = root.findViewById(R.id.slider_home)
        mPager!!.adapter = AdapterImageSlider(context, this.imageModelArrayList!!)

        val indicator = root.findViewById<CirclePageIndicator>(R.id.indicator)

        indicator.setViewPager(mPager)

        NUM_PAGES = imageModelArrayList!!.size

        // Auto start of viewpager
        val handler = Handler()
        val update = Runnable {
            if (currentPage == NUM_PAGES) {
                currentPage = 0
            }
            mPager!!.setCurrentItem(currentPage++, true)
        }
        val swipeTimer = Timer()
        swipeTimer.schedule(object : TimerTask() {
            override fun run() {
                handler.post(update)
            }
        }, 4000, 4000)

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

    companion object {

        private var mPager: ViewPager? = null
        private var currentPage = 0
        private var NUM_PAGES = 0
    }

    override fun onStarted() {
        Log.d("state", "onstart")

        Coroutines.main {
            shimerRekomendasiBaliho.visibility = View.VISIBLE
            shimerRekomendasiBaliho.startShimmer()
            recycle_view_rekomendasi.visibility = View.INVISIBLE
            card_loading.visibility = View.GONE
        }
    }

    override fun onLoadMore() {
        Coroutines.main {
            card_loading.visibility = View.VISIBLE
        }
    }

    override fun onSuccess() {
        Log.d("state", "success")

    }

    override fun onFailure(message: String) {
        root_layout.snackbar(message)
        Log.d("state", "gagal")
    }

    override fun onTimeOut(s: String) {
        root_layout.snackbar(s)
        Coroutines.main {
            page = 1
            val balihos = homeViewModel.getBaliho(page, true)
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

    override fun onRekomendasiLoaded() {
        Coroutines.main {
            shimerRekomendasiBaliho.stopShimmer()
            shimerRekomendasiBaliho.visibility = View.GONE
            card_loading.visibility = View.GONE
            recycle_view_rekomendasi.visibility = View.VISIBLE
            readyToLoad = true

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
    }

    private fun recycleViewOnBottom(pages: Int) {

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
}