package com.projects.pasBal.ui.transaksi

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProviders
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.orhanobut.dialogplus.DialogPlus
import com.orhanobut.dialogplus.ViewHolder
import com.projects.pasBal.R
import com.projects.pasBal.data.network.api.URL_FOTO
import com.projects.pasBal.ui.home.AjukanPenawaranViewModelFactory
import com.projects.pasBal.ui.splashScreen.SplashScreen
import com.projects.pasBal.ui.splashScreen.SplashScreen.Companion.API_TOKEN
import com.projects.pasBal.ui.splashScreen.SplashScreen.Companion.ID_ADVERTISER
import com.projects.pasBal.ui.transaksi.ajukanPenawaran.AjukanPenawaranViewModel
import com.projects.pasBal.ui.transaksi.menuTransaksi.MenuTransaksi
import com.projects.pasBal.utils.*
import kotlinx.android.synthetic.main.activity_ajukan_penawaran.*
import org.kodein.di.KodeinAware
import org.kodein.di.android.kodein
import org.kodein.di.generic.instance
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import kotlin.concurrent.timerTask


class AjukanPenawaranActivity : AppCompatActivity(), KodeinAware, CommonListener, GenosDialog {


    override val kodein by kodein()
    private val factory: AjukanPenawaranViewModelFactory by instance()

    var alamat: String? = null
    private var gambar: String? = null
    private var idBaliho: Int? = null
    private var cal = Calendar.getInstance()
    var idPref: SharedPreferences? = null

    var textKonfirmasi: TextView? = null
    var buttonBatal: Button? = null
    var buttonAjukan: Button? = null
    var dialog: DialogPlus? = null

