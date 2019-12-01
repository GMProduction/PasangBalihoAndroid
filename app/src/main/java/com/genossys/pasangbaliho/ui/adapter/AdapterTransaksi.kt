package com.genossys.pasangbaliho.ui.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.genossys.pasangbaliho.R
import com.genossys.pasangbaliho.data.db.entity.Transaksi
import com.genossys.pasangbaliho.data.network.api.URL_FOTO
import com.genossys.pasangbaliho.ui.transaksi.detailTransaksi.DetailTransaksi
import com.genossys.pasangbaliho.utils.tglSystemToView
import kotlinx.android.synthetic.main.temp_item_transaksi.view.*

class AdapterTransaksi : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var items: List<Transaksi> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return TransaksiViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.temp_item_transaksi, parent, false)
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        when (holder) {
            is TransaksiViewHolder -> {
                holder.bind(items[position])
            }
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }

    fun sumitList(transaksiList: List<Transaksi>) {
        items = transaksiList
    }

    class TransaksiViewHolder constructor(
        itemView: View
    ) : RecyclerView.ViewHolder(itemView) {

        private val transaksiImage: ImageView = itemView.image_transaksi
        private val noTransaksi: TextView = itemView.text_notans
        private val orientasidanukuran: TextView = itemView.text_orientasi_dan_ukuran
        private val transaksiAlamat: TextView = itemView.text_alamat
        private val tanggalTransaksi: TextView = itemView.text_tanggal_transaksi
        private val cardNotif: CardView = itemView.card_notif


        fun bind(transaksi: Transaksi) {

            val requestOptions = RequestOptions()
                .placeholder(R.color.backdrop)
                .error(R.mipmap.noimage)

            Glide.with(itemView.context)
                .applyDefaultRequestOptions(requestOptions)
                .load(URL_FOTO + transaksi.urlFoto)
                .into(transaksiImage)

            val noTrans = "No. Transaksi: " + transaksi.idTransaksi
            val terbaca = transaksi.terbacaAdvertiser
            val alamatBaliho = transaksi.alamat + ", " + transaksi.kota + ", " + transaksi.provinsi
            val kategori = transaksi.kategori

            transaksiAlamat.text = alamatBaliho
            orientasidanukuran.text = kategori
            tanggalTransaksi.text = tglSystemToView(transaksi.tanggalTransaksi!!)
            noTransaksi.text = noTrans

            if (terbaca == 1) {
                cardNotif.setBackgroundResource(R.color.frontdrop)

            } else {
                cardNotif.setBackgroundResource(R.color.colorViewNotif)
            }

            itemView.setOnClickListener {
                val i = Intent(itemView.context, DetailTransaksi::class.java)
                i.putExtra("id", transaksi.idTransaksi)
                itemView.context.startActivity(i)
            }
        }
    }

}
