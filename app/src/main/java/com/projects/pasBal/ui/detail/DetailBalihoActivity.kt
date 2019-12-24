package com.projects.pasBal.ui.detail

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.widget.NestedScrollView
import androidx.lifecycle.ViewModelProviders
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.viewpager.widget.ViewPager
import com.google.android.material.button.MaterialButton
import com.projects.pasBal.R
import com.projects.pasBal.ui.adapter.AdapterImageSlider
import com.projects.pasBal.ui.detail.maps.DetailMapsActivity
import com.projects.pasBal.ui.detail.maps.StreetWebViewActivity
import com.projects.pasBal.ui.home.DetailBalihoViewModel
import com.projects.pasBal.ui.home.DetailBalihoViewModelFactory
import com.projects.pasBal.ui.splashScreen.SplashScreen
import com.projects.pasBal.ui.transaksi.AjukanPenawaranActivity
import com.projects.pasBal.utils.*
import com.projects.pasBal.utils.customSnackBar.ChefSnackbar
import com.projects.pasBal.utils.firebaseServices.MyFirebaseMessagingService
import com.viewpagerindicator.CirclePageIndicator
import kotlinx.android.synthetic.main.activity_detail_baliho.*
import kotlinx.android.synthetic.main.loading_mid_layout.*
import kotlinx.android.synthetic.main.shimmer_detail_baliho.*
import org.kodein.di.KodeinAware
import org.kodein.di.android.kodein
import org.kodein.di.generic.instance
import java.text.DecimalFormat
import java.util.*
import kotlin.collections.ArrayList

class DetailBalihoActivity : AppCompatActivity(), DetailListener, KodeinAware, GenosDialog {

    override val kodein by kodein()
    private val factory: DetailBalihoViewModelFactory by instance()
    private var idBaliho = 1
    private var imageModelArrayList: ArrayList<ImageSlider>? = null
    private var myImageList = mutableListOf(
        "fotobaliho1.jpg"
    )

    var swipeTimer = Timer()
    var lat: String? = ""
    var lng: String? = ""
    var streetView: Int = 0
    var alamat: String? = ""

    var viewModel: DetailBalihoViewModel? = null
    val dec = DecimalFormat("#,###")
    private lateinit var btnReload: ImageView
    private lateinit var root: NestedScrollView
    private lateinit var textTglDipesan: TextView
    private lateinit var textOrientasiDanUuran: TextView
    private lateinit var textPosisi: TextView
    private lateinit var textTampilan: TextView
    private lateinit var buttonAjukanPenawaran: MaterialButton
    private lateinit var layoutDipesan: ConstraintLayout

    private val broadCastReceiver = object : BroadcastReceiver() {
        override fun onReceive(contxt: Context?, intent: Intent?) {

            val tittle: String? = intent?.getStringExtra("tittle")
            val body: String? = intent?.getStringExtra("body")

            ChefSnackbar.make(root, R.mipmap.boyolali, tittle!!, body!!).show()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_baliho)

        LocalBroadcastManager.getInstance(this)
            .registerReceiver(
                broadCastReceiver,
                IntentFilter(MyFirebaseMessagingService.NOTIF_TRANSAKSI)
            )

        idBaliho = intent.getIntExtra("id", 1)
        btnReload = findViewById(R.id.reload)

        viewModel = ViewModelProviders.of(this, factory).get(DetailBalihoViewModel::class.java)
        viewModel!!.detailListener = this

        initComponent()
        loadData()
        initButton()

    }

    private fun initComponent() {
        root = findViewById(R.id.root_layout)
        textTglDipesan = findViewById(R.id.text_tanggal_dipesan)
        textPosisi = findViewById(R.id.text_posisi)
        textTampilan = findViewById(R.id.text_tampilan)
        textOrientasiDanUuran = findViewById(R.id.text_orientasi_dan_ukuran)
        layoutDipesan = findViewById(R.id.layout_transaksi)
        buttonAjukanPenawaran = findViewById(R.id.button_ajukan_penawaran)
        layoutDipesan.visibility = View.GONE
    }

