package com.genossys.pasangbaliho.data.db.entity

import com.google.gson.annotations.SerializedName

data class Transaksi(

    @SerializedName("id_transaksi")
    val idTransaksi: Int?,

    @SerializedName("id_baliho")
    val idBaliho: Int?,

    @SerializedName("id_advertiser")
    val idAdvertiser: Int?,

    @SerializedName("harga_ditawarkan")
    val hargaDitawarkan: Int?,

    @SerializedName("harga_deal")
    val hargaDeal: Int?,

    @SerializedName("status")
    val status: String?,

    @SerializedName("status_pembayaran")
    val statusPembayaran: String?,

    @SerializedName("tanggal_transaksi")
    val tanggalTransaksi: String?,

    @SerializedName("keterangan_batal")
    val keteranganBatal: String?,

    @SerializedName("tanggal_awal")
    val tanggalAwal: String?,

    @SerializedName("alamat")
    val alamat: String,

    @SerializedName("nama_kota")
    val kota: String?,

    @SerializedName("nama_provinsi")
    val provinsi: String?,

    @SerializedName("url_foto")
    val urlFoto: String?,

    @SerializedName("tanggal_akhir")
    val tanggalAkhir: String?,

    @SerializedName("terbaca_advertiser")
    val terbacaAdvertiser: Int?,

    @SerializedName("terbaca_client")
    val terbacaClient: Int?,

    val kategori: String?

)