    private var viewModel: AjukanPenawaranViewModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ajukan_penawaran)

        viewModel =
            ViewModelProviders.of(this, factory).get(AjukanPenawaranViewModel::class.java)
        viewModel!!.listener = this


        initGetExtra()
        cekUser()
        init()
        initCard()
        initButton()
        initDatePicker(text_tanggal_awal)
        initDatePicker(text_tanggal_akhir)

    }

    private fun cekUser() {
        if (idPref?.getInt(ID_ADVERTISER, 0)!! == 0) {
            gotoLoginFormBelumLogin()
        }
    }

    private fun init() {
        Coroutines.main {
            loading_screen.visibility = View.INVISIBLE
            card_detail.visibility = View.VISIBLE
            button_order.visibility = View.VISIBLE
        }

        text_nama.text = "Ajukan Penawaran"
    }

    private fun initCard() {

        text_alamat_ajukan_penawaran.text = alamat

        val requestOptions = RequestOptions()
            .placeholder(R.color.backdrop)
            .error(R.color.backdrop)

        Glide.with(this@AjukanPenawaranActivity)
            .applyDefaultRequestOptions(requestOptions)
            .load(URL_FOTO + gambar)
            .into(image_detail_ajukan_penawaran)
    }

    @SuppressLint("SimpleDateFormat")
    private fun initButton() {
        button_back.setOnClickListener {
            finish()
        }

        button_order.setOnClickListener {
            try {
                val dateAwal =
                    SimpleDateFormat("dd MMM yyyy").parse(text_tanggal_awal.text.toString())
                val dateAkhir =
                    SimpleDateFormat("dd MMM yyyy").parse(text_tanggal_akhir.text.toString())
                val myFormat = "yyyy-MM-dd" // mention the format you need
                SimpleDateFormat(myFormat, Locale.US)
                val currentTime = Calendar.getInstance()
                currentTime.add(Calendar.DAY_OF_YEAR, 2)

                val duaHariBerikutnya = currentTime.time
                if (duaHariBerikutnya < dateAwal) {

                    if (dateAkhir!! >= dateAwal) {

                        Coroutines.main {
                            val totalHari =
                                (dateAkhir.time - dateAwal!!.time) / (1000 * 60 * 60 * 24) + 1
                            val text = text_tanggal_awal.text.toString() +
                                    " sampai " + text_tanggal_akhir.text.toString() +
                                    " (" + totalHari + " hari)"

                            dialogHandler(text)
                        }

                    } else {
                        dialogHandler(
                            this,
                            "Cek Lagi tanggal anda..",
                            "tanggal awal iklanya tidak bisa lebih besar dari tanggal ahkir..",
                            R.drawable.ic_notif,
                            "pesan"
                        )
                    }
                } else {
                    dialogHandler(
                        this,
                        "Cek Lagi tanggal anda..",
                        "tanggal awal iklanya harus lebih 3 hari dari hari ini..",
                        R.drawable.ic_notif,
                        "pesan"
                    )
                }
            } catch (e: ParseException) {
                dialogHandler(
                    this,
                    "Cek Lagi tanggal anda..",
                    "pilih dulu tanggal iklanya..",
                    R.drawable.ic_notif,
                    "pesan"
                )
            }

        }
    }

    private fun initDatePicker(textView: TextView) {
        // create an OnDateSetListener
        val dateSetListener =
            DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                cal.set(Calendar.YEAR, year)
                cal.set(Calendar.MONTH, monthOfYear)
                cal.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                updateDateInView(textView)
            }

        // when you click on the button, show DatePickerDialog that is set with OnDateSetListener
        textView.setOnClickListener {
            DatePickerDialog(
                this@AjukanPenawaranActivity,
                dateSetListener,
                // set DatePickerDialog to point to today's date when it loads up
                cal.get(Calendar.YEAR),
                cal.get(Calendar.MONTH),
                cal.get(Calendar.DAY_OF_MONTH)
            ).show()
        }
    }

    private fun updateDateInView(textView: TextView) {
        val myFormat = "dd MMM yyyy" // mention the format you need
        val sdf = SimpleDateFormat(myFormat, Locale.US)
        textView.text = sdf.format(cal.getTime())
    }


    private fun dialogHandler(text: String) {
        dialog = DialogPlus.newDialog(this)
            .setContentHolder(ViewHolder(R.layout.dialog_ajukan_penawaran))
            .setContentBackgroundResource(R.drawable.background_rounded2_corner)
            .create()
        textKonfirmasi = dialog?.holderView!!.findViewById(R.id.text_konfirmasi_pengajuan)
        buttonBatal = dialog?.holderView!!.findViewById(R.id.button_batal_ajukan)
        buttonAjukan = dialog?.holderView!!.findViewById(R.id.button_konfirmasi_ajukan)
        textKonfirmasi?.text = text

        buttonBatal?.setOnClickListener {
            dialog?.dismiss()
        }

        buttonAjukan?.setOnClickListener {
            prosesPengajuan()
        }
        dialog?.show()
    }


    @SuppressLint("SimpleDateFormat")
    private fun prosesPengajuan() {
        val dateAwal =
            SimpleDateFormat("dd MMM yyyy").parse(text_tanggal_awal.text.toString())
        val dateAkhir =
            SimpleDateFormat("dd MMM yyyy").parse(text_tanggal_akhir.text.toString())
        val myFormat = "yyyy-MM-dd" // mention the format you need
        val sdf = SimpleDateFormat(myFormat, Locale.US)
        dialog?.dismiss()

        Coroutines.main {
            val token: String = idPref?.getString(API_TOKEN, "")!!
            val idAdvertiser: Int = idPref?.getInt(ID_ADVERTISER, 0)!!
            viewModel!!.postAjukanTransaksi(
                idBaliho!!,
                idAdvertiser,
                sdf.format(dateAwal!!),
                sdf.format(dateAkhir!!),
                token
            )
        }
    }

    private fun initGetExtra() {
        alamat = intent.getStringExtra("alamat")
        gambar = intent.getStringExtra("gambar")
        idBaliho = intent.getIntExtra("id", 1)

        idPref = androidx.preference.PreferenceManager
            .getDefaultSharedPreferences(this)
    }

    override fun onStarted() {
        Coroutines.main {
            loading_screen.visibility = View.VISIBLE
            card_detail.visibility = View.INVISIBLE
            button_order.visibility = View.INVISIBLE
        }
    }

    override fun onSuccess() {
        Coroutines.main {
            loading_screen.visibility = View.INVISIBLE
            card_detail.visibility = View.VISIBLE
            button_order.visibility = View.VISIBLE
        }
    }

    override fun onStartJob() {
        Coroutines.main {
            loading_screen.visibility = View.VISIBLE
            card_detail.visibility = View.INVISIBLE
            button_order.visibility = View.INVISIBLE
        }
    }

    override fun onSuccessJob() {
        val timer = Timer()
        dialogHandler(
            this,
            "Sukses",
            "Pengajuan penawaran anda berhasil, mohon tunggu konfirmasi dari admin",
            R.drawable.ic_welcome,
            "pesan"
        )
        timer.schedule(timerTask {
            Coroutines.main {

                loading_screen.visibility = View.INVISIBLE
                card_detail.visibility = View.VISIBLE
                button_order.visibility = View.VISIBLE

                Intent(this@AjukanPenawaranActivity, MenuTransaksi::class.java).also {
                    it.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    startActivity(it)
                }
            }
        }, 1000)


    }

    override fun onTimeOut(message: String) {
        loading_screen.visibility = View.INVISIBLE
        card_detail.visibility = View.VISIBLE
        button_order.visibility = View.INVISIBLE

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


    override fun onFailure(message: String) {
        Coroutines.main {

            loading_screen.visibility = View.INVISIBLE
            card_detail.visibility = View.VISIBLE
            button_order.visibility = View.VISIBLE

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

    override fun onEmpty() {

    }

    override fun onResume() {
        super.onResume()
        appContext = this
        SplashScreen.STATE_ACTIVITY = "AjukanPenawaranActivity"
        cekUser()
    }

    companion object {

        var appContext: Context? = null


    }

    override fun action() {

    }
}
