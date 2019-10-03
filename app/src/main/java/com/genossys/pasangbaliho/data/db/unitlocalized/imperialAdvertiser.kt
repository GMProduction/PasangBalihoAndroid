package com.genossys.pasangbaliho.data.db.unitlocalized

import androidx.room.ColumnInfo

class imperialAdvertiser(
    @ColumnInfo(name = "email")
    override val email: String,

    @ColumnInfo(name = "nama")
    override val nama: String,

    @ColumnInfo(name = "telp")
    override val telp: String,

    @ColumnInfo(name = "alamat")
    override val alamat: String
) : SpesifikAdvertiserData