package com.tnm.android.core.data

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(
    entities = [TodoTaskEntity::class],
    version = 1,
    exportSchema = true
)
@TypeConverters(AppDataConverter::class)
abstract class TodoTaskDatabase : RoomDatabase() {
    abstract fun todoTaskDao(): TodoTaskDao
}