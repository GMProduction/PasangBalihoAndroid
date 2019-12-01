package com.genossys.pasangbaliho.data.db.entity

import com.google.gson.annotations.SerializedName

data class News(

    @SerializedName("id_news")
    val idNews: Int?,

    val judul: String?,
    val isi: String?,
    val gambar: String?,
    val link: String?,

    @SerializedName("created_at")
    val createdAt: String?,

    @SerializedName("updated_at")
    val updateAt: String?
    )