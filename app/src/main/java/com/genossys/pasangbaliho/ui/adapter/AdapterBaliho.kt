package com.genossys.pasangbaliho.ui.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.genossys.pasangbaliho.R
import com.genossys.pasangbaliho.data.db.entity.Baliho
import com.genossys.pasangbaliho.data.network.api.URL_THUMBNAIL
import com.genossys.pasangbaliho.ui.detail.DetailBalihoActivity
import com.genossys.pasangbaliho.utils.toDesimalText
import kotlinx.android.synthetic.main.temp_baliho_utama.view.*

class AdapterBaliho : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var items: List<Baliho> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return BalihoViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.temp_baliho_utama, parent, false)
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        when (holder) {
            is BalihoViewHolder -> {
                holder.bind(items[position])
            }
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }

    fun sumitList(balihoList: List<Baliho>) {
        items = balihoList
    }

    class BalihoViewHolder constructor(
        itemView: View
    ) : RecyclerView.ViewHolder(itemView) {

        private val balihoImage: ImageView = itemView.image_temp_utama
        private val rootLayout: ConstraintLayout = itemView.root_template
        private val balihoTittle: TextView = itemView.text_alamat_temp_utama
        private val balihoOrientasi: TextView = itemView.text_orientasi_dan_ukuran
//        private val balihoDeskripsi: TextView = itemView.text_deskripsi
        private val balihoHarga: TextView = itemView.text_harga
        private val buttonDetail: TextView = itemView.button_temp_utama

        fun bind(baliho: Baliho) {

            val requestOptions = RequestOptions()
                .placeholder(R.color.backdrop)
                .error(R.mipmap.noimage)

            Glide.with(itemView.context)
                .applyDefaultRequestOptions(requestOptions)
                .load(URL_THUMBNAIL + baliho.urlFoto)
                .into(balihoImage)

            val alamatBaliho = baliho.alamat + ", " + baliho.kota + ", " + baliho.provinsi
            val orientasi =
                baliho.kategori+" " + baliho.orientasi + ", " + baliho.lebar + "cm x " + baliho.tinggi + "cm"
            val harga = "Rp " + toDesimalText(baliho.hargaMarket!!) + " /bulan"
            balihoTittle.text = alamatBaliho
//            balihoDeskripsi.text = baliho.deskripsi
            balihoOrientasi.text = orientasi
            balihoHarga.text = harga


            buttonDetail.setOnClickListener {
                val i = Intent(itemView.context, DetailBalihoActivity::class.java)
                i.putExtra("id", baliho.idBaliho)
                itemView.context.startActivity(i)
            }

            rootLayout.setOnClickListener {
                val i = Intent(itemView.context, DetailBalihoActivity::class.java)
                i.putExtra("id", baliho.idBaliho)
                itemView.context.startActivity(i)
            }
        }
    }

}
