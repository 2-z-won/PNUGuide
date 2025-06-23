package com.pnu.pnuguide.data

import android.content.Context
import android.content.res.AssetManager
import org.tensorflow.lite.Interpreter
import org.tensorflow.lite.support.common.FileUtil
import org.json.JSONArray
import java.io.IOException

object ModelLoader {
    fun loadModel(context: Context): Any {
        val assets = context.assets
        return when {
            assetExists(assets, "mobile_feature.tflite") -> {
                val buffer = FileUtil.loadMappedFile(context, "mobile_feature.tflite")
                Interpreter(buffer)
            }
            assetExists(assets, "mobile_feature.ptl") -> {
                val buffer = FileUtil.loadMappedFile(context, "mobile_feature.ptl")
                try {
                    val loaderClass = Class.forName("org.pytorch.LiteModuleLoader")
                    val loadMethod = loaderClass.getMethod("load", java.nio.ByteBuffer::class.java)
                    loadMethod.invoke(null, buffer)
                } catch (e: Exception) {
                    throw IllegalStateException("PyTorch Mobile library missing", e)
                }
            }
            assetExists(assets, "mobile_feature.pt") -> {
                val buffer = FileUtil.loadMappedFile(context, "mobile_feature.pt")
                try {
                    val loaderClass = Class.forName("org.pytorch.LiteModuleLoader")
                    val loadMethod = loaderClass.getMethod("load", java.nio.ByteBuffer::class.java)
                    loadMethod.invoke(null, buffer)
                } catch (e: Exception) {
                    throw IllegalStateException("PyTorch Mobile library missing", e)
                }
            }
            else -> throw IllegalStateException("Model file not found in assets")
        }
    }

    fun loadLabels(context: Context): List<String> {
        val json = context.assets.open("labels.json").bufferedReader().use { it.readText() }
        val array = JSONArray(json)
        return List(array.length()) { idx -> array.getString(idx) }
    }

    private fun assetExists(assets: AssetManager, name: String): Boolean {
        return try {
            assets.open(name).close()
            true
        } catch (e: IOException) {
            false
        }
    }
}
