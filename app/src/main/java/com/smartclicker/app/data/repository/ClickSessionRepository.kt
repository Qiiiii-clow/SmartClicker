package com.smartclicker.app.data.repository

import android.content.Context
import com.smartclicker.app.model.ClickSession
import com.smartclicker.app.model.ClickStep
import com.smartclicker.app.model.StepType
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileReader
import java.io.FileWriter
import kotlinx.serialization.json.*

/**
 * 点击方案仓库 - 管理点击方案的保存和加载
 */
class ClickSessionRepository private constructor(context: Context) {
    
    private val storageDir: File
    
    companion object {
        private const val TAG = "ClickSessionRepo"
        private const val STORAGE_DIR = "click_sessions"
        
        @Volatile
        private var instance: ClickSessionRepository? = null
        
        fun getInstance(context: Context): ClickSessionRepository {
            return instance ?: synchronized(this) {
                ClickSessionRepository(context).also {
                    instance = it
                }
            }
        }
    }
    
    init {
        storageDir = File(context.filesDir, STORAGE_DIR).apply {
            if (!exists()) {
                mkdirs()
            }
        }
    }
    
    /**
     * 保存点击方案
     */
    suspend fun saveSession(session: ClickSession): Boolean = withContext(Dispatchers.IO) {
        return@withContext try {
            val file = File(storageDir, "${session.id}.json")
            val json = buildJsonObject {
                put("id", session.id)
                put("name", session.name)
                put("createdAt", session.createdAt)
                put("updatedAt", session.updatedAt)
                put("isFavorite", session.isFavorite)
                
                putJsonArray("steps") { stepsArray ->
                    session.steps.forEach { step ->
                        stepsArray.apply {
                            put(step.id)
                            put("title", step.title)
                            put("stepType", step.stepType.name)
                            put("x", step.x)
                            put("y", step.y)
                            put("delay", step.delay)
                            put("repeatCount", step.repeatCount)
                            step.imageTemplatePath?.let { put("imageTemplatePath", it) }
                            step.colorHex?.let { put("colorHex", it) }
                            step.searchText?.let { put("searchText", it) }
                            step.packageName?.let { put("packageName", it) }
                            step.className?.let { put("className", it) }
                        }
                    }
                }
            }
            
            FileWriter(file).use { writer ->
                writer.write(json.toString())
            }
            
            android.util.Log.d(TAG, "方案已保存: ${session.name}")
            true
        } catch (e: Exception) {
            android.util.Log.e(TAG, "保存方案失败", e)
            false
        }
    }
    
    /**
     * 加载所有点击方案
     */
    suspend fun loadAllSessions(): List<ClickSession> = withContext(Dispatchers.IO) {
        return@withContext try {
            val sessions = mutableListOf<ClickSession>()
            
            storageDir.listFiles()?.forEach { file ->
                if (file.extension == "json") {
                    val json = FileReader(file).use { it.readText() }
                    val jsonElement = Json.parseToJsonElement(json)
                    val jsonObject = jsonElement.jsonObject
                    
                    val steps = mutableListOf<ClickStep>()
                    jsonObject["steps"]?.jsonArray?.forEach { stepJson ->
                        val stepObject = stepJson.jsonObject
                        
                        val step = ClickStep(
                            id = stepObject["id"]?.longOrNull ?: System.currentTimeMillis(),
                            title = stepObject["title"]?.contentOrNull ?: "",
                            stepType = StepType.valueOf(stepObject["stepType"]?.contentOrNull ?: "SIMPLE_CLICK"),
                            x = stepObject["x"]?.doubleOrNull?.toFloat() ?: 0f,
                            y = stepObject["y"]?.doubleOrNull?.toFloat() ?: 0f,
                            delay = stepObject["delay"]?.longOrNull ?: 100,
                            repeatCount = stepObject["repeatCount"]?.intOrNull ?: 1,
                            imageTemplatePath = stepObject["imageTemplatePath"]?.contentOrNull,
                            colorHex = stepObject["colorHex"]?.contentOrNull,
                            searchText = stepObject["searchText"]?.contentOrNull,
                            packageName = stepObject["packageName"]?.contentOrNull,
                            className = stepObject["className"]?.contentOrNull
                        )
                        steps.add(step)
                    }
                    
                    val session = ClickSession(
                        id = jsonObject["id"]?.longOrNull ?: 0,
                        name = jsonObject["name"]?.contentOrNull ?: "未命名方案",
                        steps = steps,
                        createdAt = jsonObject["createdAt"]?.longOrNull ?: System.currentTimeMillis(),
                        updatedAt = jsonObject["updatedAt"]?.longOrNull ?: System.currentTimeMillis(),
                        isFavorite = jsonObject["isFavorite"]?.booleanOrNull ?: false
                    )
                    
                    sessions.add(session)
                }
            }
            
            sessions.sortedByDescending { it.updatedAt }
        } catch (e: Exception) {
            android.util.Log.e(TAG, "加载方案失败", e)
            emptyList()
        }
    }
    
    /**
     * 删除点击方案
     */
    suspend fun deleteSession(sessionId: Long): Boolean = withContext(Dispatchers.IO) {
        return@withContext try {
            val file = File(storageDir, "$sessionId.json")
            if (file.exists()) {
                file.delete()
                android.util.Log.d(TAG, "方案已删除: $sessionId")
                true
            } else {
                false
            }
        } catch (e: Exception) {
            android.util.Log.e(TAG, "删除方案失败", e)
            false
        }
    }
    
    /**
     * 获取所有方案数量
     */
    fun getSessionCount(): Int {
        return storageDir.listFiles { dir, name -> name.endsWith(".json") }?.size ?: 0
    }
}
