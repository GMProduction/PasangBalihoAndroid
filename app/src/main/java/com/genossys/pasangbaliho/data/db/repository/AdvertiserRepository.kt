package com.genossys.pasangbaliho.data.db.repository

import androidx.lifecycle.LiveData
import com.genossys.pasangbaliho.data.db.unitlocalized.SpesifikAdvertiserData

interface AdvertiserRepository {
    suspend fun getAdvertiser(metric: Boolean): LiveData<out SpesifikAdvertiserData>
}