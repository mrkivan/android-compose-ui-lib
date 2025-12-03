package com.tnm.android.core.di

import android.content.Context
import androidx.room.Room
import com.tnm.android.core.data.TodoTaskDao
import com.tnm.android.core.data.TodoTaskDatabase
import com.tnm.android.core.data.TodoTaskRepositoryImpl
import com.tnm.android.core.domain.TodoTaskRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideTodoTaskDatabase(@ApplicationContext context: Context): TodoTaskDatabase {
        return Room.databaseBuilder(
            context.applicationContext,
            TodoTaskDatabase::class.java,
            "app_todo_task_db"
        ).build()
    }

    @Provides
    fun provideTodoTaskDao(database: TodoTaskDatabase): TodoTaskDao {
        return database.todoTaskDao()
    }

    @Provides
    @Singleton
    fun provideTodoTaskRepository(dao: TodoTaskDao): TodoTaskRepository {
        return TodoTaskRepositoryImpl(dao)
    }
}