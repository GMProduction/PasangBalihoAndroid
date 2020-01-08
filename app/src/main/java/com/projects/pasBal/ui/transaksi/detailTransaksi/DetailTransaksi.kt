package com.projects.pasBal.ui.transaksi.detailTransaksi

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.widget.NestedScrollView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.android.material.button.MaterialButton
import com.projects.pasBal.R
import com.projects.pasBal.data.network.api.URL_FOTO
import com.projects.pasBal.ui.detail.DetailBalihoActivity
import com.projects.pasBal.ui.home.DetailTransaksiViewModelFactory
import com.projects.pasBal.ui.splashScreen.SplashScreen
import com.projects.pasBal.utils.*
import com.projects.pasBal.utils.customSnackBar.ChefSnackbar
import com.projects.pasBal.utils.firebaseServices.MyFirebaseMessagingService
import com.wang.avi.AVLoadingIndicatorView
import kotlinx.android.synthetic.main.activity_detail_transaksi.*
import org.kodein.di.KodeinAware
import org.kodein.di.android.kodein
import org.kodein.di.generic.instance

class DetailTransaksi : AppCompatActivity(), CommonListener, KodeinAware, GenosDialog {


    override val kodein by kodein()
    private val factory: DetailTransaksiViewModelFactory by instance()
    lateinit var viewModel: DetailTransaksiViewModel

    private var idTransaksi = 0
    private var idBaliho = 0
    private var hargaDitawarkan = 0
    private var hargaSetujui = 0
    private var advToken: String = ""
    private var keteranganBatal: String? = ""
    private var idAdv: Int = 0
    private var readyToReload = false


    private lateinit var textInfo: TextView
    private lateinit var textHarga: TextView
    private lateinit var textHargaYangDitawar: TextView
    private lateinit var textStatusPermintaan: TextView
    private lateinit var textStatusNegoMateri: TextView
    private lateinit var textStatusPembayaran: TextView
    private lateinit var textStatusSelesai: TextView
    private lateinit var btnChat: MaterialButton
    private lateinit var btnEmail: MaterialButton
    private lateinit var btnBayarSekarang: MaterialButton
    private lateinit var layoutNegoMateri: ConstraintLayout
    private lateinit var layoutPembayaran: ConstraintLayout
    private lateinit var layoutSelesai: ConstraintLayout
    private lateinit var rootLayout: NestedScrollView
    private lateinit var loadingProgress: AVLoadingIndicatorView
    private lateinit var layotInfo: ConstraintLayout
    private lateinit var img1: ImageView
    private lateinit var img2: ImageView
    private lateinit var img3: ImageView
    private lateinit var img4: ImageView
    private lateinit var img5: ImageView
    private lateinit var imageTransaksi: ImageView
    private lateinit var btnReload: ImageView

    private val broadCastReceiver = object : BroadcastReceiver() {
        override fun onReceive(contxt: Context?, intent: Intent?) {

            val tittle: String? = intent?.getStringExtra("tittle")
            val body: String? = intent?.getStringExtra("body")

            ChefSnackbar.make(rootLayout, R.mipmap.boyolali, tittle!!, body!!).show()
            loadData()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_transaksi)

        idTransaksi = intent.getIntExtra("id", 0)

        viewModel = ViewModelProviders.of(this, factory).get(DetailTransaksiViewModel::class.java)
        viewModel.listener = this

        LocalBroadcastManager.getInstance(this)
            .registerReceiver(
                broadCastReceiver,
                IntentFilter(MyFirebaseMessagingService.NOTIF_TRANSAKSI)
            )

        appContext = this
        cekActive = true

        initComponent()
        getDataUser()
        loadData()
        initButton()
    }

    private fun getDataUser() {
        Coroutines.main {
            viewModel.getUser().observe(this@DetailTransaksi, Observer {
                idAdv = it.id!!
                advToken = it.apiToken!!
            })
        }
    }

