package com.genossys.pasangbaliho.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.genossys.pasangbaliho.data.db.entity.Advertiser

@Database(
    entities = [Advertiser::class],
    version = 1
)

abstract class PasangBalihoDb : RoomDatabase() {

    abstract fun AdvertiserDao(): AdvertiserDao

    companion object {
        @Volatile
        private var instance: PasangBalihoDb? = null
        private val LOCK = Any()

        operator fun invoke(context: Context) = instance ?: synchronized(LOCK) {
            instance ?: buildDatabase(context).also {
                instance = it
            }
        }

        private fun buildDatabase(context: Context) =
            Room.databaseBuilder(
                context.applicationContext,
                PasangBalihoDb::class.java,
                "pasangBalihoDb"
            ).build()
    }
}