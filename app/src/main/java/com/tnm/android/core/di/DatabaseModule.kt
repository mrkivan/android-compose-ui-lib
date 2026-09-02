package com.tnm.android.core.di

import android.content.Context
import androidx.room.Room
import com.tnm.android.core.data.TodoTaskDao
import com.tnm.android.core.data.TodoTaskDatabase
import com.tnm.android.core.data.TodoTaskRepositoryImpl
import com.tnm.android.core.domain.TodoTaskRepository
import dagger.Binds
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
    fun provideTodoTaskDatabase(@ApplicationContext context: Context): TodoTaskDatabase = Room.databaseBuilder(
        context.applicationContext,
        TodoTaskDatabase::class.java,
        "app_todo_task_db",
    )
        // Showcase app: a schema bump just recreates the tables. No migrations to maintain,
        // and no crash on upgrade — the trade is that local tasks are wiped.
        .fallbackToDestructiveMigration(dropAllTables = true)
        .build()

    @Provides
    fun provideTodoTaskDao(database: TodoTaskDatabase): TodoTaskDao = database.todoTaskDao()
}

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    // @Binds, not a manual `TodoTaskRepositoryImpl(dao)`: the impl already has @Inject and
    // @Singleton, so Hilt builds it and the binding stays correct when its constructor changes.
    @Binds
    abstract fun bindTodoTaskRepository(impl: TodoTaskRepositoryImpl): TodoTaskRepository
}