    private fun initComponent() {
        textInfo = findViewById(R.id.text_info)
        textHarga = findViewById(R.id.text_harga)
        textStatusNegoMateri = findViewById(R.id.status_nego_materi)
        textStatusPembayaran = findViewById(R.id.status_pembayaran)
        textStatusPermintaan = findViewById(R.id.status_permintaan)
        textStatusSelesai = findViewById(R.id.status_selesai)
        btnChat = findViewById(R.id.btn_chat)
        btnEmail = findViewById(R.id.btn_email)
        btnBayarSekarang = findViewById(R.id.btn_bayarsekarang)
        layoutNegoMateri = findViewById(R.id.layout_nego_materi)
        layoutPembayaran = findViewById(R.id.layout_pembayaran)
        layoutSelesai = findViewById(R.id.layout_selesai)
        imageTransaksi = findViewById(R.id.image_transaksi)
        rootLayout = findViewById(R.id.root_layout)
        loadingProgress = findViewById(R.id.progress_loading)
        layotInfo = findViewById(R.id.layout_info)
        rootLayout = findViewById(R.id.root_layout)
        img1 = findViewById(R.id.img_1)
        img2 = findViewById(R.id.img_2)
        img3 = findViewById(R.id.img_3)
        img4 = findViewById(R.id.img_4)
        img5 = findViewById(R.id.img_5)
        btnReload = findViewById(R.id.reload)
        textHargaYangDitawar = findViewById(R.id.text_harga_yang_ditawar)

        btnReload.visibility = View.GONE
        btnBayarSekarang.visibility = View.GONE
    }

    private fun initButton() {
        button_back.setOnClickListener {
            finish()
        }


        btnChat.setOnClickListener {
            gotoWhatsapp(getString(R.string.text_chat_negomateri), idTransaksi.toString())
        }

        btnEmail.setOnClickListener {
            gotoGmail(getString(R.string.text_email_negomateri), idTransaksi.toString())
        }

        btnReload.setOnClickListener {
            if (readyToReload) {
                loadData()
            }
        }

        imageTransaksi.setOnClickListener {
            val i = Intent(this, DetailBalihoActivity::class.java)
            i.putExtra("id", idBaliho)
            this.startActivity(i)
        }

        
    }

    private fun loadData() {
        readyToReload = false
        Coroutines.main {

            viewModel.getDetailTransaksi(idTransaksi).observe(this, Observer {
                val requestOptions = RequestOptions()
                    .placeholder(R.color.backdrop)
                    .error(R.color.backdrop)

                Glide.with(this)
                    .applyDefaultRequestOptions(requestOptions)
                    .load(URL_FOTO + it.transaksi!!.urlFoto)
                    .into(image_transaksi)

                val tglAwal = tglSystemToView(it.transaksi.tanggalAwal!!)
                val tglAkhir = tglSystemToView(it.transaksi.tanggalAkhir!!)

                idBaliho = it.transaksi.idBaliho!!
                keteranganBatal = it.transaksi.keteranganBatal
                text_tanggal_awal.text = tglAwal
                text_tanggal_akhir.text = tglAkhir
                val status = it.transaksi.status

                when {
                    status.equals("permintaan") -> statePermintaan()
                    status.equals("negoharga") -> {
                        stateNegoHarga()
                    }
                    status.equals("negomateri") -> {
                        hargaSetujui = it.transaksi.hargaDeal!!
                        stateNegoMateri()
                    }
                    status.equals("pembayaran") -> {
                        hargaSetujui = it.transaksi.hargaDeal!!
                        statePembayaran()
                    }
                    status.equals("selesai") -> {
                        hargaSetujui = it.transaksi.hargaDeal!!
                        stateSelesai()
                    }
                    status.equals("batal") -> {
                        stateBatal()
                    }
                }
            })
        }
    }

    private fun terimapenawaran() {
        Coroutines.main {
            viewModel.postSetujuiHarga(idAdv, advToken, idTransaksi).observe(this, Observer {
                toast(it.message!!)
                loadData()
            })
        }
    }

    private fun statePermintaan() {
        textInfo.text = "Tunggu penawaran harga yang akan di kirim dari admin, sabar ya....."
        textStatusPermintaan.text = "status: Menunggu harga dari admin"
        layoutNegoMateri.visibility = View.GONE
        layoutPembayaran.visibility = View.GONE
        layoutSelesai.visibility = View.GONE
        btnBayarSekarang.visibility = View.GONE
    }

    private fun stateNegoHarga() {
        btnBayarSekarang.visibility = View.GONE
        img1.setImageResource(R.drawable.cb_success)
        img2.setImageResource(R.drawable.cb_proses)
        textStatusPermintaan.text = "status: Menunggu kesepakatan harga"
        textInfo.text = "Menunggu kesepakatan harga..."
        layoutNegoMateri.visibility = View.GONE
        layoutPembayaran.visibility = View.GONE
        layoutSelesai.visibility = View.GONE
    }


