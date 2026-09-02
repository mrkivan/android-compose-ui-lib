package com.tnm.android.core.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.tnm.android.core.domain.TodoTaskStatus
import kotlinx.coroutines.flow.Flow

@Dao
interface TodoTaskDao {

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insertTodoTask(todoTask: TodoTaskEntity)

    @Update
    suspend fun updateTodoTask(todoTask: TodoTaskEntity)

    // Flow, not List: Room re-emits on every write to the table, so the UI updates itself after
    // an insert/update/delete instead of needing a manual re-fetch.
    @Query("SELECT * FROM table_todo_task ORDER BY insertAt DESC")
    fun observeAllTasks(): Flow<List<TodoTaskEntity>>

    @Query("SELECT * FROM table_todo_task WHERE status = :status ORDER BY insertAt DESC")
    fun observeTasksByStatus(status: TodoTaskStatus): Flow<List<TodoTaskEntity>>

    @Delete
    suspend fun deleteTodoTask(todoTask: TodoTaskEntity)
}