    private fun loadData() {
        Coroutines.main {

            viewModel!!.getDetailBaliho(idBaliho).observe(this, androidx.lifecycle.Observer {
                myImageList.clear()

                if (it.foto.isEmpty()) {
                    myImageList.add("noimage.jpg")
                }

                for (i in it.foto) {
                    myImageList.add(i.urlFoto.toString())
                }

                imageModelArrayList = populateList()
                init()
                val harga = "Rp " + toDesimalText(it.baliho?.hargaMarket!!) + "/bulan"
                val kota = "Kota: " + it.baliho.kota
                val provinsi = "Provinsi: " + it.baliho.provinsi
                val orientasi =
                    it.baliho.kategori + " " + it.baliho.orientasi + ", " + it.baliho.lebar + "cm x " + it.baliho.tinggi + "cm"

                text_nama.text = it.baliho.namaBaliho
                text_alamat.text = it.baliho.alamat
                text_kota.text = kota
                text_provinsi.text = provinsi
                textPosisi.text = it.baliho.posisi
                textTampilan.text = it.baliho.tampilan
                textOrientasiDanUuran.text = orientasi
                text_deskripsi.text = it.baliho.deskripsi
                text_harga.text = harga

                lat = it.baliho.latitude
                lng = it.baliho.longitude
                streetView = it.baliho.idBaliho!!
                alamat = it.baliho.alamat + ", " + it.baliho.kota + ", " + it.baliho.provinsi


                textTglDipesan.text = ""
                if (it.transaksi.isNotEmpty()) {
                    layoutDipesan.visibility = View.VISIBLE

                    for (i in it.transaksi) {
                        val textAwal: String = textTglDipesan.text.toString()
                        val tglAwal = tglSystemToView(i.tanggalAwal!!)
                        val tglAkhir = tglSystemToView(i.tanggalAkhir!!)

                        val textNya = "$textAwal\n$tglAwal - $tglAkhir"
                        textTglDipesan.text = textNya
                    }
                }

            })
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

    private fun init() {

        mPager = findViewById(R.id.pager)
        mPager!!.adapter = AdapterImageSlider(
            this@DetailBalihoActivity,
            this.imageModelArrayList!!
        )

        val indicator = findViewById<CirclePageIndicator>(R.id.indicator)

        indicator.setViewPager(mPager)

        val density = resources.displayMetrics.density

        //Set circle indicator radius
        indicator.radius = 3 * density

        NUM_PAGES = imageModelArrayList!!.size

        // Auto start of viewpager
        val handler = Handler()
        val update = Runnable {
            if (currentPage == NUM_PAGES) {
                currentPage = 0
            }
            mPager!!.setCurrentItem(currentPage++, true)
        }

        swipeTimer = Timer()
        swipeTimer.schedule(object : TimerTask() {
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

    private fun initButton() {
        button_map.setOnClickListener {
            val i = Intent(this@DetailBalihoActivity, DetailMapsActivity::class.java)
            i.putExtra("lat", lat)
            i.putExtra("lng", lng)
            i.putExtra("alamat", alamat)
            startActivity(i)
        }


        button_360.setOnClickListener {
            val i = Intent(this@DetailBalihoActivity, StreetWebViewActivity::class.java)
            i.putExtra("streetView", streetView)
            i.putExtra("alamat", alamat)
            startActivity(i)
        }


        button_back.setOnClickListener {
            finish()
        }

        btnReload.setOnClickListener {
            loadData()
        }

        buttonAjukanPenawaran.setOnClickListener {
            val i = Intent(this@DetailBalihoActivity, AjukanPenawaranActivity::class.java)
            i.putExtra("gambar", myImageList[0])
            i.putExtra("alamat", alamat)
            i.putExtra("id", idBaliho)
            startActivity(i)
        }
    }

    override fun onStarted() {
        Coroutines.main {
            buttonAjukanPenawaran.visibility = View.GONE
            root_layout.visibility = View.INVISIBLE
            shimmer_detail.visibility = View.VISIBLE
            shimmer_detail.startShimmer()
            card_loading.visibility = View.GONE
        }
    }

    override fun onFotoLoaded() {

    }

    override fun onDetailLoaded() {
        Coroutines.main {
            buttonAjukanPenawaran.visibility = View.VISIBLE
            shimmer_detail.visibility = View.GONE
            shimmer_detail.stopShimmer()
            root_layout.visibility = View.VISIBLE
            card_loading.visibility = View.GONE
        }
    }

    override fun onSuccess() {

    }

    override fun onTimeOut(message: String) {
        Coroutines.main {
            shimmer_detail.visibility = View.GONE
            shimmer_detail.stopShimmer()
            root_layout.visibility = View.VISIBLE
            card_loading.visibility = View.VISIBLE
            progress_loading_mid.visibility = View.GONE

            if (message == ERROR_INTERNET) {
                dialogHandler(
                    this,
                    "Internet Error",
                    ERROR_INTERNET,
                    R.drawable.ic_nointernet,
                    "pesan"
                )
            }
        }
    }

    override fun onFailure(message: String) {
        Coroutines.main {
            shimmer_detail.visibility = View.GONE
            shimmer_detail.stopShimmer()
            root_layout.visibility = View.VISIBLE
            card_loading.visibility = View.VISIBLE
            progress_loading_mid.visibility = View.GONE

            if (message == ERROR_INTERNET) {
                dialogHandler(
                    this,
                    "Internet Error",
                    ERROR_INTERNET,
                    R.drawable.ic_nointernet,
                    "pesan"
                )
            } else {
                dialogHandler(this, "Error", ERROR_API, R.drawable.ic_nointernet, "pesan")
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        swipeTimer.cancel()
    }

    override fun onResume() {
        super.onResume()
        appContext = this
        SplashScreen.STATE_ACTIVITY = "DetailBalihoActivity"

    }

    override fun onPause() {
        super.onPause()
        LocalBroadcastManager.getInstance(this)
            .unregisterReceiver(broadCastReceiver)

    }

    companion object {

        private var mPager: ViewPager? = null
        private var currentPage = 0
        private var NUM_PAGES = 0

        var appContext: Context? = null

    }

    override fun action() {

    }
}
