package com.genossys.pasangbaliho.data.db.entity

import com.google.gson.annotations.SerializedName

data class PageBaliho(

    @SerializedName("current_page")
    val currentPage: Int?,

    @SerializedName("data")
    val baliho: List<Baliho?>,

    val total: Int?,
    @SerializedName("last_page")
    val lastPage: Int?

)