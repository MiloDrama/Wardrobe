package com.example.milo.wardrobe

import android.arch.persistence.room.Database
import android.arch.persistence.room.Room
import android.arch.persistence.room.RoomDatabase
import android.arch.persistence.room.TypeConverters
import android.content.Context

@Database(entities=arrayOf(Cloth::class), version = 1)
@TypeConverters(InstantConverter::class,ClothTypeConverter::class)
abstract class MyDatabase : RoomDatabase() {
    abstract fun getDao() : MyDao

    companion object {
        @Volatile private var INSTANCE: MyDatabase?=null

        fun getInstance(context:Context): MyDatabase = INSTANCE?: synchronized(this) {
            INSTANCE?:buildDatabase(context).also { INSTANCE = it}
        }

        private fun buildDatabase(context:Context) =
                Room.databaseBuilder(context.applicationContext, MyDatabase::class.java, "Cloths.db").allowMainThreadQueries().build()
    }
}