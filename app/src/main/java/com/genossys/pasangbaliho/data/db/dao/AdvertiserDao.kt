package com.genossys.pasangbaliho.data.db.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.genossys.pasangbaliho.data.db.entity.ADVERTISER_NUMBER
import com.genossys.pasangbaliho.data.db.entity.Advertiser

@Dao
interface AdvertiserDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(advertiserEntry: Advertiser)

    @Query("DELETE from advertiser where num = $ADVERTISER_NUMBER")
    suspend fun delete()

    @Query("select * from advertiser where num = $ADVERTISER_NUMBER")
    fun checkAdvertiser(): LiveData<Advertiser>

}