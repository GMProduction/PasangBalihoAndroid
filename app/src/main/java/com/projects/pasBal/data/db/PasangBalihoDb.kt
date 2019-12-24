package com.projects.pasBal.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.projects.pasBal.data.db.dao.AdvertiserDao
import com.projects.pasBal.data.db.dao.BalihoDao
import com.projects.pasBal.data.db.entity.Advertiser
import com.projects.pasBal.data.db.entity.Baliho


@Database(
    entities = [Advertiser::class, Baliho::class],
    version = 2,
    exportSchema = false
)

abstract class PasangBalihoDb : RoomDatabase() {

    abstract fun advertiserDao(): AdvertiserDao
    abstract fun balihoDao(): BalihoDao


    companion object {

        @Volatile
        private var INSTANCE: PasangBalihoDb? = null
        private val LOCK = Any()

        operator fun invoke(context: Context) = INSTANCE ?: synchronized(LOCK) {
            INSTANCE ?: builDataBase(context).also {
                INSTANCE = it
            }
        }

        private fun builDataBase(context: Context) =
            Room.databaseBuilder(
                context.applicationContext,
                PasangBalihoDb::class.java,
                "pasangBaliho.db"
            )
                .fallbackToDestructiveMigration()
                .build()


    }

}