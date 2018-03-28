package proglife.com.ua.intellektiks.data.db

import android.arch.persistence.room.Database
import android.arch.persistence.room.Room
import android.arch.persistence.room.RoomDatabase
import android.content.Context

/**
 * Created by Evhenyi Shcherbyna on 23.03.2018.
 * Copyright (c) 2018 ProgLife. All rights reserved.
 */
//@Database(entities = [], version = 1)
abstract class DataBase: RoomDatabase() {

//    abstract fun deviceDao(): DeviceDao

    companion object {

        fun getInstance(context: Context): DataBase {
            return Room.databaseBuilder(context.applicationContext,
                    DataBase::class.java, "local.db").build()
        }

    }

}