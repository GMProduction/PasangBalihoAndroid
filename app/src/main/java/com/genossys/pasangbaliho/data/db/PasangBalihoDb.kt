package com.genossys.pasangbaliho.data.db

import android.content.Context
import android.util.Log
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.genossys.pasangbaliho.data.db.dao.AdvertiserDao
import com.genossys.pasangbaliho.data.db.dao.BalihoDao
import com.genossys.pasangbaliho.data.db.entity.Advertiser
import com.genossys.pasangbaliho.data.db.entity.Baliho
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@Database(
    entities = [Advertiser::class, Baliho::class],
    version = 1
)

abstract class PasangBalihoDb : RoomDatabase() {

    abstract fun advertiserDao(): AdvertiserDao
    abstract fun balihoDao(): BalihoDao

    companion object {

        @Volatile
        private var INSTANCE: PasangBalihoDb? = null
        private val LOCK = Any()

        operator fun invoke(context: Context) = INSTANCE?: synchronized(LOCK){
            INSTANCE?:builDataBase(context).also {
                INSTANCE = it
            }
        }

        private fun builDataBase(context: Context) =
            Room.databaseBuilder(
                context.applicationContext,
                PasangBalihoDb::class.java,
                "pasangBaliho.db"
            ).build()




    }

}