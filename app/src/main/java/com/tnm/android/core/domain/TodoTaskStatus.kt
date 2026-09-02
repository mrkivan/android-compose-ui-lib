package com.tnm.android.core.domain

/** Domain concept, not a storage detail — Room persists it via [com.tnm.android.core.data.AppDataConverter]. */
enum class TodoTaskStatus {
    PENDING,
    COMPLETED,
    FAILED,
    CANCELLED,
}
