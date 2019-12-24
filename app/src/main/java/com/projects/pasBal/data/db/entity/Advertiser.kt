package com.projects.pasBal.data.db.entity


import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

const val ADVERTISER_NUMBER = 0

@Entity(tableName = "advertiser")
data class Advertiser(


    @PrimaryKey(autoGenerate = false)
    var num: Int = ADVERTISER_NUMBER,

    var id: Int?,
    val email: String,
    val nama: String,
    @SerializedName("nama_instansi")
    val namaInstansi: String?,

    val telp: String?,
    val alamat: String?,

    @SerializedName("api_token")
    val apiToken: String?,

    @SerializedName("email_verified_at")
    val emailVerifiedAt: String?,

    @SerializedName("created_at")
    val createdAt: String?,

    @SerializedName("updated_at")
    val updatedAt: String?
)