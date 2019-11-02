package com.genossys.pasangbaliho.ui.detail

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProviders
import androidx.viewpager.widget.ViewPager
import com.genossys.pasangbaliho.R
import com.genossys.pasangbaliho.ui.detail.maps.DetailMapsActivity
import com.genossys.pasangbaliho.ui.detail.maps.StreetWebViewActivity
import com.genossys.pasangbaliho.ui.home.DetailBalihoViewModel
import com.genossys.pasangbaliho.ui.home.DetailBalihoViewModelFactory
import com.genossys.pasangbaliho.ui.transaksi.AjukanPenawaranActivity
import com.genossys.pasangbaliho.utils.Coroutines
import com.genossys.pasangbaliho.utils.ImageSlider
import com.genossys.pasangbaliho.utils.snackbar
import com.viewpagerindicator.CirclePageIndicator
import kotlinx.android.synthetic.main.activity_detail_baliho.*
import kotlinx.android.synthetic.main.shimmer_detail_baliho.*
import org.kodein.di.KodeinAware
import org.kodein.di.android.kodein
import org.kodein.di.generic.instance
import java.text.DecimalFormat
import java.util.*
import kotlin.collections.ArrayList

class DetailBalihoActivity : AppCompatActivity(), DetailListener, KodeinAware {


    override val kodein by kodein()
    private val factory: DetailBalihoViewModelFactory by instance()
    private var idBaliho = 1
    private var imageModelArrayList: ArrayList<ImageSlider>? = null
    private var myImageList = mutableListOf(
        "fotobaliho1.jpg"
    )

    var lat: String? = ""
    var lng: String? = ""
    var streetView: String? = ""
    var alamat: String? = ""

    val dec = DecimalFormat("#,###")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_baliho)

        idBaliho = intent.getIntExtra("id", 1)

        val viewModel = ViewModelProviders.of(this, factory).get(DetailBalihoViewModel::class.java)
        viewModel.detailListener = this
        Coroutines.main {

            val dataBaliho = viewModel.getDetailBaliho(idBaliho)

            dataBaliho.observe(this, androidx.lifecycle.Observer {
                myImageList.clear()
                for (element in it.foto) {
                    myImageList.add(element.urlFoto.toString())
                }
                imageModelArrayList = populateList()
                init()

                text_nama.text = it.baliho?.namaBaliho
                text_alamat.text = it.baliho?.alamat
                text_kota.text = "Kota: " + it.baliho?.kota
                text_provinsi.text = "Provinsi: " + it.baliho?.provinsi

                text_deskripsi.text = it.baliho?.deskripsi

                text_harga.text =
                    "Harga: " + dec.format(it.baliho?.minHarga).toString() + "-" + dec.format(it.baliho?.maxHarga).toString()

                lat = it.baliho?.latitude
                lng = it.baliho?.logitude
                streetView = it.baliho?.url360
                alamat = it.baliho?.alamat + ", " + it.baliho?.kota + ", " + it.baliho?.provinsi


            })
        }

        initButton()

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
        mPager!!.adapter = AdapterImageSlider(this@DetailBalihoActivity, this.imageModelArrayList!!)

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
        val swipeTimer = Timer()
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

    companion object {

        private var mPager: ViewPager? = null
        private var currentPage = 0
        private var NUM_PAGES = 0
    }


    private fun initButton(){
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

        button_ajukan_penawaran.setOnClickListener {
            val i = Intent(this@DetailBalihoActivity, AjukanPenawaranActivity::class.java)
            i.putExtra("gambar", myImageList[0])
            i.putExtra("alamat", alamat)
            i.putExtra("id", idBaliho)
            startActivity(i)
        }
    }
    override fun onStarted() {
        Coroutines.main {
            root_layout.visibility = View.INVISIBLE
            shimmer_detail.visibility = View.VISIBLE
            shimmer_detail.startShimmer()
        }
    }

    override fun onFotoLoaded() {

    }

    override fun onDetailLoaded() {
        Coroutines.main {
            shimmer_detail.visibility = View.GONE
            shimmer_detail.stopShimmer()
            root_layout.visibility = View.VISIBLE
        }
    }

    override fun onSuccess() {

    }

    override fun onFailure(message: String) {
        root_layout.snackbar(message)
    }
}
