package com.genossys.pasangbaliho.ui.transaksi

import android.app.DatePickerDialog
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.genossys.pasangbaliho.R
import com.genossys.pasangbaliho.data.network.Api.URL_FOTO
import com.genossys.pasangbaliho.utils.Coroutines
import com.genossys.pasangbaliho.utils.snackbar
import com.genossys.pasangbaliho.utils.toast
import com.orhanobut.dialogplus.DialogPlus
import com.orhanobut.dialogplus.ViewHolder
import kotlinx.android.synthetic.main.activity_ajukan_penawaran.*
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*


class AjukanPenawaranActivity : AppCompatActivity() {

    var alamat: String? = null
    private var gambar: String? = null
    private var idBaliho: Int? = null
    var cal = Calendar.getInstance()

    var textKonfirmasi: TextView? = null
    var buttonBatal: Button? = null
    var buttonAjukan: Button? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ajukan_penawaran)

        initGetExtra()
        init()
        initCard()
        initButton()
        initDatePicker(text_tanggal_awal)
        initDatePicker(text_tanggal_akhir)

    }

    private fun init() {
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
                val sdf = SimpleDateFormat(myFormat, Locale.US)
                toast(sdf.format(dateAwal))

                if (dateAkhir >= dateAwal) {

                    Coroutines.main {
                        val totalHari = (dateAkhir.time - dateAwal.time) / (1000 * 60 * 60 * 24) + 1
                        val text = text_tanggal_awal.text.toString() +
                                    " sampai " + text_tanggal_akhir.text.toString() +
                                " (" + totalHari + " hari)"

                        dialogHandler(text)
                    }

                } else {
                    root_ajukan_penawaran.snackbar("tanggal awal iklanya tidak bisa lebih besar dari tanggal ahkir..")
                }
            } catch (e: ParseException) {
                root_ajukan_penawaran.snackbar("pilih dulu tanggal iklanya..")
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
        val dialog = DialogPlus.newDialog(this)
            .setContentHolder(ViewHolder(R.layout.dialog_ajukan_penawaran))
            .setContentBackgroundResource(R.drawable.background_rounded2_corner)
            .setOnItemClickListener { dialog, item, view, position -> }
            .setExpanded(true)  // This will enable the expand feature, (similar to android L share dialog)
            .create()
        textKonfirmasi = dialog.holderView.findViewById(R.id.text_konfirmasi_pengajuan)
        buttonBatal = dialog.holderView.findViewById(R.id.button_batal_ajukan)
        buttonAjukan = dialog.holderView.findViewById(R.id.button_konfirmasi_ajukan)
        textKonfirmasi?.text = text

        buttonBatal?.setOnClickListener {
            dialog.dismiss()
        }

        buttonAjukan?.setOnClickListener {
            prosesPengajuan()
        }
        dialog.show()
    }


    private fun prosesPengajuan() {

    }

    private fun initGetExtra(){
        alamat = intent.getStringExtra("alamat")
        gambar = intent.getStringExtra("gambar")
        idBaliho = intent.getIntExtra("id", 1)
    }
}
