package com.genossys.pasangbaliho.data.db.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.genossys.pasangbaliho.data.db.entity.ADVERTISER_NUMBER
import com.genossys.pasangbaliho.data.db.entity.Advertiser
import com.genossys.pasangbaliho.data.db.entity.Baliho

@Dao
interface BalihoDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun saveAllBaliho(baliho: List<Baliho>)

    @Query("DELETE from advertiser where num = $ADVERTISER_NUMBER")
    suspend fun delete()

    @Query("select * from balihos")
    fun getDataBAliho(): LiveData<List<Baliho>>

}