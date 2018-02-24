package com.tuwien.buildinginteractioninterfaces.prototype.data.room

import android.arch.persistence.room.Room
import android.content.Context

class RoomDatabase private constructor(){
    private var db: AppDatabase? = null

    companion object {
        val instance: RoomDatabase = RoomDatabase()

    }
    fun getDatabase(context: Context): AppDatabase {
        if(db == null){
            db = Room.databaseBuilder(context, AppDatabase::class.java, "database-name").build()
        }

        return db as AppDatabase
    }

}