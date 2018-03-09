package com.tuwien.buildinginteractioninterfaces.prototype.data.room

import android.arch.persistence.db.SupportSQLiteDatabase
import android.arch.persistence.room.Room
import android.arch.persistence.room.migration.Migration
import android.content.Context

class RoomDatabase private constructor(){
    private var db: AppDatabase? = null

    companion object {
        val instance: RoomDatabase = RoomDatabase()
        val MIGRATION_1_2: Migration = object: Migration(1,2) {
            override fun migrate (database: SupportSQLiteDatabase) {
                database.execSQL("ALTER TABLE benchmark "
                        + " ADD COLUMN input_string TEXT NOT NULL DEFAULT '' ")
            }
        }

    }
    fun getDatabase(context: Context): AppDatabase {
        if(db == null){
            db = Room.databaseBuilder(context, AppDatabase::class.java, "benchmarks")
                    .addMigrations(MIGRATION_1_2).build()
        }

        return db as AppDatabase
    }



}