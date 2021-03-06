package com.projects.pasBal.data.db.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.projects.pasBal.data.db.entity.ADVERTISER_NUMBER
import com.projects.pasBal.data.db.entity.Advertiser

@Dao
interface AdvertiserDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(advertiserEntry: Advertiser)

    @Query("DELETE from advertiser where num = $ADVERTISER_NUMBER")
    suspend fun delete()

    @Query("select * from advertiser where num = $ADVERTISER_NUMBER")
    fun checkAdvertiser(): LiveData<Advertiser>

    @Query("select * from advertiser where num = $ADVERTISER_NUMBER")
    suspend fun checkAdvertiser2(): Advertiser

}