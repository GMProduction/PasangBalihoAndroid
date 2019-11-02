package com.genossys.pasangbaliho.data.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "balihos")
data class Baliho(

    @PrimaryKey(autoGenerate = false)
    @SerializedName("id_baliho")
    val idBaliho: Int?,

    @SerializedName("id_client")
    val idClient: Int?,

    val kategori: String?,

    @SerializedName("nama_baliho")
    val namaBaliho: String?,

    @SerializedName("ukuran_baliho")
    val ukuranBaliho: String?,

    val provinsi: String?,
    val kota: String?,
    val alamat: String?,
    val latitude: String?,
    val logitude: String?,

    @SerializedName("min_harga")
    val minHarga: Int?,

    @SerializedName("max_harga")
    val maxHarga: Int?,

    val deskripsi: String?,

    @SerializedName("url_360")
    val url360: String?,

    @SerializedName("url_foto")
    val urlFoto: String?,

    @SerializedName("created_at")
    val createdAt: String?,

    @SerializedName("update_at")
    val updateAt: String?

)