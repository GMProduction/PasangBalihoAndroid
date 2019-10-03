package com.genossys.pasangbaliho.data.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.genossys.pasangbaliho.data.db.entity.ADVERTISER_ID
import com.genossys.pasangbaliho.data.db.entity.Advertiser
import com.genossys.pasangbaliho.data.db.unitlocalized.imperialAdvertiser

@Dao
interface AdvertiserDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun upsert (advertiserEntry: Advertiser)

    @Query("select * from tb_advertiser Where id = $ADVERTISER_ID")
    fun getAdvertiser():LiveData<imperialAdvertiser>
}