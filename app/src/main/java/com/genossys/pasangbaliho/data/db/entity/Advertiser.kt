package com.genossys.pasangbaliho.data.db.entity


import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

const val ADVERTISER_ID = 0

@Entity(tableName = "tb_advertiser")
data class Advertiser(


    val email: String,
    val nama: String,
    val telp: String,
    val alamat: String,

    @SerializedName("api_token")
    val apiToken: String,

    @SerializedName("email_verified_at")
    val emailVerifiedAt: String,

    @SerializedName("created_at")
    val createdAt: String,

    @SerializedName("updated_at")
    val updatedAt: String
) {
    @PrimaryKey(autoGenerate = false)
    val id: Int = ADVERTISER_ID
}