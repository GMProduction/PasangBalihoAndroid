package com.genossys.pasangbaliho.data.db.entity

import com.google.gson.annotations.SerializedName

data class Slider(

    @SerializedName("id_slider")
    val idSlider: Int?,

    val title: String?,

    val deskripsi: String?,

    @SerializedName("url_foto")
    val urlFoto: String?,

    @SerializedName("link")
    val link: String?,

    val terlihat: Int?
)