package com.tnm.android.core.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

@Dao
interface TodoTaskDao {

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insertTodoTask(todoTask: TodoTaskEntity)

    @Update
    suspend fun updateTodoTask(todoTask: TodoTaskEntity)

    @Query("SELECT * FROM table_todo_task ORDER BY insertAt DESC")
    suspend fun getAllTask(): List<TodoTaskEntity>

    @Query("SELECT * FROM table_todo_task where status = :status ORDER BY insertAt DESC")
    suspend fun getTodoTaskByStatus(
        status: TodoTaskStatus = TodoTaskStatus.PENDING
    ): List<TodoTaskEntity>

    @Delete
    suspend fun deleteTodoTask(todoTask: TodoTaskEntity)
}