    private fun stateNegoMateri() {
        img1.setImageResource(R.drawable.cb_success)
        img2.setImageResource(R.drawable.cb_success)
        img3.setImageResource(R.drawable.cb_success)
        img4.setImageResource(R.drawable.cb_proses)
        textStatusPermintaan.visibility = View.GONE
        textInfo.text = "Kirim materi iklan anda ke admin via email / chat ya "
        textStatusNegoMateri.text = "status: Dalam Proses"
        layoutNegoMateri.visibility = View.VISIBLE
        layoutPembayaran.visibility = View.VISIBLE
        btnBayarSekarang.visibility = View.VISIBLE
        textStatusPembayaran.visibility = View.VISIBLE
        textStatusPembayaran.text = "Status: Dalam proses"
        layoutSelesai.visibility = View.GONE
        btnBayarSekarang.visibility = View.GONE
        textHarga.text = "Rp " + toDesimalText(hargaSetujui)
    }

    private fun statePembayaran() {
        img1.setImageResource(R.drawable.cb_success)
        img2.setImageResource(R.drawable.cb_success)
        img3.setImageResource(R.drawable.cb_proses)
        textStatusPermintaan.visibility = View.GONE
        textInfo.text = "Tinggal langkah pembayaran sebelum iklanya terbit "
        textStatusPembayaran.text = "status: Selesai"
        layoutNegoMateri.visibility = View.GONE
        layoutPembayaran.visibility = View.VISIBLE
        layoutSelesai.visibility = View.GONE
        btnBayarSekarang.visibility = View.VISIBLE
        textHarga.text = "Rp " + toDesimalText(hargaSetujui)
    }

    private fun stateSelesai() {
        img1.setImageResource(R.drawable.cb_success)
        img2.setImageResource(R.drawable.cb_success)
        img3.setImageResource(R.drawable.cb_success)
        img4.setImageResource(R.drawable.cb_success)
        img5.setImageResource(R.drawable.cb_success)
        textStatusPermintaan.visibility = View.GONE
        textInfo.text =
            "Iklanya sudah di pasang ya, Terima kasih sudah menggunakan jasa pasang baliho..."
        textStatusNegoMateri.text = "status: Materi sudah di setujui"
        textStatusPembayaran.text = "status: Pembayaran selesai"
        textStatusSelesai.text = "status: Iklan sudah di pasang"
        layoutNegoMateri.visibility = View.VISIBLE
        layoutPembayaran.visibility = View.VISIBLE
        layoutSelesai.visibility = View.VISIBLE
        btnBayarSekarang.visibility = View.GONE
        textHarga.text = "Rp " + toDesimalText(hargaSetujui)
    }

    private fun stateBatal() {
        img1.setImageResource(R.drawable.cb_gagal)
        img2.setImageResource(R.drawable.cb_gagal)
        img3.setImageResource(R.drawable.cb_gagal)
        img4.setImageResource(R.drawable.cb_gagal)
        img5.setImageResource(R.drawable.cb_gagal)

        textStatusPermintaan.visibility = View.GONE
        textInfo.text =
            keteranganBatal
        layoutNegoMateri.visibility = View.GONE
        layoutPembayaran.visibility = View.GONE
        layoutSelesai.visibility = View.GONE
    }


    override fun onStarted() {
    }

    override fun onSuccess() {
    }

    override fun onStartJob() {
        loadingProgress.visibility = View.VISIBLE
        rootLayout.visibility = View.GONE
        layotInfo.visibility = View.GONE
        btnReload.visibility = View.GONE
    }

    override fun onSuccessJob() {
        loadingProgress.visibility = View.GONE
        rootLayout.visibility = View.VISIBLE
        layotInfo.visibility = View.VISIBLE
    }

    override fun onEmpty() {
    }

    override fun onTimeOut(message: String) {
        readyToReload = true
        Coroutines.main {
            btnReload.visibility = View.VISIBLE
            loadingProgress.visibility = View.GONE
            rootLayout.visibility = View.GONE
            layotInfo.visibility = View.GONE

            dialogHandler(
                this,
                "Internet Error",
                ERROR_INTERNET,
                R.drawable.ic_nointernet,
                "pesan"
            )
        }
    }

    override fun onFailure(message: String) {
        readyToReload = true
        Coroutines.main {
            btnReload.visibility = View.VISIBLE
            loadingProgress.visibility = View.GONE
            rootLayout.visibility = View.GONE
            layotInfo.visibility = View.GONE

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
        LocalBroadcastManager.getInstance(this)
            .unregisterReceiver(broadCastReceiver)
        cekActive = false
    }

    override fun onResume() {
        super.onResume()
        SplashScreen.STATE_ACTIVITY == "DetailTransaksi"
    }

    companion object {
        var appContext: Context? = null
        var cekActive: Boolean = false
    }

    override fun action() {

    }
}
