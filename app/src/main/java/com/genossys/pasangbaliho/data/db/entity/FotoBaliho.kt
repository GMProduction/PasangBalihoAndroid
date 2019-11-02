package com.genossys.pasangbaliho.data.db.entity

import com.google.gson.annotations.SerializedName

data class FotoBaliho(

    @SerializedName("id_foto")
    val idFoto: Int?,

    @SerializedName("id_baliho")
    val idBaliho: Int?,

    @SerializedName("url_foto")
    val urlFoto: String?
)