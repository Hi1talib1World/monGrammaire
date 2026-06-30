package com.example.mongrammaire.Data

import android.content.Context
import com.example.mongrammaire.Data.Local.LessonDatabaseHelper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

/**
 * Pillar 1: Single Source of Truth Interface
 */
interface ILessonRepository {
    fun getLessonProgress(lessonId: Int): Flow<Int>
    suspend fun saveLessonProgress(lessonId: Int, stepIndex: Int, isCompleted: Boolean): Result<Unit>
    suspend fun setLessonMastered(lessonId: Int, isMastered: Boolean): Result<Unit>
    suspend fun setCardBookmarked(lessonId: Int, cardIndex: Int): Result<Unit>
    suspend fun saveSetting(key: String, value: String): Result<Unit>
    suspend fun getSetting(key: String, defaultValue: String): String
}

class LessonRepositoryImpl(context: Context) : ILessonRepository {
    private val dbHelper = LessonDatabaseHelper(context)

    override fun getLessonProgress(lessonId: Int): Flow<Int> = flow {
        // Pillar 1: Hydrate directly from persistent cache
        emit(dbHelper.getLessonProgress(lessonId))
    }.flowOn(Dispatchers.IO)

    override suspend fun saveLessonProgress(lessonId: Int, stepIndex: Int, isCompleted: Boolean): Result<Unit> = runCatching {
        // Pillar 2: Atomic State Mutation (committing to disk)
        dbHelper.saveLessonProgress(lessonId, stepIndex, isCompleted)
    }

    override suspend fun setLessonMastered(lessonId: Int, isMastered: Boolean): Result<Unit> = runCatching {
        dbHelper.setLessonMastered(lessonId, isMastered)
    }

    override suspend fun setCardBookmarked(lessonId: Int, cardIndex: Int): Result<Unit> = runCatching {
        dbHelper.setCardBookmarked(lessonId, cardIndex)
    }

    override suspend fun saveSetting(key: String, value: String): Result<Unit> = runCatching {
        dbHelper.saveSetting(key, value)
    }

    override suspend fun getSetting(key: String, defaultValue: String): String {
        return dbHelper.getSetting(key, defaultValue)
    }
}
