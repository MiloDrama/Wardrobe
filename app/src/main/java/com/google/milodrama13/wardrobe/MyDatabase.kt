package com.google.milodrama13.wardrobe

import android.arch.persistence.db.SupportSQLiteDatabase
import android.arch.persistence.room.Database
import android.arch.persistence.room.Room
import android.arch.persistence.room.RoomDatabase
import android.arch.persistence.room.TypeConverters
import android.arch.persistence.room.migration.Migration
import android.content.Context

@Database(entities=arrayOf(Cloth::class,TraceItem::class), version = 2)
@TypeConverters(InstantConverter::class,ClothTypeConverter::class, TracingLevelConverter::class)
abstract class MyDatabase : RoomDatabase() {
    abstract fun getDao() : MyDao

    companion object {
        @Volatile private var INSTANCE: MyDatabase?=null

        fun getInstance(context:Context): MyDatabase = INSTANCE?: synchronized(this) {
            INSTANCE?:buildDatabase(context).also { INSTANCE = it}
        }

        val MIGRATION_1_2 = object : Migration(1,2){
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("CREATE TABLE  `tracing` (`id` INTEGER NOT NULL, `timestamp` INTEGER NOT NULL, `level` TEXT NOT NULL, `message` TEXT NOT NULL, PRIMARY KEY(`id`))")
            }
        }

        private fun buildDatabase(context:Context) =
                Room.databaseBuilder(context.applicationContext, MyDatabase::class.java, "Cloths.db")
                        .addMigrations(MIGRATION_1_2)
                        .allowMainThreadQueries().build()
    }
}